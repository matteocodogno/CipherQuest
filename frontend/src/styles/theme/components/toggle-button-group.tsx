import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiToggleButtonGroup = {
  styleOverrides: {
    root: {
      border: '1px solid var(--mui-palette-divider)',
      boxShadow: 'var(--mui-shadows-1)',
      gap: '4px',
      padding: '4px',
    },
    grouped: { border: 0, margin: 0 },
    firstButton: { borderRadius: '8px' },
    middleButton: { borderRadius: '8px' },
    lastButton: { borderRadius: '8px' },
  },
} satisfies Components<Theme>['MuiToggleButtonGroup'];
