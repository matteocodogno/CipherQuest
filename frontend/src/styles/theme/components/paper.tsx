import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiPaper = {
  styleOverrides: { root: { backgroundImage: 'none' } },
} satisfies Components<Theme>['MuiPaper'];
