import type { Components } from '@mui/material/styles';
import type { Theme } from '../types';
import { tableCellClasses } from '@mui/material/TableCell';

export const MuiTableHead = {
  styleOverrides: {
    root: {
      [`& .${tableCellClasses.root}`]: {
        backgroundColor: 'var(--mui-palette-background-level1)',
        color: 'var(--mui-palette-text-secondary)',
        lineHeight: 1,
      },
    },
  },
} satisfies Components<Theme>['MuiTableHead'];
