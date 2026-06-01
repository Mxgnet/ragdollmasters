import { ArenaOverlay } from "@/components/ArenaOverlay";
import { Viewport } from "@/components/Viewport";
import { GameContext } from "@/GameContext";
import {
  animateDeadFighter,
  createArenaFighter,
  respawnArenaFighter,
  type Fighter,
} from "@/lib/arena";
import { useImpactHandler } from "@/lib/handleImpacts";
import { moveBody } from "@/lib/moveBody";
import { useBloodyParticules } from "@/lib/useBloodyParticules";
import { useDeathDrops } from "@/lib/useDeathDrops";
import {
  Composite,
  PlayerInput,
  useEventBeforeUpdate,
} from "@1.framework/matter4react";
import debug from "debug";
import { Vector } from "matter-js";
import { useCallback, useContext, useEffect, useMemo, useState } from "react";

export const log = debug("@:routes:LeveL1");

const BOT_MOVE_SPEED = 12;
const PLAYER_RESPAWN_MIN_DISTANCE = 900;
const PLAYER_RESPAWN_RANDOM_DISTANCE = 1_200;
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
  { color: "#f4f1de", id: "local", role: "local", spawn: SPAWN_POINTS[0]!, username: "you" },
  { color: "#e07a5f", id: "bot-rift", role: "bot", spawn: SPAWN_POINTS[1]!, username: "rift" },
  { color: "#81b29a", id: "bot-echo", role: "bot", spawn: SPAWN_POINTS[2]!, username: "echo" },
  { color: "#f2cc8f", id: "bot-vex", role: "bot", spawn: SPAWN_POINTS[3]!, username: "vex" },
  { color: "#9d4edd", id: "bot-mono", role: "bot", spawn: SPAWN_POINTS[4]!, username: "mono" },
  { color: "#00bbf9", id: "bot-silt", role: "bot", spawn: SPAWN_POINTS[5]!, username: "silt" },
  { color: "#ef476f", id: "bot-hex", role: "bot", spawn: SPAWN_POINTS[6]!, username: "hex" },
  { color: "#06d6a0", id: "bot-drift", role: "bot", spawn: SPAWN_POINTS[7]!, username: "drift" },
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
  const [showDeathScreen, setShowDeathScreen] = useState(false);

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
  const deathDrops = useDeathDrops(fighters, [fighters]);
  const onMove = useMemo(() => {
    if (!localFighter?.alive) return () => {};
    return moveBody(localFighter.head, settings.speed);
  }, [localFighter, settings.speed]);

  const handleFighterKilled = useCallback((fighter: Fighter) => {
    deathDrops.spawnDrops(fighter);
    setFighters((current) => [...current]);

    if (fighter.role === "local") {
      setShowDeathScreen(true);
    }
  }, [deathDrops]);

  const respawnLocalPlayer = useCallback(() => {
    setFighters((current) => {
      const player = current.find((fighter) => fighter.role === "local");
      if (!player) {
        return current;
      }

      player.spawn = getRandomRespawnPoint(player.head.position);
      respawnArenaFighter(player);
      return [...current];
    });
    setShowDeathScreen(false);
  }, []);

  useImpactHandler(fighters, [fighters, handleFighterKilled], {
    onFighterKilled: handleFighterKilled,
  });
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
        animateDeadFighter(fighter, now);

        if (fighter.role === "local") {
          continue;
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
        {...(localFighter ? { followBody: localFighter.head } : {})}
        protagonists={protagonists}
        viewHeight={1_000}
      />
      <PlayerInput map="gamepad" event="move" call={onMove} />
      {fighters.map((fighter) => (
        <Composite.add key={fighter.id} object={fighter.composite} />
      ))}
      <ArenaOverlay fighters={fighters} />
      {showDeathScreen && (
        <div className="pointer-events-auto fixed inset-0 z-10 hstack justify-center bg-black/72 px-6 text-center text-white backdrop-blur-sm">
          <div className="max-w-24rem rounded bg-black/70 px-6 py-7 shadow-2xl">
            <p className="text-2.5rem uppercase tracking-0.14em">You Died</p>
            <p className="mt-3 text-0.95rem text-white/72">
              Respawn into the same fight at a new spot.
            </p>
            <button
              className="mt-6 rounded bg-white px-5 py-3 text-0.9rem font-bold uppercase tracking-0.12em text-black"
              type="button"
              onClick={respawnLocalPlayer}
            >
              Respawn
            </button>
          </div>
        </div>
      )}
    </>
  );
}

export default LeveL1;

function getRandomRespawnPoint(origin: Vector) {
  const angle = Math.random() * Math.PI * 2;
  const distance =
    PLAYER_RESPAWN_MIN_DISTANCE + Math.random() * PLAYER_RESPAWN_RANDOM_DISTANCE;

  return Vector.create(
    origin.x + Math.cos(angle) * distance,
    origin.y + Math.sin(angle) * distance
  );
}
