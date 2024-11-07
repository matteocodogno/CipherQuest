import type { Components } from '@mui/material/styles';
import type { Theme } from '../types';
import { listItemIconClasses } from '@mui/material/ListItemIcon';

export const MuiMenuItem = {
  defaultProps: { disableRipple: true },
  styleOverrides: {
    root: {
      borderRadius: '8px',
      gap: 'var(--ListItem-gap)',
      paddingBlock: 'var(--MenuItem-paddingBlock, 4px)',
      paddingInline: 'var(--MenuItem-paddingInline, 8px)',
      [`& .${listItemIconClasses.root}`]: { minWidth: 'auto' },
    },
  },
} satisfies Components<Theme>['MuiMenuItem'];
