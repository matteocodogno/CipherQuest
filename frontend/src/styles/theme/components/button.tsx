import type { Components } from '@mui/material/styles';
import type { Theme } from '../types';
import { buttonClasses } from '@mui/material/Button';

function getContainedVars(color: string): Record<string, string> {
  return {
    '--Button-containedBg': `var(--mui-palette-${color}-dark)`,
    '--Button-containedBgGradient': `linear-gradient(180deg, var(--mui-palette-${color}-main) 0%, var(--mui-palette-${color}-dark) 100%)`,
    '--Button-containedStroke': `inset 0px 0px 0px 1px var(--mui-palette-${color}-dark), inset 0px 2px 0px 0px rgba(255, 255, 255, 0.16)`,
  };
}

function getOutlinedVars(color: string, dark: boolean): Record<string, string> {
  const vars = {
    '--Button-outlinedBorder': `var(--mui-palette-${color}-main)`,
    '--Button-outlinedHoverBg': `var(--mui-palette-${color}-hovered)`,
    '--Button-outlinedActiveBg': `var(--mui-palette-${color}-activated)`,
  };

  // Custom case for secondary
  if (color === 'secondary') {
    if (dark) {
      vars['--Button-outlinedBorder'] = 'var(--mui-palette-secondary-700)';
    } else {
      vars['--Button-outlinedBorder'] = 'var(--mui-palette-secondary-200)';
    }
  }

  return vars;
}

function getTextVars(color: string): Record<string, string> {
  return {
    '--Button-textHoverBg': `var(--mui-palette-${color}-hovered)`,
    '--Button-textActiveBg': `var(--mui-palette-${color}-activated)`,
  };
}

export const MuiButton = {
  defaultProps: { disableRipple: true },
  styleOverrides: {
    root: {
      borderRadius: '8px',
      minHeight: 'var(--Button-minHeight)',
      minWidth: 'unset',
      textTransform: 'none',
      '&:focus-visible': { outline: '2px solid var(--mui-palette-primary-main)' },
    },
    text: {
      '&:hover': { backgroundColor: 'var(--Button-textHoverBg)' },
      '&:active': { backgroundColor: 'var(--Button-textActiveBg)' },
    },
    textPrimary: getTextVars('primary'),
    textSecondary: getTextVars('secondary'),
    textSuccess: getTextVars('success'),
    textInfo: getTextVars('info'),
    textWarning: getTextVars('warning'),
    textError: getTextVars('error'),
    outlined: {
      boxShadow: 'var(--mui-shadows-1)',
      borderColor: 'var(--Button-outlinedBorder)',
      '&:hover': { borderColor: 'var(--Button-outlinedBorder)', backgroundColor: 'var(--Button-outlinedHoverBg)' },
      '&:active': { backgroundColor: 'var(--Button-outlinedActiveBg)' },
    },
    outlinedPrimary: ({ theme }) => {
      return getOutlinedVars('primary', theme.palette.mode === 'dark');
    },
    outlinedSecondary: ({ theme }) => {
      return getOutlinedVars('secondary', theme.palette.mode === 'dark');
    },
    outlinedSuccess: ({ theme }) => {
      return getOutlinedVars('success', theme.palette.mode === 'dark');
    },
    outlinedInfo: ({ theme }) => {
      return getOutlinedVars('info', theme.palette.mode === 'dark');
    },
    outlinedWarning: ({ theme }) => {
      return getOutlinedVars('warning', theme.palette.mode === 'dark');
    },
    outlinedError: ({ theme }) => {
      return getOutlinedVars('error', theme.palette.mode === 'dark');
    },
    contained: {
      backgroundColor: 'var(--Button-containedBg)',
      backgroundImage: 'var(--Button-containedBgGradient)',
      boxShadow: 'var(--mui-shadows-1), var(--Button-containedStroke)',
      overflow: 'hidden',
      '&:hover': {
        boxShadow:
          'var(--mui-shadows-8), var(--Button-containedStroke), inset 0px 6px 10px 0px rgba(255, 255, 255, 0.10)',
      },
      '&:active': { backgroundImage: 'var(--Button-containedBg)' },
      '&:focus-visible': { boxShadow: 'var(--mui-shadows-8)', outlineOffset: '1px' },
      [`&.${buttonClasses.disabled}`]: { backgroundImage: 'none', '&::before': { boxShadow: 'none' } },
    },
    containedPrimary: getContainedVars('primary'),
    containedSecondary: getContainedVars('secondary'),
    containedSuccess: getContainedVars('success'),
    containedInfo: getContainedVars('info'),
    containedWarning: getContainedVars('warning'),
    containedError: getContainedVars('error'),
    sizeSmall: { '--Button-minHeight': '32x', fontSize: '0.8125rem', lineHeight: '24px' },
    sizeMedium: { '--Button-minHeight': '40x', fontSize: '0.875rem', lineHeight: '28px' },
    sizeLarge: { '--Button-minHeight': '48x', fontSize: '0.9375rem', lineHeight: '32px' },
  },
} satisfies Components<Theme>['MuiButton'];
