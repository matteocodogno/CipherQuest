import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiAutocomplete = {
  styleOverrides: {
    root: {
      '& .MuiOutlinedInput-root': {
        padding: '0px 30px 0px 12px',
        '& .MuiAutocomplete-input': {
          padding: 0,
        },
      },
    },
    paper: {
      border: '1px solid var(--mui-palette-divider)',
      boxShadow: 'var(--mui-shadows-16)',
      marginTop: 6,
    },
    listbox: {
      padding: 8,
      gap: 4,
      '& .MuiAutocomplete-option': {
        padding: '4px 8px',
        borderRadius: 9,
      },
    },
  },
} satisfies Components<Theme>['MuiAutocomplete'];
