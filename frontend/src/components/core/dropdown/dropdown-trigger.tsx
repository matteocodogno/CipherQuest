import { KeyboardEvent, MouseEvent, ReactElement, cloneElement, useContext } from 'react';
import { DropdownContext } from './dropdown-context';

export interface DropdownButtonProps {
  children: ReactElement;
}

export function DropdownTrigger({ children }: DropdownButtonProps): ReactElement {
  const { onTriggerMouseEnter, onTriggerMouseLeave, onTriggerKeyUp } = useContext(DropdownContext);

  return cloneElement(children, {
    onKeyUp: (event: KeyboardEvent<HTMLElement>) => {
      (children.props as { onKeyUp?: (event: KeyboardEvent<HTMLElement>) => void }).onKeyUp?.(event);
      onTriggerKeyUp(event);
    },
    onMouseEnter: (event: MouseEvent<HTMLElement>) => {
      (children.props as { onMouseEnter?: (event: MouseEvent<HTMLElement>) => void }).onMouseEnter?.(event);
      onTriggerMouseEnter(event);
    },
    onMouseLeave: (event: MouseEvent<HTMLElement>) => {
      (children.props as { onMouseLeave?: (event: MouseEvent<HTMLElement>) => void }).onMouseLeave?.(event);
      onTriggerMouseLeave(event);
    },
  });
}
