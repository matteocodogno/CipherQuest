import { ErrorMessages } from '@/types/errorMessages';
import { STORY_NAME } from '@/constants.ts';
import { SignUpParams } from '@/lib/auth/custom/client.ts';
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
  data: SignUpParams & { recaptchaVersion?: RecaptchaVersion },
): Promise<UserLevel> => {
  const response = await fetch(`/api/user/${STORY_NAME}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      recaptcha: data.recaptchaToken ?? '',
      'recaptcha-version': data.recaptchaVersion ?? 'v3',
    },
    body: JSON.stringify(data),
  });

const body = await response.json().catch(() => null);

  if (response.status === 409) {
    throw new Error(ErrorMessages.EMAIL_ALREADY_TAKEN);
  }

  if (response.status === 403 || response.status === 400) {
    throw new Error(ErrorMessages.INVALID_RECAPTCHA);
  }

  if(response.status === 428) {
    throw new Error(ErrorMessages.PRECONDITION_REQUIRED);
  }
  return UserLevel.parse(body);
};
