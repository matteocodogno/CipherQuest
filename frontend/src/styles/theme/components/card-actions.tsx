import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiCardActions = {
  styleOverrides: { root: { padding: '8px 16px 16px' } },
} satisfies Components<Theme>['MuiCardActions'];
