import { GameSessionInfo } from './type';

export const initializeGameSessionInfo = () => {
  localStorage.setItem('game_session', JSON.stringify({ coins: 25, level: 1 }));
};

export const saveCoinsAndLevel = ({ coins, level }: GameSessionInfo) => {
  localStorage.setItem('game_session', JSON.stringify({ coins, level }));
};

export const getGameSessionInfo = (): GameSessionInfo | null => {
  const gameSessionJson = localStorage.getItem('game_session');

  if (!gameSessionJson) return null;

  return JSON.parse(gameSessionJson) as GameSessionInfo;
};

export const saveGameNotes = (notes: string) => {
  const gameSessionJson = localStorage.getItem('game_session');

  if (!gameSessionJson) return null;

  const gameInfo = JSON.parse(gameSessionJson) as GameSessionInfo;
  gameInfo.notes = notes;

  localStorage.setItem('game_session', JSON.stringify(gameInfo));
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
