import { createStickman } from "@/utils/createStickman";
import Matter, { Body, Vector } from "matter-js";

export type FighterRole = "local" | "bot" | "remote";

type FighterSnapshot = {
  angle: number;
  offset: Vector;
};

export type Fighter = {
  alive: boolean;
  color: string;
  composite: Matter.Composite;
  head: Body;
  id: string;
  respawnAt: number | null;
  role: FighterRole;
  spawn: Vector;
  snapshot: Map<number, FighterSnapshot>;
  username: string;
};

type FighterSeed = {
  color: string;
  id: string;
  role: FighterRole;
  spawn: Vector;
  username: string;
};

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
        offset: Vector.sub(body.position, head.position),
      },
    ])
  );

  return {
    ...seed,
    alive: true,
    composite,
    head,
    respawnAt: null,
    snapshot,
  };
}

export function respawnArenaFighter(fighter: Fighter) {
  for (const body of fighter.composite.bodies) {
    const snapshot = fighter.snapshot.get(body.id);
    if (!snapshot) continue;

    const position = Vector.add(fighter.spawn, snapshot.offset);
    Body.setAngle(body, snapshot.angle);
    Body.setPosition(body, position);
    Body.setVelocity(body, Vector.create(0, 0));
    Body.setAngularVelocity(body, 0);
    Matter.Sleeping.set(body, false);
  }

  fighter.alive = true;
  fighter.respawnAt = null;
}

export function isFighterOutOfBounds(
  fighter: Fighter,
  worldSize: number,
  margin = 600
) {
  const { x, y } = fighter.head.position;
  return x < -margin || y < -margin || x > worldSize + margin || y > worldSize + margin;
}
