export enum SenderType {
  USER = 'USER',
  OVERMIND = 'ASSISTANT',
}

export type ChatResponse = {
  message: string;
  level: number;
  coins: number;
  info: ChatInfo;
};

export type ChatInfo = {
  isLevelUp: boolean;
};
