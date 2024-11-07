import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiTooltip = {
  defaultProps: { placement: 'top' },
  styleOverrides: { tooltip: { backdropFilter: 'blur(6px)' } },
} satisfies Components<Theme>['MuiTooltip'];
