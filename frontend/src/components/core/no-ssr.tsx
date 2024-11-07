import { ReactElement, ReactNode, useEffect, useState } from 'react';
import useEnhancedEffect from '@mui/utils/useEnhancedEffect';

export type NoSsrProps = {
  children?: ReactNode;
  defer?: boolean;
  fallback?: ReactNode;
};

// https://github.com/mui/material-ui/blob/master/packages/mui-base/src/NoSsr/NoSsr.tsx
// without prop-types
export function NoSsr(props: NoSsrProps): ReactElement {
  const { children, defer = false, fallback = null } = props;
  const [mountedState, setMountedState] = useState(false);

  useEnhancedEffect((): void => {
    if (!defer) {
      setMountedState(true);
    }
  }, [defer]);

  useEffect((): void => {
    if (defer) {
      setMountedState(true);
    }
  }, [defer]);

  return <>{mountedState ? children : fallback}</>;
}
