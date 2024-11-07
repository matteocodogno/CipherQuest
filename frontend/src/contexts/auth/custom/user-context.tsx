import { ReactElement, ReactNode, createContext, useCallback, useEffect, useState } from 'react';
import { User } from '@/types/user';
import { authClient } from '@/lib/auth/custom/client.ts';
import { logger } from '@/lib/default-loggger.ts';

export type UserProviderProps = {
  children: ReactNode;
};

export type UserContextValue = {
  user: User | null;
  error: string | null;
  isLoading: boolean;
  checkSession?: () => Promise<void>;
  setCoins?: (x: number) => Promise<{error?: string}>;
  setLevel?: (x: number) => Promise<{error?: string}>;
};

// eslint-disable-next-line react-refresh/only-export-components
export const UserContext = createContext<UserContextValue | undefined>(undefined);

export const UserProvider = ({ children }: UserProviderProps): ReactElement => {
  const [state, setState] = useState<{user: User | null, error: string | null, isLoading: boolean}>({
    user: null,
    error: null,
    isLoading: true,
  });

  const checkSession = useCallback(async (): Promise<void> => {
    try {
      const {user, error} = await authClient.getUser();

      if (error) {
        logger.error(error);
        setState({user: null, error: 'Something went wrong!', isLoading: false});
        return;
      }

      setState({user, error: null, isLoading: false});
    } catch (err) {
      logger.error('Error checking session', err);
    }
  }, []);

  const setLevel = useCallback(async (level: number): Promise<{ error?: string }> => {
    return authClient.updateUserProperty('level', level);
  },  []);

  const setCoins = useCallback(async (coins: number): Promise<{ error?: string }> => {
    return authClient.updateUserProperty('coins', coins);
  }, []);

  useEffect(() => {
    checkSession().catch((err: unknown) => {
      logger.error(err);
      // noop
    })
    // eslint-disable-next-line react-hooks/exhaustive-deps -- Expected
  }, []);

  return <UserContext.Provider value={{...state, checkSession, setCoins, setLevel}}>{children}</UserContext.Provider>;
}
