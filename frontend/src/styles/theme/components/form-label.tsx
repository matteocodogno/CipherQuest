import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiFormLabel = {
  styleOverrides: { root: { color: 'var(--mui-palette-text-primary)', fontSize: '0.875rem', fontWeight: 500 } },
} satisfies Components<Theme>['MuiFormLabel'];
