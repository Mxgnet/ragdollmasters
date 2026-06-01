import Matter, { Body, Common, Composite, Vector } from "matter-js";

const spriteRender = {
  texture: "",
  xScale: 1,
  yScale: 1,
  xOffset: 0,
  yOffset: 0,
};

export class DeathDrop {
  body: Body;
  life = 1;
  private age = 0;
  private scale = 1;
  private readonly origin: Vector;

  constructor(x: number, y: number, color: string) {
    this.origin = Vector.create(x, y);
    this.body = Matter.Bodies.circle(x, y, Common.random(7, 12), {
      frictionAir: 0.055,
      isSensor: true,
      render: {
        fillStyle: color,
        lineWidth: 3,
        opacity: 1,
        sprite: spriteRender,
        strokeStyle: "#fff6a8",
      },
    });
    Body.setVelocity(this.body, Vector.create(0, 0));
  }

  update(event: Matter.IEventTimestamped<Matter.Engine>) {
    const dt = ((event as any).delta || 1000 / 60) / 1000;
    this.age += dt;
    this.life = Math.max(0, this.life - dt / 18);

    const pulse = 1 + Math.sin(this.age * 8) * 0.18;
    Body.scale(this.body, pulse / this.scale, pulse / this.scale);
    this.scale = pulse;
    this.body.render.lineWidth = 2 + Math.sin(this.age * 10) * 1.2;
    this.body.render.opacity = Math.min(1, 0.55 + this.life * 0.45);
  }

  pinToOrigin() {
    Body.setPosition(this.body, this.origin);
    Body.setVelocity(this.body, Vector.create(0, 0));
    Body.setAngularVelocity(this.body, 0);
  }

  moveToward(target: Vector, event: Matter.IEventTimestamped<Matter.Engine>) {
    const dt = ((event as any).delta || 1000 / 60) / 1000;
    const direction = Vector.normalise(Vector.sub(target, this.body.position));
    const pull = Vector.mult(direction, 44 * dt);
    Body.setVelocity(this.body, Vector.add(Vector.mult(this.body.velocity, 0.9), pull));
  }

  removeFrom(world: Composite) {
    Composite.remove(world, this.body);
  }
}
