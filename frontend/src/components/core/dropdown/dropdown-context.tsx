import { KeyboardEvent, MouseEvent, createContext } from 'react';

function noop(..._: unknown[]): void {
  // Do nothing
}

export interface ContextValue {
  anchorEl: HTMLElement | null;
  onPopoverMouseEnter: (event: MouseEvent<HTMLElement>) => void;
  onPopoverMouseLeave: (event: MouseEvent<HTMLElement>) => void;
  onPopoverEscapePressed: () => void;
  onTriggerMouseEnter: (event: MouseEvent<HTMLElement>) => void;
  onTriggerMouseLeave: (event: MouseEvent<HTMLElement>) => void;
  onTriggerKeyUp: (event: KeyboardEvent<HTMLElement>) => void;
  open: boolean;
}

export const DropdownContext = createContext<ContextValue>({
  anchorEl: null,
  onPopoverMouseEnter: noop,
  onPopoverMouseLeave: noop,
  onPopoverEscapePressed: noop,
  onTriggerMouseEnter: noop,
  onTriggerMouseLeave: noop,
  onTriggerKeyUp: noop,
  open: false,
});
