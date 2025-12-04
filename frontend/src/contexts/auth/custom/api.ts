import {STORY_NAME} from '@/constants.ts'
import {SignUpParams} from '@/lib/auth/custom/client.ts';
import {z} from 'zod';
import {ErrorMessages} from'@/types/errorMessages';

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
  const response = await fetch(`/api/user/${STORY_NAME}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'recaptcha': data.recaptchaToken ?? '',
    },
    body: JSON.stringify(data),
  });

  if (response.status === 409) {
    throw new Error(ErrorMessages.EMAIL_ALREADY_TAKEN);
  }

  if (response.status === 403 || response.status === 400) {
    throw new Error(ErrorMessages.INVALID_RECAPTCHA);
  }

  const jsonResponse = await response.json();
  return UserLevel.parse(jsonResponse);
};
