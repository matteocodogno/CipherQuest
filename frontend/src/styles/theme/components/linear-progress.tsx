import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiLinearProgress = {
  styleOverrides: { root: { borderRadius: '8px', overflow: 'hidden' } },
} satisfies Components<Theme>['MuiLinearProgress'];
