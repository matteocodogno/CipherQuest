import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiListItem = {
  styleOverrides: { root: { gap: 'var(--ListItem-gap)' } },
} satisfies Components<Theme>['MuiListItem'];
