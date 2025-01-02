import { SignUpParams } from '@/lib/auth/custom/client.ts';
import { logger } from '@/lib/default-loggger.ts';
import { z } from 'zod';

const UserLevel = z.object({
  userId: z.string(),
  username: z.string(),
  level: z.number().int(),
  coins: z.number().int(),
  createdAt: z.string().datetime({ offset: true }),
  terminatedAt: z.string().datetime({ offset: true }).nullable(),
});

export type UserLevel = z.infer<typeof UserLevel>;

export const signUpApi = async (data: SignUpParams): Promise<UserLevel> => {
  const response = await fetch('/api/user', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  });

  if (response.status === 500) {
    throw new Error('Internal Server Error: The server encountered an issue.');
  }

  const jsonResponse = await response.json();
  const user = UserLevel.parse(jsonResponse);
  logger.debug('signIn', user);

  return user;
};
