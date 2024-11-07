import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiListItemAvatar = {
  styleOverrides: { root: { minWidth: 'auto' } },
} satisfies Components<Theme>['MuiListItemAvatar'];
