import { ReactElement, ReactNode, useEffect, useState } from 'react';
import Alert from '@mui/material/Alert';
import { logger } from '@/lib/default-loggger.ts';
import { paths } from '@/paths';
import { useNavigate } from 'react-router-dom';
import { useUser } from '@/hooks/use-user';

export interface AuthGuardProps {
  children: ReactNode;
}

export const AuthGuard = ({ children }: AuthGuardProps): ReactElement | null => {
  const navigate = useNavigate();
  const { user, error, isLoading } = useUser();
  const [isChecking, setIsChecking] = useState<boolean>(true);

  const checkPermissions = async (): Promise<void> => {
    if (isLoading) {
      return;
    }

    if (error) {
      setIsChecking(false);
      return;
    }

    if (!user) {
      logger.debug('[AuthGuard]: User is not logged in, redirecting to sign in');

      navigate(paths.auth.custom.signIn, { replace: true });
      return;
    }

    setIsChecking(false);
  };

  useEffect(() => {
    checkPermissions().catch(() => {
      // noop
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps -- Expected
  }, [user, error, isLoading]);

  if (isChecking) {
    return null;
  }

  if (error) {
    return <Alert color='error'>{error}</Alert>;
  }

  return <>{children}</>;
}
