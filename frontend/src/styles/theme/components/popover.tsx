import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiPopover = {
  defaultProps: { disableScrollLock: true, elevation: 16 },
  styleOverrides: { paper: { border: '1px solid var(--mui-palette-divider)' } },
} satisfies Components<Theme>['MuiPopover'];
