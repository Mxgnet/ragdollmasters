export interface RagdollState {
  id: string;
  bodies: Array<{
    id: string;
    position: { x: number; y: number };
    velocity: { x: number; y: number };
    angle: number;
  }>;
}

export function serializeRagdollState(composite: Matter.Composite): RagdollState {
  return {
    id: composite.id.toString(),
    bodies: composite.bodies.map(body => ({
      id: body.id.toString(),
      position: { ...body.position },
      velocity: { ...body.velocity },
      angle: body.angle
    }))
  };
} 