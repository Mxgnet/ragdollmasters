import {
  useEngine,
  useEngineEvent,
  useRender,
  useRenderEvent,
} from "@1.framework/matter4react";
import type { Fighter } from "@/lib/arena";
import { startTransition, useRef, useState } from "react";

type Props = {
  fighters: Fighter[];
  worldSize: number;
};

type Label = {
  alive: boolean;
  color: string;
  id: string;
  x: number;
  y: number;
  username: string;
};

type Metrics = {
  activeFighters: number;
  bodyCount: number;
  constraintCount: number;
  fps: number;
  frameTime: number;
  heapMb: number | null;
  physicsDelta: number;
  timeScale: number;
  viewportHeight: number;
  viewportWidth: number;
};

export function ArenaOverlay({ fighters, worldSize }: Props) {
  const engine = useEngine();
  const render = useRender();
  const [labels, setLabels] = useState<Label[]>([]);
  const [metrics, setMetrics] = useState<Metrics>({
    activeFighters: 0,
    bodyCount: 0,
    constraintCount: 0,
    fps: 0,
    frameTime: 0,
    heapMb: null,
    physicsDelta: 0,
    timeScale: 1,
    viewportHeight: 0,
    viewportWidth: 0,
  });
  const renderStatsRef = useRef({
    fps: 0,
    frameTime: 0,
    lastAt: 0,
    samples: [] as number[],
  });
  const physicsDeltaRef = useRef(0);
  const publishRef = useRef(0);

  const publishMetrics = (timestamp: number) => {
    if (timestamp - publishRef.current < 250) {
      return;
    }

    publishRef.current = timestamp;

    const memory = (performance as Performance & {
      memory?: { usedJSHeapSize: number };
    }).memory;

    startTransition(() =>
      setMetrics({
        activeFighters: fighters.filter((fighter) => fighter.alive).length,
        bodyCount: engine.world.bodies.length,
        constraintCount: engine.world.constraints.length,
        fps: renderStatsRef.current.fps,
        frameTime: renderStatsRef.current.frameTime,
        heapMb: memory ? memory.usedJSHeapSize / 1024 / 1024 : null,
        physicsDelta: physicsDeltaRef.current,
        timeScale: engine.timing.timeScale,
        viewportHeight: Math.round(render.bounds.max.y - render.bounds.min.y),
        viewportWidth: Math.round(render.bounds.max.x - render.bounds.min.x),
      })
    );
  };

  useEngineEvent(
    "afterUpdate",
    (event) => {
      physicsDeltaRef.current = (event as any).delta ?? 0;
    },
    [engine]
  );

  useRenderEvent(
    "afterRender",
    () => {
      const width = render.canvas.width || window.innerWidth;
      const height = render.canvas.height || window.innerHeight;
      const worldWidth = render.bounds.max.x - render.bounds.min.x;
      const worldHeight = render.bounds.max.y - render.bounds.min.y;
      const now = performance.now();

      if (!worldWidth || !worldHeight || !width || !height) {
        return;
      }

      const renderStats = renderStatsRef.current;
      if (renderStats.lastAt > 0) {
        const frameTime = now - renderStats.lastAt;
        renderStats.frameTime = frameTime;
        renderStats.samples.push(frameTime);
        if (renderStats.samples.length > 30) {
          renderStats.samples.shift();
        }

        const averageFrameTime =
          renderStats.samples.reduce((sum, sample) => sum + sample, 0) /
          renderStats.samples.length;
        renderStats.fps = averageFrameTime > 0 ? 1_000 / averageFrameTime : 0;
      }
      renderStats.lastAt = now;

      const nextLabels = fighters.map((fighter) => ({
        alive: fighter.alive,
        color: fighter.color,
        id: fighter.id,
        username: fighter.username,
        x:
          ((fighter.head.position.x - render.bounds.min.x) / worldWidth) *
          width,
        y:
          ((fighter.head.position.y - 42 - render.bounds.min.y) / worldHeight) *
          height,
      }));

      startTransition(() => setLabels(nextLabels));
      publishMetrics(now);
    },
    [fighters, render]
  );

  return (
    <div className="pointer-events-none absolute inset-0 overflow-hidden">
      <div className="absolute left-4 top-4 max-w-22rem rounded bg-black/45 px-3 py-2 text-white backdrop-blur-sm">
        <p className="text-0.75rem uppercase tracking-0.18em text-white/65">
          Arena Foundations
        </p>
        <p className="mt-1 text-0.95rem font-medium">
          {fighters.length} fighters in a {worldSize} x {worldSize} test arena
        </p>
      </div>

      <div className="absolute bottom-4 right-4 min-w-15rem rounded bg-black/58 px-3 py-3 font-mono text-0.75rem text-white/88 backdrop-blur-sm">
        <p className="uppercase tracking-0.18em text-white/58">Perf HUD</p>
        <p className="mt-2">FPS: {metrics.fps.toFixed(1)}</p>
        <p>Frame: {metrics.frameTime.toFixed(1)} ms</p>
        <p>Physics: {metrics.physicsDelta.toFixed(2)} ms</p>
        <p>Time Scale: {metrics.timeScale.toFixed(2)}</p>
        <p>Fighters: {metrics.activeFighters}/{fighters.length}</p>
        <p>Bodies: {metrics.bodyCount}</p>
        <p>Constraints: {metrics.constraintCount}</p>
        <p>
          View: {metrics.viewportWidth} x {metrics.viewportHeight}
        </p>
        <p>
          Heap: {metrics.heapMb === null ? "n/a" : `${metrics.heapMb.toFixed(1)} MB`}
        </p>
      </div>

      {labels.map((label) => (
        <div
          key={label.id}
          className="absolute left-0 top-0 whitespace-nowrap text-center text-0.75rem font-semibold uppercase tracking-0.14em"
          style={{
            color: label.color,
            opacity: label.alive ? 0.9 : 0.35,
            textShadow: "0 1px 4px rgba(0, 0, 0, 0.85)",
            transform: `translate(${label.x}px, ${label.y}px) translate(-50%, -100%)`,
          }}
        >
          {label.username}
        </div>
      ))}
    </div>
  );
}
