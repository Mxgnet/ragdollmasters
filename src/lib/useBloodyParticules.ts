//

import { ImpactBloom } from "@/lib/ImpactBloom";
import { Particle } from "@/lib/Particle";
import {
  useEngineEvent,
  useEventBeforeUpdate,
} from "@1.framework/matter4react";
import debug from "debug";
import { Body, Common, Composite, Engine, Vector } from "matter-js";
import { useRef, type DependencyList } from "react";
import { isSaveBody } from "./isSaveBody";
import { useStickmanCollision } from "./useStickmanCollision";

//

export const log = debug("@:lib:useBloodyParticules");

//

const BLOOMS_PER_SAFE_IMPACT = 5;
const MAX_ACTIVE_BLOOMS = 90;

export function useBloodyParticules(
  composites: Composite[],
  deps: DependencyList
) {
  log("!");
  const particules = useRef<Particle[]>([]);
  const blooms = useRef<ImpactBloom[]>([]);
  const dead_particules = useRef<(Particle | ImpactBloom)[]>([]);
  const lastSafeImpactAt = useRef(new Map<string, number>());

  useStickmanCollision(
    composites,
    {
      onCollisionStart: (event, { pair }) => {
        const contact = pair.collision.supports.at(0);
        if (!contact) return;

        const isSafeImpact = [pair.bodyA, pair.bodyB].every(isSaveBody);

        if (isSafeImpact) {
          const key = [pair.bodyA.id, pair.bodyB.id].sort((a, b) => a - b).join(":");
          const now = event.source.timing.timestamp;
          const lastImpact = lastSafeImpactAt.current.get(key) ?? -Infinity;
          if (now - lastImpact < 90) {
            return;
          }

          lastSafeImpactAt.current.set(key, now);

          const createdBlooms = Array.from(
            { length: BLOOMS_PER_SAFE_IMPACT },
            (_, index) => {
              const angle = Common.random(0, Math.PI * 2);
              const distance = Common.random(0, 10);
              const drift = {
                x: Math.cos(angle) * Common.random(18, 54),
                y: Math.sin(angle) * Common.random(18, 54) - Common.random(4, 20),
              };

              return new ImpactBloom(
                contact.x + Math.cos(angle) * distance,
                contact.y + Math.sin(angle) * distance,
                Common.random(7, index === 0 ? 18 : 14),
                {
                  drift,
                  growth: Common.random(1.3, 2.1),
                  hueOffset: Common.random(-10, 18),
                  lifetime: Common.random(1.1, 1.8),
                  maxOpacity: index === 0 ? 0.32 : Common.random(0.12, 0.24),
                }
              );
            }
          );

          const nextBlooms = [...createdBlooms, ...blooms.current].map((bloom) => {
            if (!event.source.world.bodies.includes(bloom.body)) {
              Composite.add(event.source.world, bloom.body);
            }
            return bloom;
          });

          blooms.current = nextBlooms.slice(0, MAX_ACTIVE_BLOOMS);
          for (const bloom of nextBlooms.slice(MAX_ACTIVE_BLOOMS)) {
            Composite.remove(event.source.world, bloom.body);
          }
          return;
        }

        const imp = 1 / 10000;
        const impactVelocity = Vector.magnitude(pair.collision.penetration) * 2;
        const particleCount = Math.max(50, Math.floor(impactVelocity));
        particules.current = Array.from({ length: particleCount })
          .map(() => new Particle(contact.x, contact.y, 2))
          .map((p) => {
            Composite.add(event.source.world, p.body);
            Body.applyForce(p.body, p.body.position, {
              x: Common.random(-imp, imp),
              y: Common.random(-imp, imp),
            });
            return p;
          })
          .concat(particules.current);
      },
    },
    deps
  );

  useEventBeforeUpdate((event) => {
    dead_particules.current = [];

    const active_particules = particules.current;
    particules.current = [];
    for (const p of active_particules) {
      p.update(event);
      if (p.life <= 0) {
        dead_particules.current.push(p);
        continue;
      }
      particules.current.push(p);
    }

    const active_blooms = blooms.current;
    blooms.current = [];
    for (const bloom of active_blooms) {
      bloom.update(event);
      if (bloom.life <= 0) {
        dead_particules.current.push(bloom);
        continue;
      }
      blooms.current.push(bloom);
    }

    //
  }, deps);

  useEngineEvent(
    "afterUpdate",
    (event) => {
      const engine = event.source as Engine;
      for (const p of dead_particules.current) {
        Composite.remove(engine.world, p.body);
      }
    },
    deps
  );
}
