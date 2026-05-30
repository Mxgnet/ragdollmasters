import Matter, { Body, Vector } from "matter-js";

const spriteRender = {
  texture: "",
  xScale: 1,
  yScale: 1,
  xOffset: 0,
  yOffset: 0,
};

export class ImpactBloom {
  body: Body;
  life = 1;

  readonly initialRadius: number;
  readonly growth: number;
  readonly hueOffset: number;
  readonly lifetime: number;
  readonly maxOpacity: number;
  readonly drift: Vector;

  constructor(
    x: number,
    y: number,
    radius: number,
    options?: Partial<{
      drift: Vector;
      growth: number;
      hueOffset: number;
      lifetime: number;
      maxOpacity: number;
    }>
  ) {
    this.initialRadius = radius;
    this.growth = options?.growth ?? 1.8;
    this.hueOffset = options?.hueOffset ?? 0;
    this.lifetime = options?.lifetime ?? 0.38;
    this.maxOpacity = options?.maxOpacity ?? 0.28;
    this.drift = options?.drift ?? { x: 0, y: 0 };

    this.body = Matter.Bodies.circle(x, y, radius, {
      isSensor: true,
      isStatic: true,
      friction: 0,
      collisionFilter: { category: 0, mask: 0 },
      render: {
        fillStyle: this.colorAt(1),
        opacity: this.maxOpacity,
        sprite: spriteRender,
      },
    });
  }

  update(event: Matter.IEventTimestamped<Matter.Engine>) {
    const dt = ((event as any).delta || 1000 / 60) / 1000;
    const nextLife = Math.max(0, this.life - dt / this.lifetime);
    const nextScale = 1 + (1 - nextLife) * this.growth;
    const fade = nextLife * nextLife;

    this.life = nextLife;
    Body.translate(
      this.body,
      Vector.mult(this.drift, dt * (0.35 + (1 - nextLife) * 1.25))
    );
    Body.scale(
      this.body,
      nextScale / this.body.scale.x,
      nextScale / this.body.scale.y
    );
    this.body.render.opacity = this.maxOpacity * fade;
    this.body.render.fillStyle = this.colorAt(nextLife);
  }

  private colorAt(life: number) {
    const hue = 52 + (1 - life) * 42 + this.hueOffset;
    const saturation = 92 - (1 - life) * 18;
    const lightness = 78 - (1 - life) * 26;
    return `hsl(${hue} ${saturation}% ${lightness}%)`;
  }
}
