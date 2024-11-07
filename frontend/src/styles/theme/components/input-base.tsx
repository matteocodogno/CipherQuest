import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiInputBase = {
  styleOverrides: {
    root: {
      '--Input-borderRadius': '8px',
      '--Input-paddingBlock': 0,
      '--Input-paddingInline': '12px',
      '--Input-minHeight': '40px',
      borderRadius: 'var(--Input-borderRadius)',
      paddingBlock: 'var(--Input-paddingBlock)',
      paddingInline: 'var(--Input-paddingInline)',
      minHeight: 'var(--Input-minHeight)',
    },
    input: {
      alignItems: 'center',
      alignSelf: 'stretch',
      display: 'inline-flex !important', // Fix flicker
      fontSize: 'var(--Input-fontSize, 1rem)',
      '&::placeholder': { color: 'var(--mui-palette-text-secondary)', opacity: 1 },
      '&:-webkit-autofill': {
        borderRadius: 'inherit',
        marginBlock: 'calc(var(--Input-paddingBlock) * -1)',
        marginInline: 'calc(var(--Input-paddingInline) * -1)',
        paddingBlock: 'var(--Input-paddingBlock)',
        paddingInline: 'var(--Input-paddingInline)',
      },
    },
    multiline: { '--Input-paddingBlock': '12px' },
    sizeSmall: { '--Input-fontSize': '0.875rem', '--Input-paddingInline': '8px', '--Input-minHeight': '32px' },
  },
} satisfies Components<Theme>['MuiInputBase'];
