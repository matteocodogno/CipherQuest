import type { Components } from '@mui/material/styles';
import type { Theme } from '../types';
import { avatarClasses } from '@mui/material/Avatar';

export const MuiCardHeader = {
  defaultProps: { titleTypographyProps: { variant: 'h6' }, subheaderTypographyProps: { variant: 'body2' } },
  styleOverrides: {
    root: { padding: '32px 24px 16px' },
    avatar: {
      [`& .${avatarClasses.root}`]: {
        '--Icon-fontSize': 'var(--icon-fontSize-lg)',
        backgroundColor: 'var(--mui-palette-background-paper)',
        boxShadow: 'var(--mui-shadows-8)',
        color: 'var(--mui-palette-text-primary)',
      },
    },
  },
} satisfies Components<Theme>['MuiCardHeader'];
