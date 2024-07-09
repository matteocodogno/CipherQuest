import { createContextId } from '@builder.io/qwik';

export type User = {
  id: number;
  level: number;
  startedAt: Date;
};

export const UserContext = createContextId<User>("UserContext");
