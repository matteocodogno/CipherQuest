import { STORY_NAME } from '@/constants.ts'
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

export type RecaptchaVersion = 'v3' | 'v2';

export const signUpApi = async (
  data: SignUpParams & { recaptchaVersion?: RecaptchaVersion }
): Promise<UserLevel> => {
  const response = await fetch(`/api/user/${STORY_NAME}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'recaptcha': data.recaptchaToken ?? '',
      'recaptcha-version': data.recaptchaVersion ?? 'v3',
    },
    body: JSON.stringify(data),
  });

  const payload = await response.json().catch(() => null);

  if (response.status === 409) {
    throw new Error(payload?.message || 'Username already exists.');
  }

  if (response.status === 428 && payload?.error === 'RECAPTCHA_V2_REQUIRED') {
    throw new Error('RECAPTCHA_V2_REQUIRED');
  }

  if (response.status === 403) {
    throw new Error(payload?.message || 'Access Denied');
  }

  if (!response.ok) {
    throw new Error(payload?.message || 'Unexpected error');
  }

  const user = UserLevel.parse(payload);
  logger.debug('signIn', user);
  return user;
};
