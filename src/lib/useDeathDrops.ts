import { useEngine, useEventBeforeUpdate } from "@1.framework/matter4react";
import { DeathDrop } from "@/lib/DeathDrop";
import { Common, Composite, Vector } from "matter-js";
import { useRef, type DependencyList } from "react";
import { resizeArenaFighter, type Fighter } from "./arena";

const DROPS_PER_DEATH = 22;
const MAGNET_RADIUS = 180;
const PICKUP_RADIUS = 42;
const GROWTH_PER_DROP = 1.018;
const HEALTH_PER_DROP = 20;
const MAX_SIZE = 2.6;

export function useDeathDrops(fighters: Fighter[], deps: DependencyList) {
  const engine = useEngine();
  const drops = useRef<DeathDrop[]>([]);

  const spawnDrops = (fighter: Fighter) => {
    const center = fighter.head.position;

    const dropCount = Math.round(DROPS_PER_DEATH * fighter.size);

    for (let i = 0; i < dropCount; i += 1) {
      const source = fighter.composite.bodies[i % fighter.composite.bodies.length];
      const position = source?.position ?? center;
      const drop = new DeathDrop(
        position.x + Common.random(-12, 12),
        position.y + Common.random(-12, 12),
        fighter.color
      );

      Composite.add(engine.world, drop.body);
      drops.current.push(drop);
    }
  };

  useEventBeforeUpdate((event) => {
    const activeDrops = drops.current;
    drops.current = [];

    for (const drop of activeDrops) {
      drop.update(event);

      const collector = getClosestCollector(fighters, drop);
      const collectorDistance = collector
        ? Vector.magnitude(Vector.sub(collector.head.position, drop.body.position))
        : Infinity;

      if (collector && collectorDistance < MAGNET_RADIUS) {
        drop.moveToward(collector.head.position, event);
      } else {
        drop.pinToOrigin();
      }

      if (collector && collectorDistance < PICKUP_RADIUS) {
        growFighter(collector);
        drop.removeFrom(event.source.world);
        continue;
      }

      if (drop.life <= 0) {
        drop.removeFrom(event.source.world);
        continue;
      }

      drops.current.push(drop);
    }
  }, deps);

  return { spawnDrops };
}

function getClosestCollector(fighters: Fighter[], drop: DeathDrop) {
  return fighters.reduce<Fighter | null>((closest, fighter) => {
    if (!fighter.alive) {
      return closest;
    }

    if (!closest) {
      return fighter;
    }

    const currentDistance = Vector.magnitude(
      Vector.sub(fighter.head.position, drop.body.position)
    );
    const closestDistance = Vector.magnitude(
      Vector.sub(closest.head.position, drop.body.position)
    );

    return currentDistance < closestDistance ? fighter : closest;
  }, null);
}

function growFighter(fighter: Fighter) {
  fighter.maxHealth += HEALTH_PER_DROP;
  fighter.health = Math.min(fighter.maxHealth, fighter.health + HEALTH_PER_DROP);

  if (fighter.size >= MAX_SIZE) {
    return;
  }

  const nextScale = Math.min(MAX_SIZE, fighter.size * GROWTH_PER_DROP);
  resizeArenaFighter(fighter, nextScale);
}
