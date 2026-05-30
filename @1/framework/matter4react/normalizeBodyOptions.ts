import type Matter from "matter-js";

const defaultSprite: Matter.IBodyRenderOptions["sprite"] = {
  texture: "",
  xScale: 1,
  yScale: 1,
  xOffset: 0,
  yOffset: 0,
};

export function normalizeBodyOptions<T extends Matter.IBodyDefinition | undefined>(
  options: T
): T {
  if (!options?.render) {
    return options;
  }

  return {
    ...options,
    render: {
      ...options.render,
      sprite: {
        ...defaultSprite,
        ...options.render.sprite,
      },
    },
  };
}
