import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

export const MuiIconButton = {
  defaultProps: { color: 'secondary', disableRipple: true },
  styleOverrides: {
    root: {
      borderRadius: '8px',
      height: 'var(--IconButton-size)',
      width: 'var(--IconButton-size)',
      '&:hover': { backgroundColor: 'rgba(0, 0, 0, 0.04)' },
      '&:focus-visible': { outline: '2px solid var(--mui-palette-primary-main)' },
    },
    colorPrimary: {
      color: 'var(--mui-palette-primary-main)',
      '&:hover': { backgroundColor: 'var(--mui-palette-primary-hovered)' },
      '&:active': { backgroundColor: 'var(--mui-palette-primary-activated)' },
    },
    colorSecondary: {
      color: 'var(--mui-palette-secondary-main)',
      '&:hover': { backgroundColor: 'var(--mui-palette-secondary-hovered)' },
      '&:active': { backgroundColor: 'var(--mui-palette-secondary-activated)' },
    },
    colorInfo: {
      color: 'var(--mui-palette-info-main)',
      '&:hover': { backgroundColor: 'var(--mui-palette-info-hovered)' },
      '&:active': { backgroundColor: 'var(--mui-palette-info-activated)' },
    },
    colorSuccess: {
      color: 'var(--mui-palette-success-main)',
      '&:hover': { backgroundColor: 'var(--mui-palette-success-hovered)' },
      '&:active': { backgroundColor: 'var(--mui-palette-success-activated)' },
    },
    colorWarning: {
      color: 'var(--mui-palette-warning-main)',
      '&:hover': { backgroundColor: 'var(--mui-palette-warning-hovered)' },
      '&:active': { backgroundColor: 'var(--mui-palette-warning-activated)' },
    },
    colorError: {
      color: 'var(--mui-palette-error-main)',
      '&:hover': { backgroundColor: 'var(--mui-palette-error-hovered)' },
      '&:active': { backgroundColor: 'var(--mui-palette-error-activated)' },
    },
    sizeSmall: { '--IconButton-size': '32px' },
    sizeMedium: { '--IconButton-size': '40px' },
    sizeLarge: { '--IconButton-size': '48px' },
  },
} satisfies Components<Theme>['MuiIconButton'];
