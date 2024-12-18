import { GameSessionInfo } from './type';

export const initializeGameSessionInfo = () => {
  localStorage.setItem('game_session', JSON.stringify({ coins: 25, level: 1 }));
};

export const saveGameSessionInfo = ({ coins, level }: GameSessionInfo) => {
  localStorage.setItem('game_session', JSON.stringify({ coins, level }));
};

export const getGameSessionInfo = (): GameSessionInfo | null => {
  const gameSessionJson = localStorage.getItem('game_session');

  if (!gameSessionJson) return null;

  return JSON.parse(gameSessionJson) as GameSessionInfo;
};

export const deleteSessionInfo = () => {
  localStorage.removeItem('game_session');
};
