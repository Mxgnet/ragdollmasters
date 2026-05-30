import { ArenaOverlay } from "@/components/ArenaOverlay";
import { Viewport } from "@/components/Viewport";
import { GameContext } from "@/GameContext";
import {
  createArenaFighter,
  isFighterOutOfBounds,
  respawnArenaFighter,
  type Fighter,
} from "@/lib/arena";
import { useImpactHandler } from "@/lib/handleImpacts";
import { moveBody } from "@/lib/moveBody";
import { useBloodyParticules } from "@/lib/useBloodyParticules";
import {
  Composite,
  PlayerInput,
  SurroundingWalls,
  useEventBeforeUpdate,
} from "@1.framework/matter4react";
import debug from "debug";
import Matter, { Bounds, Vector } from "matter-js";
import { useContext, useEffect, useMemo, useState } from "react";

export const log = debug("@:routes:LeveL1");

const ARENA_SIZE = 6_000;
const RESPAWN_DELAY_MS = 2_000;
const BOT_MOVE_SPEED = 12;
const WORLD_BOUNDS = Bounds.create([
  { x: 0, y: 0 },
  { x: ARENA_SIZE, y: ARENA_SIZE },
]);
const SPAWN_POINTS = [
  { x: 900, y: 900 },
  { x: 1_600, y: 1_250 },
  { x: 2_250, y: 850 },
  { x: 3_200, y: 1_750 },
  { x: 4_150, y: 1_100 },
  { x: 1_300, y: 3_000 },
  { x: 2_700, y: 3_550 },
  { x: 4_500, y: 2_850 },
];

const FIGHTER_SEEDS = [
  { color: "#f4f1de", id: "local", role: "local", spawn: SPAWN_POINTS[0], username: "you" },
  { color: "#e07a5f", id: "bot-rift", role: "bot", spawn: SPAWN_POINTS[1], username: "rift" },
  { color: "#81b29a", id: "bot-echo", role: "bot", spawn: SPAWN_POINTS[2], username: "echo" },
  { color: "#f2cc8f", id: "bot-vex", role: "bot", spawn: SPAWN_POINTS[3], username: "vex" },
  { color: "#9d4edd", id: "bot-mono", role: "bot", spawn: SPAWN_POINTS[4], username: "mono" },
  { color: "#00bbf9", id: "bot-silt", role: "bot", spawn: SPAWN_POINTS[5], username: "silt" },
  { color: "#ef476f", id: "bot-hex", role: "bot", spawn: SPAWN_POINTS[6], username: "hex" },
  { color: "#06d6a0", id: "bot-drift", role: "bot", spawn: SPAWN_POINTS[7], username: "drift" },
] as const;

function spawnArenaFighters() {
  return FIGHTER_SEEDS.map((seed) =>
    createArenaFighter({
      ...seed,
      role: seed.role,
      spawn: Vector.create(seed.spawn.x, seed.spawn.y),
    })
  );
}

export function LeveL1() {
  log("!");
  const { settings } = useContext(GameContext);
  const [fighters, setFighters] = useState<Fighter[]>([]);

  useEffect(() => {
    setFighters(spawnArenaFighters());
  }, []);

  const localFighter = useMemo(
    () => fighters.find((fighter) => fighter.role === "local"),
    [fighters]
  );
  const botFighters = useMemo(
    () => fighters.filter((fighter) => fighter.role === "bot"),
    [fighters]
  );
  const protagonists = useMemo(
    () => fighters.flatMap((fighter) => fighter.composite.bodies),
    [fighters]
  );
  const impactComposites = useMemo(
    () => fighters.map((fighter) => fighter.composite),
    [fighters]
  );
  const onMove = useMemo(() => {
    if (!localFighter) return () => {};
    return moveBody(localFighter.head, settings.speed);
  }, [localFighter, settings.speed]);

  useImpactHandler(impactComposites, [impactComposites]);
  useBloodyParticules(impactComposites, [impactComposites]);

  useEventBeforeUpdate(
    (event) => {
      const player = localFighter;
      if (!player?.alive) return;

      for (const fighter of botFighters) {
        if (!fighter.alive) continue;

        const direction = Vector.normalise(
          Vector.sub(fighter.head.position, player.head.position)
        );

        moveBody(fighter.head, settings.speed)(event, direction, BOT_MOVE_SPEED);
      }
    },
    [botFighters, localFighter, settings.speed]
  );

  useEventBeforeUpdate(
    (event) => {
      const now = event.source.timing.timestamp;

      for (const fighter of fighters) {
        if (fighter.alive && isFighterOutOfBounds(fighter, ARENA_SIZE, 900)) {
          fighter.alive = false;
          fighter.respawnAt = now + RESPAWN_DELAY_MS;
        }

        if (!fighter.alive && fighter.respawnAt && now >= fighter.respawnAt) {
          respawnArenaFighter(fighter);
        }
      }
    },
    [fighters]
  );

  return (
    <>
      <Viewport
        extents={WORLD_BOUNDS}
        followBody={localFighter?.head}
        protagonists={protagonists}
        viewHeight={1_000}
      />
      <PlayerInput map="gamepad" event="move" call={onMove} />
      <SurroundingWalls
        thick={ARENA_SIZE}
        bounds={WORLD_BOUNDS}
        options={{ render: { fillStyle: "#2b2d42" } }}
      />
      {fighters.map((fighter) => (
        <Composite.add key={fighter.id} object={fighter.composite} />
      ))}
      <ArenaOverlay fighters={fighters} worldSize={ARENA_SIZE} />
    </>
  );
}

export default LeveL1;
