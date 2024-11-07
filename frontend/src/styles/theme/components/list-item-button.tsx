import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiListItemButton = {
  defaultProps: { disableRipple: true },
  styleOverrides: { root: { gap: 'var(--ListItem-gap)' } },
} satisfies Components<Theme>['MuiListItemButton'];
