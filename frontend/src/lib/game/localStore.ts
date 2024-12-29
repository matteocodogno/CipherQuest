import { GameSessionInfo } from './type';

export const initializeGameSessionInfo = () => {
  // TODO: download default value from backend
  localStorage.setItem('game_session', JSON.stringify({ coins: 25, level: 1, notes: '' }));
};

export const saveGameSessionInfo = ({ coins, level }: Partial<GameSessionInfo>) => {
  saveGameSessionProperties({ coins, level });
};

export const getGameSessionInfo = (): GameSessionInfo | null => {
  const gameSessionJson = localStorage.getItem('game_session');

  if (!gameSessionJson) return null;

  return JSON.parse(gameSessionJson) as GameSessionInfo;
};

export const saveGameNotes = (notes: string) => {
  saveGameSessionProperties({ notes })
};

export const getGameNotes = (): string | undefined => {
  const gameSessionJson = localStorage.getItem('game_session');

  if (!gameSessionJson) return undefined;

  const gameInfo = JSON.parse(gameSessionJson) as GameSessionInfo;

  return gameInfo.notes;
};

export const deleteSessionInfo = () => {
  localStorage.removeItem('game_session');
};

const saveGameSessionProperties = (properties: Record<string, unknown>) => {
  const gameSessionJson = localStorage.getItem('game_session');
  if (!gameSessionJson) return null;

  const gameInfo = JSON.parse(gameSessionJson) as GameSessionInfo;

  localStorage.setItem('game_session', JSON.stringify({...gameInfo, ...properties}))
};
