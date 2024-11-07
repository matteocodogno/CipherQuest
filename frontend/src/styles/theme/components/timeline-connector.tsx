import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiTimelineConnector = {
  styleOverrides: { root: { backgroundColor: 'var(--mui-palette-divider)' } },
} satisfies Components<Theme>['MuiTimelineConnector'];
