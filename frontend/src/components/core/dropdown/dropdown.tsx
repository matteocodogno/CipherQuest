import { KeyboardEvent, MouseEvent, ReactElement, ReactNode, useCallback, useRef, useState } from 'react';
import { DropdownContext } from './dropdown-context';

export type DropdownProps = {
  children: ReactNode[];
  delay?: number;
};

export const Dropdown= ({ children, delay = 50 }: DropdownProps): ReactElement => {
  const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);
  const cleanupRef = useRef<number>();

  const handleTriggerMouseEnter = useCallback((event: MouseEvent<HTMLElement>) => {
    clearTimeout(cleanupRef.current);
    setAnchorEl(event.currentTarget);
  }, []);

  const handleTriggerMouseLeave = useCallback(
    (_: MouseEvent<HTMLElement>) => {
      cleanupRef.current = setTimeout(() => {
        setAnchorEl(null);
      }, delay) as unknown as number;
    },
    [delay]
  );

  const handleTriggerKeyUp = useCallback((event: KeyboardEvent<HTMLElement>) => {
    if (event.key === 'Enter' || event.key === ' ') {
      setAnchorEl(event.currentTarget as unknown as HTMLElement);
    }
  }, []);

  const handlePopoverMouseEnter = useCallback((_: MouseEvent<HTMLElement>) => {
    clearTimeout(cleanupRef.current);
  }, []);

  const handlePopoverMouseLeave = useCallback(
    (_: MouseEvent<HTMLElement>) => {
      cleanupRef.current = setTimeout(() => {
        setAnchorEl(null);
      }, delay) as unknown as number;
    },
    [delay]
  );

  const handlePopoverEscapePressed = useCallback(() => {
    setAnchorEl(null);
  }, []);

  const open = Boolean(anchorEl);

  return (
    <DropdownContext.Provider
      value={{
        anchorEl,
        onPopoverMouseEnter: handlePopoverMouseEnter,
        onPopoverMouseLeave: handlePopoverMouseLeave,
        onPopoverEscapePressed: handlePopoverEscapePressed,
        onTriggerMouseEnter: handleTriggerMouseEnter,
        onTriggerMouseLeave: handleTriggerMouseLeave,
        onTriggerKeyUp: handleTriggerKeyUp,
        open,
      }}
    >
      {children}
    </DropdownContext.Provider>
  );
}
