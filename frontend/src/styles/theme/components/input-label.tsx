import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiInputLabel = {
  styleOverrides: { root: { maxWidth: '100%', position: 'static', transform: 'none' } },
} satisfies Components<Theme>['MuiInputLabel'];
