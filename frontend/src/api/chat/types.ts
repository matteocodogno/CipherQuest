export enum SenderType {
  USER = 'USER',
  ASSISTANT = 'ASSISTANT',
}

export type ChatHistory = {
  index: string;
  message: string;
  sender: SenderType;
  timestamp: string;
}[];
