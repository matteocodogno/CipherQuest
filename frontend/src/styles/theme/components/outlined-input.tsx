import type { Components } from '@mui/material/styles';
import type { Theme } from '../types';
import { inputBaseClasses } from '@mui/material/InputBase';
import { outlinedInputClasses } from '@mui/material/OutlinedInput';

export const MuiOutlinedInput = {
  defaultProps: { notched: false },
  styleOverrides: {
    root: {
      '--Input-focused': 0,
      '--Input-focusedHighlight': 'var(--mui-palette-primary-main)',
      '--Input-focusedThickness': '2px',
      '--Input-borderWidth': '1px',
      '--Input-borderColor': 'var(--mui-palette-OutlinedInput-border)',
      '--Input-boxShadow': 'var(--mui-shadows-1)',
      border: 'var(--Input-borderWidth) solid var(--Input-borderColor)',
      boxShadow: 'var(--Input-boxShadow)',
      '&::before': {
        borderRadius: 'inherit',
        bottom: 0,
        boxShadow: '0 0 0 calc(var(--Input-focused) * var(--Input-focusedThickness)) var(--Input-focusedHighlight)',
        content: '" "',
        left: 0,
        pointerEvents: 'none',
        position: 'absolute',
        right: 0,
        top: 0,
      },
      '&:focus-within': { '&::before': { '--Input-focused': 1 } },
      'label + &': { marginTop: '8px' },
      [`&.${outlinedInputClasses.error}`]: {
        '--Input-borderColor': 'var(--mui-palette-error-main)',
        '--Input-focusedHighlight': 'var(--mui-palette-error-main)',
      },
      [`&.${outlinedInputClasses.disabled}`]: { '--Input-boxShadow': 'none' },
    },
    input: {
      height: 'auto',
      padding: 0,
      [`label[data-shrink=false]+.${inputBaseClasses.formControl} &`]: {
        '&::placeholder': { opacity: '1 !important' },
      },
    },
    notchedOutline: { display: 'none' },
  },
} satisfies Components<Theme>['MuiOutlinedInput'];
