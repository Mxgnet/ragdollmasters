//

import { useEngineEvent, useEventBeforeUpdate } from "@1.framework/matter4react";
import * as colorString from "color-string";
import debug from "debug";
import { cubicIn } from "eases";
import Matter, { Body, Vector } from "matter-js";
import { useRef, type DependencyList } from "react";
import { killArenaFighter, type Fighter } from "./arena";
import { isSaveBody } from "./isSaveBody";
import { moveBody } from "./moveBody";
import { useStickmanCollision } from "./useStickmanCollision";

//

export const log = debug("@:lib:useImpactHandler");

//

type ImpactStrengthPair = [Body, number, Vector];
const SPEED = -30;
const SLOWMO_DURATION_MS = 300;
const SLOWMO_TIMESCALE = 0.82;
const DAMAGE_COOLDOWN_MS = 300;
const DAMAGE_SIZE_MULTIPLIER = 0.35;
const RESPAWN_DELAY_MS = 2_000;

export function useImpactHandler(
  fighters: Fighter[],
  deps: DependencyList,
  callbacks?: {
    onFighterKilled?: (fighter: Fighter) => void;
  }
) {
  log("!");
  const body_colors_ref = useRef(new Map<number, string>());
  const impactedBody = useRef<ImpactStrengthPair[]>([]);
  const lastDamageAt = useRef(new Map<string, number>());
  const slowmoUntilRef = useRef(0);
  const composites = fighters.map((fighter) => fighter.composite);
  const bodyIdToFighter = new Map(
    fighters.flatMap((fighter) =>
      fighter.composite.bodies.map((body) => [body.id, fighter] as const)
    )
  );

  const applyPairDamage = (bodyA: Body, bodyB: Body, now: number) => {
    const fighterA = bodyIdToFighter.get(bodyA.id);
    const fighterB = bodyIdToFighter.get(bodyB.id);
    if (!fighterA?.alive || !fighterB?.alive || fighterA === fighterB) {
      return;
    }

    const damageKey = [fighterA.id, fighterB.id].sort().join(":");
    const lastDamage = lastDamageAt.current.get(damageKey) ?? -Infinity;
    if (now - lastDamage < DAMAGE_COOLDOWN_MS) {
      return;
    }

    lastDamageAt.current.set(damageKey, now);
    applyDamage(fighterA, bodyA, fighterB, now, callbacks?.onFighterKilled);
    applyDamage(fighterB, bodyB, fighterA, now, callbacks?.onFighterKilled);
  };

  useStickmanCollision(
    composites,
    {
      onCollisionStart: (
        event,
        { pair: { bodyA, bodyB, collision } }
      ) => {
        const bodyId_to_color = body_colors_ref.current;

        const contact = collision.supports.at(0);
        if (!contact) return;

        const fighterA = bodyIdToFighter.get(bodyA.id);
        const fighterB = bodyIdToFighter.get(bodyB.id);
        if (!fighterA?.alive || !fighterB?.alive) {
          return;
        }

        const impulse = Vector.normalise(
          Vector.sub(bodyA.position, bodyB.position)
        );

        // save the original color of the two bodies in a map if we don't know it yet
        bodyId_to_color.has(bodyA.id) ||
          bodyId_to_color.set(bodyA.id, bodyA.render.fillStyle!);
        bodyId_to_color.has(bodyB.id) ||
          bodyId_to_color.set(bodyB.id, bodyB.render.fillStyle!);

        const now = event.source.timing.timestamp;
        applyPairDamage(bodyA, bodyB, now);

        // No bleeding from stickman safe bodies labels
        if ([bodyA, bodyB].every(isSaveBody)) {
          return;
        }

        impactedBody.current.push([
          bodyA,
          1,
          Vector.mult(impulse, 10 * bodyB.mass),
        ]);
        impactedBody.current.push([
          bodyB,
          1,
          Vector.mult(Vector.neg(impulse), 10 * bodyA.mass),
        ]);
        slowmoUntilRef.current = event.source.timing.timestamp + SLOWMO_DURATION_MS;
      },
    },
    deps
  );

  useEngineEvent(
    "collisionActive",
    (event) => {
      const activeEvent = event as Matter.IEventCollision<Matter.Engine>;
      const now = activeEvent.source.timing.timestamp;

      for (const pair of activeEvent.pairs) {
        if (pair.bodyA.isStatic || pair.bodyB.isStatic) {
          continue;
        }

        applyPairDamage(pair.bodyA, pair.bodyB, now);
      }
    },
    deps
  );

  useEventBeforeUpdate((event) => {
    const timeScale = (event as any).delta / 1_000;
    const bodyId_to_color = body_colors_ref.current;

    const { current: impacts } = impactedBody;
    const nextImpactedBody: ImpactStrengthPair[] = [];

    while (impacts.length > 0) {
      const impact = impacts.pop();
      if (!impact) {
        break;
      }
      const [body, strength, vec] = impact;

      //

      const color = bodyId_to_color.get(body.id);
      if (!color) {
        continue;
      }
      if (!isSaveBody(body)) {
        let [r, g, b] = colorString.get.rgb(color);
        r += strength * 255;
        g -= strength * g;
        b -= strength * b;
        body.render.fillStyle = colorString.to.hex([Math.min(r, 255), g, b]);
      }

      //

      moveBody(body)(event, vec, SPEED);

      //

      if (strength > 0) {
        nextImpactedBody.push([
          body,
          strength - timeScale,
          Vector.mult(vec, strength),
        ]);
      }
    }

    //

    if (
      event.source.timing.timestamp < slowmoUntilRef.current &&
      nextImpactedBody.length
    ) {
      const lastImpact = nextImpactedBody.at(-1);
      const [, ratio] = lastImpact || [null, 0];
      event.source.timing.timeScale = Math.min(
        1,
        Math.max(SLOWMO_TIMESCALE, cubicIn(1 - ratio * ratio))
      );
    } else if (event.source.timing.timeScale < 1) {
      event.source.timing.timeScale = 1;
    }
    //

    impactedBody.current = nextImpactedBody;

    //
  }, deps);
}

function applyDamage(
  fighter: Fighter,
  body: Body,
  attacker: Fighter,
  now: number,
  onFighterKilled?: (fighter: Fighter) => void
) {
  if (!fighter.alive) {
    return;
  }

  const damage = getDamageForBody(body) * getDamageMultiplier(attacker);
  fighter.health = Math.max(0, fighter.health - damage);

  if (fighter.health > 0) {
    return;
  }

  killArenaFighter(fighter, now, RESPAWN_DELAY_MS);
  onFighterKilled?.(fighter);
}

function getDamageForBody(body: Body) {
  if (body.label === "Head") {
    return 35;
  }

  if (body.label === "Chest") {
    return 22;
  }

  if (body.label.includes("Upper")) {
    return 14;
  }

  return 10;
}

function getDamageMultiplier(attacker: Fighter) {
  return 1 + Math.max(0, attacker.size - 1) * DAMAGE_SIZE_MULTIPLIER;
}
