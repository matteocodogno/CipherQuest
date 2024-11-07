import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiFormControlLabel = {
  defaultProps: { slotProps: { typography: { variant: 'subtitle2' } } },
  styleOverrides: { root: { gap: '8px', margin: 0 } },
} satisfies Components<Theme>['MuiFormControlLabel'];
