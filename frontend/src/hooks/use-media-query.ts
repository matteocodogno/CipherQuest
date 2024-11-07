import { useEffect, useState } from 'react';
import { useTheme } from '@mui/material/styles';

type Breakpoint = 'xs' | 'sm' | 'md' | 'lg' | 'xl';

type FnName = 'up' | 'down' | 'between' | 'only' | 'not';

export function useMediaQuery(fn: 'up' | 'down', key: Breakpoint | number): boolean;

export function useMediaQuery(fn: 'between', start: Breakpoint | number, end: Breakpoint | number): boolean;

export function useMediaQuery(fn: 'only' | 'not', key: Breakpoint): boolean;

export function useMediaQuery(fn: FnName, start: Breakpoint | number, end?: Breakpoint | number): boolean {
  const theme = useTheme();
  const [matches, setMatches] = useState<boolean>(false);

  let mq: string;

  if (['up', 'down'].includes(fn) && start) {
    mq = theme.breakpoints[fn as 'up' | 'down'](start);
  } else if (fn === 'between' && start && end) {
    mq = theme.breakpoints[fn as 'between'](start, end);
  } else if (['only', 'not'].includes(fn) && start) {
    mq = theme.breakpoints[fn as 'only' | 'not'](start as Breakpoint);
  } else {
    throw new Error('Invalid useMediaQuery params');
  }

  mq = mq.replace(/^@media(?: ?)/m, '');

  useEffect((): (() => void) => {
    setMatches(window.matchMedia(mq).matches);

    function handler(event: MediaQueryListEvent): void {
      setMatches(event.matches);
    }

    const mediaQueryList = window.matchMedia(mq);

    mediaQueryList.addEventListener('change', handler);

    return (): void => {
      mediaQueryList.removeEventListener('change', handler);
    };
  }, [mq]);

  return matches;
}
