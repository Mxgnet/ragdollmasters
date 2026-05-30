//

import { GameContext } from "@/GameContext";
import { Back } from "@/components/Back";
import { MenuButton } from "@/components/Button";
import { useContext, type ComponentPropsWithoutRef } from "react";
import { useKeyPressEvent } from "react-use";

//

export default GameMode;
export function GameMode({ ...props }: ComponentPropsWithoutRef<"section">) {
  const { sendN, settings, setAgility, setSpeed } = useContext(GameContext);

  useKeyPressEvent("Escape", sendN("BACK"));

  return (
    <div className="vstack h-100%">
      <Back />
      <section
        {...props}
        className={[
          "vstack",
          "flex-1",
          "justify-around",
          "h-100%",
          props.className,
        ].join(" ")}
      >
        <div className="vstack items-center gap-5 px-4">
          <label
            className="vstack items-center gap-2 text-center text-white"
            htmlFor="movement-speed"
          >
            <span className="font-sans text-size-1.25rem uppercase">
              Movement Speed
            </span>
            <span className="font-sans text-size-2rem leading-none">
              {settings.speed}
            </span>
          </label>
          <input
            id="movement-speed"
            min={1}
            max={10}
            step={1}
            type="range"
            value={settings.speed}
            className="w-full max-w-24rem"
            onChange={(event) => setSpeed(Number(event.currentTarget.value))}
          />
          <label
            className="vstack items-center gap-2 text-center text-white"
            htmlFor="movement-agility"
          >
            <span className="font-sans text-size-1.25rem uppercase">
              Movement Agility
            </span>
            <span className="font-sans text-size-2rem leading-none">
              {settings.agility}
            </span>
          </label>
          <input
            id="movement-agility"
            min={1}
            max={10}
            step={1}
            type="range"
            value={settings.agility}
            className="w-full max-w-24rem"
            onChange={(event) => setAgility(Number(event.currentTarget.value))}
          />
        </div>
        <div className="mx-auto max-w-28rem px-4 text-center text-white/72">
          <p className="text-0.8rem uppercase tracking-0.18em">
            Current Build
          </p>
          <p className="mt-2 text-0.95rem leading-relaxed">
            Arena foundations only for now: huge map, named fighters, respawn
            loop, and camera work aimed at the online beta shape.
          </p>
        </div>
        <ul className="vstack items-center ">
          <li className="w-66%">
            <MenuButton onClick={sendN("PLAY_1")}>Arena Prototype</MenuButton>
          </li>
          <li className="w-66%">
            <MenuButton disabled onClick={sendN("PLAY_2VS")}>
              Online Arena
            </MenuButton>
          </li>
          <li className="w-66%">
            <MenuButton disabled onClick={sendN("PLAY_2CO-OP")}>
              Squads
            </MenuButton>
          </li>
        </ul>
      </section>
    </div>
  );
}
