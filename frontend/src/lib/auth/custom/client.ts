import type { User } from '@/types/user';
import { initializeGameSessionInfo } from '@/lib/game/localStore';
import { signUpApi } from '@/api/auth/api.ts';

export type SignUpParams = {
  username: string;
  firstName?: string;
  lastName?: string;
};

export const getRandomArbitrary = (min: number, max: number) =>
  Math.ceil(Math.random() * (max - min) + min);

const authClientBuilder = () => ({
  signUp: async (params: SignUpParams): Promise<{ error?: string }> => {
    try {
      const user = await signUpApi(params);

      localStorage.setItem('user', JSON.stringify(user));
      initializeGameSessionInfo();

      return {};
    } catch (error) {
      console.debug({ error });

      if (error instanceof Error) {
        return { error: error.message };
      }

      return {};
    }
  },

  updateUserProperty: (key: string, value: unknown) => {
    const userJson = localStorage.getItem('user');

    if (!userJson) return { error: 'User not found' };

    const user = JSON.parse(userJson) as User;
    user[key] = value;

    localStorage.setItem('user', JSON.stringify(user));

    return {};
  },

  getUser: async (): Promise<{ user: User | null; error?: string }> => {
    const userJson = localStorage.getItem('user');

    if (!userJson) return { user: null };

    return { user: JSON.parse(userJson) as User };
  },

  isLogged: async (): Promise<boolean> => Boolean(localStorage.getItem('user')),

  signOut: async (): Promise<{ error?: string }> => {
    localStorage.removeItem('user');
    localStorage.removeItem('game_session');
    return {};
  },
});

export const authClient = authClientBuilder();
