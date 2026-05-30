//

import { useRender, useRenderEvent } from "@1.framework/matter4react";
import debug from "debug";
import Matter, { Body, Vector } from "matter-js";
import { useLayoutEffect } from "react";
import { useEvent } from "react-use";

//

const log = debug("@1.framework:matter4react:Viewport");

//

const padding = Vector.create(90, 90);
const DEFAULT_VIEW_HEIGHT = 900;

export function Viewport({
  extents,
  followBody,
  protagonists,
  viewHeight = DEFAULT_VIEW_HEIGHT,
}: Props) {
  log("!");
  const render = useRender();

  const resize = () => {
    const [width, height] = [window.innerWidth, window.innerHeight];
    // TODO(douglasduteil): remove this when Render.setSize is released
    // https://github.com/liabru/matter-js/commit/fc0583975d07f74a7c45e7a84bd3a94b3a2068be
    render.options.width = width;
    render.options.height = height;

    render.canvas.width = width;
    render.canvas.height = height;
  };
  useEvent("resize", resize);
  useLayoutEffect(resize, [render]);

  useRenderEvent(
    "beforeRender",
    () => {
      if (followBody) {
        const aspectRatio =
          render.canvas.height === 0
            ? 1
            : render.canvas.width / render.canvas.height;
        const halfHeight = viewHeight / 2;
        const halfWidth = (viewHeight * aspectRatio) / 2;

        let minX = followBody.position.x - halfWidth;
        let maxX = followBody.position.x + halfWidth;
        let minY = followBody.position.y - halfHeight;
        let maxY = followBody.position.y + halfHeight;

        if (extents) {
          const extentsWidth = extents.max.x - extents.min.x;
          const extentsHeight = extents.max.y - extents.min.y;

          if (extentsWidth > halfWidth * 2) {
            minX = Math.max(extents.min.x, Math.min(minX, extents.max.x - halfWidth * 2));
            maxX = minX + halfWidth * 2;
          }

          if (extentsHeight > halfHeight * 2) {
            minY = Math.max(extents.min.y, Math.min(minY, extents.max.y - halfHeight * 2));
            maxY = minY + halfHeight * 2;
          }
        }

        render.bounds.min.x = minX;
        render.bounds.max.x = maxX;
        render.bounds.min.y = minY;
        render.bounds.max.y = maxY;
        return;
      }

      if (protagonists.length === 0) {
        return;
      }

      Matter.Render.lookAt(render, protagonists, padding, true);
    },
    [extents, followBody, protagonists, render, viewHeight]
  );
  return null;
}

//

type Props = {
  extents?: { max: Vector; min: Vector };
  followBody?: Body;
  protagonists: Body[];
  viewHeight?: number;
};
