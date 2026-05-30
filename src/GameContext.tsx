//

import { useInterpret } from "@xstate/react";
import {
  createContext,
  useMemo,
  useState,
  type PropsWithChildren,
} from "react";
import type { InterpreterFrom, StateValueFrom } from "xstate";
import { GameFlow } from "./GameFlow";

//

export type GameFlowMachineType = typeof GameFlow;
export type GameFlowMachineInterpreter = InterpreterFrom<GameFlowMachineType>;
export type GameFlowMachineStateValues = StateValueFrom<GameFlowMachineType>;
export type States = StateValueFrom<GameFlowMachineType>;

export type GameFlowMachineSendFn = GameFlowMachineInterpreter["send"];
export type GameSettings = {
  agility: number;
  speed: number;
};

//

export const GameContext = createContext(
  {} as {
    sendN: (
      ...params: Parameters<GameFlowMachineSendFn>
    ) => () => ReturnType<GameFlowMachineSendFn>;
    gameM: GameFlowMachineInterpreter;
    settings: GameSettings;
    setAgility: (agility: number) => void;
    setSpeed: (speed: number) => void;
  }
);

//

export function GameContextProvider({ children }: PropsWithChildren) {
  const game_flow = useInterpret(GameFlow);
  const [agility, setAgility] = useState(7);
  const [speed, setSpeed] = useState(6);
  const sendN =
    (...params: Parameters<typeof game_flow["send"]>) =>
    () =>
      game_flow.send(...params);
  const settings = useMemo(() => ({ agility, speed }), [agility, speed]);

  return (
    <GameContext.Provider
      value={{ sendN, gameM: game_flow, settings, setAgility, setSpeed }}
    >
      {children}
    </GameContext.Provider>
  );
}
