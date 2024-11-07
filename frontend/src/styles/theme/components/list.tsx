import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiList = {
  styleOverrides: { root: { '--ListItem-gap': '16px' } },
} satisfies Components<Theme>['MuiList'];
