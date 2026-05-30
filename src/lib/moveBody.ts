//

import type { GameSettings } from "@/GameContext";
import { Body, Engine, Vector, type IEventTimestamped } from "matter-js";

//

const SPEED = 20;
const AGILITY_STEP = 0.16;
const BURST_FORCE = 1.35;
const DEFAULT_SPEED_SETTING = 6;
const DEFAULT_AGILITY_SETTING = 6;
const SPEED_STEP = 0.2;

//

function speedMultiplier(speedSetting = DEFAULT_SPEED_SETTING) {
  return 1 + (speedSetting - DEFAULT_SPEED_SETTING) * SPEED_STEP;
}

function agilityMultiplier(agilitySetting = DEFAULT_AGILITY_SETTING) {
  return 1 + (agilitySetting - DEFAULT_AGILITY_SETTING) * AGILITY_STEP;
}

function normalizeSettings(settings: number | GameSettings | undefined) {
  if (typeof settings === "number") {
    return {
      agility: DEFAULT_AGILITY_SETTING,
      speed: settings,
    };
  }

  return {
    agility: settings?.agility ?? DEFAULT_AGILITY_SETTING,
    speed: settings?.speed ?? DEFAULT_SPEED_SETTING,
  };
}

export function moveBody(body: Body, settings?: number | GameSettings) {
  const movement = normalizeSettings(settings);
  const speed = speedMultiplier(movement.speed);
  const agility = agilityMultiplier(movement.agility);
  let lastDirection = Vector.create();

  return (
    event: IEventTimestamped<Engine>,
    direction: Vector,
    intensity = SPEED
  ) => {
    if (Vector.magnitudeSquared(direction) === 0) {
      lastDirection = Vector.create();
      return;
    }

    const normalizedDirection = Vector.normalise(direction);
    const alignment =
      Vector.magnitudeSquared(lastDirection) === 0
        ? 1
        : Vector.dot(normalizedDirection, lastDirection);
    const directionChangeBoost =
      alignment < 0.5 ? 1 + (agility - 1) * BURST_FORCE : 1;
    const counterVelocityBoost =
      1 + Math.max(0, -Vector.dot(normalizedDirection, body.velocity)) * 0.06;
    const appliedIntensity =
      intensity *
      speed *
      agility *
      directionChangeBoost *
      counterVelocityBoost;
    const dt = (event as any).delta * 1_000;

    Body.applyForce(
      body,
      body.position,
      Vector.mult(
        normalizedDirection,
        (-1 * appliedIntensity * body.mass) / dt
      )
    );

    lastDirection = normalizedDirection;
  };
}
