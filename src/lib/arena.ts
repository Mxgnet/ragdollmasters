import { createStickman } from "@/utils/createStickman";
import Matter, { Body, Vector } from "matter-js";

export type FighterRole = "local" | "bot" | "remote";

type FighterSnapshot = {
  angle: number;
  collisionFilter: Body["collisionFilter"];
  offset: Vector;
  opacity: number;
};

export type Fighter = {
  alive: boolean;
  color: string;
  composite: Matter.Composite;
  deathAt: number | null;
  head: Body;
  health: number;
  id: string;
  maxHealth: number;
  respawnAt: number | null;
  role: FighterRole;
  size: number;
  spawn: Vector;
  snapshot: Map<number, FighterSnapshot>;
  username: string;
};

type FighterSeed = {
  color: string;
  id: string;
  maxHealth?: number;
  role: FighterRole;
  spawn: Vector;
  username: string;
};

const DEFAULT_MAX_HEALTH = 250;

export function createArenaFighter(seed: FighterSeed): Fighter {
  const composite = createStickman(seed.spawn.x, seed.spawn.y, {
    render: { fillStyle: seed.color },
  });
  const head = composite.bodies.at(0);

  if (!head) {
    throw new Error(`Stickman "${seed.id}" missing head body`);
  }

  const snapshot = new Map(
    composite.bodies.map((body) => [
      body.id,
      {
        angle: body.angle,
        collisionFilter: { ...body.collisionFilter },
        offset: Vector.sub(body.position, head.position),
        opacity: body.render.opacity ?? 1,
      },
    ])
  );

  return {
    ...seed,
    alive: true,
    composite,
    deathAt: null,
    head,
    health: seed.maxHealth ?? DEFAULT_MAX_HEALTH,
    maxHealth: seed.maxHealth ?? DEFAULT_MAX_HEALTH,
    respawnAt: null,
    snapshot,
    size: 1,
  };
}

export function killArenaFighter(fighter: Fighter, now: number, respawnDelayMs: number) {
  if (!fighter.alive) {
    return;
  }

  fighter.alive = false;
  fighter.deathAt = now;
  fighter.respawnAt = now + respawnDelayMs;

  for (const body of fighter.composite.bodies) {
    body.render.opacity = 0.85;
    Body.setAngularVelocity(body, body.angularVelocity + 0.12);
    Body.setVelocity(body, Vector.mult(body.velocity, 1.25));
  }
}

export function animateDeadFighter(fighter: Fighter, now: number) {
  if (fighter.alive || !fighter.deathAt || !fighter.respawnAt) {
    return;
  }

  const duration = fighter.respawnAt - fighter.deathAt;
  const progress = duration <= 0 ? 1 : Math.min(1, (now - fighter.deathAt) / duration);
  const opacity = Math.max(0.08, 0.85 * (1 - progress));

  for (const body of fighter.composite.bodies) {
    body.render.opacity = opacity;
    Body.rotate(body, 0.015 * (1 - progress), fighter.head.position);
  }
}

export function resizeArenaFighter(fighter: Fighter, nextSize: number) {
  if (fighter.size === nextSize) {
    return;
  }

  const scale = nextSize / fighter.size;
  const anchor = fighter.head.position;
  fighter.size = nextSize;

  for (const body of fighter.composite.bodies) {
    const offset = Vector.sub(body.position, anchor);
    Body.scale(body, scale, scale);
    Body.setPosition(body, Vector.add(anchor, Vector.mult(offset, scale)));
  }

  for (const constraint of fighter.composite.constraints) {
    if (constraint.length > 0) {
      constraint.length *= scale;
    }

    if (constraint.pointA) {
      constraint.pointA = Vector.mult(constraint.pointA, scale);
    }

    if (constraint.pointB) {
      constraint.pointB = Vector.mult(constraint.pointB, scale);
    }
  }
}

export function respawnArenaFighter(fighter: Fighter) {
  resizeArenaFighter(fighter, 1);

  for (const body of fighter.composite.bodies) {
    const snapshot = fighter.snapshot.get(body.id);
    if (!snapshot) continue;

    const position = Vector.add(fighter.spawn, Vector.mult(snapshot.offset, fighter.size));
    Body.setAngle(body, snapshot.angle);
    Body.setPosition(body, position);
    Body.setVelocity(body, Vector.create(0, 0));
    Body.setAngularVelocity(body, 0);
    body.collisionFilter = { ...snapshot.collisionFilter };
    body.render.opacity = snapshot.opacity;
    Matter.Sleeping.set(body, false);
  }

  fighter.alive = true;
  fighter.deathAt = null;
  fighter.maxHealth = DEFAULT_MAX_HEALTH;
  fighter.health = fighter.maxHealth;
  fighter.respawnAt = null;
}
