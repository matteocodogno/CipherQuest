export interface User {
  userId: string;
  level: number;
  username: string;
  coins: number;
  createdAt: Date;
  avatar?: string;
  email?: string;

  [key: string]: unknown;
}
