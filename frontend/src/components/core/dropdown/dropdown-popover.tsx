import { ReactElement, ReactNode, useContext } from 'react';
import { DropdownContext } from './dropdown-context';
import type { PaperProps } from '@mui/material/Paper';
import Popover from '@mui/material/Popover';
import type { PopoverOrigin } from '@mui/material/Popover';


export interface DropdownPopoverProps {
  anchorOrigin?: PopoverOrigin;
  children?: ReactNode;
  disableScrollLock?: boolean;
  PaperProps?: PaperProps;
  transformOrigin?: PopoverOrigin;
}

export function DropdownPopover({ children, PaperProps, ...props }: DropdownPopoverProps): ReactElement {
  const { anchorEl, onPopoverMouseEnter, onPopoverMouseLeave, onPopoverEscapePressed, open } =
    useContext(DropdownContext);

  return (
    <Popover
      anchorEl={anchorEl}
      anchorOrigin={{ horizontal: 'left', vertical: 'bottom' }}
      onClose={(_, reason) => {
        if (reason === 'escapeKeyDown') {
          onPopoverEscapePressed?.();
        }
      }}
      open={open}
      slotProps={{
        paper: {
          ...PaperProps,
          onMouseEnter: onPopoverMouseEnter,
          onMouseLeave: onPopoverMouseLeave,
          sx: { ...PaperProps?.sx, pointerEvents: 'auto' },
        },
      }}
      sx={{ pointerEvents: 'none' }}
      transformOrigin={{ horizontal: 'left', vertical: 'top' }}
      {...props}
    >
      {children}
    </Popover>
  );
}
