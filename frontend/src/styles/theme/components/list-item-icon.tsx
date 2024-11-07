import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiListItemIcon = {
  styleOverrides: { root: { color: 'inherit', fontSize: 'var(--icon-fontSize-md)', minWidth: 'auto' } },
} satisfies Components<Theme>['MuiListItemIcon'];
