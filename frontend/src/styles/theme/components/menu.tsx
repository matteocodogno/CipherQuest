import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiMenu = {
  defaultProps: { disableScrollLock: true },
  styleOverrides: { list: { display: 'flex', flexDirection: 'column', gap: '4px', padding: '8px' } },
} satisfies Components<Theme>['MuiMenu'];
