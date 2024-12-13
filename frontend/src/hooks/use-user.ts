import {
  UserContext,
  UserContextValue,
} from '@/contexts/auth/custom/user-context.tsx';
import { useContext } from 'react';

export const useUser = (): UserContextValue => {
  const context = useContext(UserContext);

  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }

  return context;
};
