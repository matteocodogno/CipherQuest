export enum SenderType {
  USER = 'USER',
  OVERMIND = 'OVERMIND',
}

export type ChatHistory = {
  index: string;
  message: string;
  sender: SenderType;
  timestamp: string;
  info: ChatInfo;
}[];

export type ChatResponse = {
  message: string;
  level: number;
  coins: number;
  info: ChatInfo;
};

export type ChatInfo = {
  isLevelUp: boolean;
};
