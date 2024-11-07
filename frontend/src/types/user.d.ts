export interface User {
  id: number;
  level: number;
  username: string;
  coins: number;
  startedAt: Date;
  avatar?: string;
  email?: string;

  [key: string]: unknown;
}
