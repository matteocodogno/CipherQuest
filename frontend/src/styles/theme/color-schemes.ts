import type { ColorScheme, PrimaryColor } from './types';
import type {
  ColorSystemOptions,
  PaletteColorOptions,
} from '@mui/material/styles';
import {
  california,
  chateauGreen,
  kepple,
  neonBlue,
  nevada,
  redOrange,
  royalBlue,
  shakespeare,
  stormGrey,
  tomatoOrange,
} from './colors';
import { logger } from '@/lib/default-loggger.ts';

const primarySchemes: Record<
  PrimaryColor,
  Record<ColorScheme, PaletteColorOptions>
> = {
  chateauGreen: {
    dark: {
      ...chateauGreen,
      light: chateauGreen[300],
      main: chateauGreen[400],
      dark: chateauGreen[500],
      contrastText: 'var(--mui-palette-common-black)',
      activated:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
      hovered:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
      selected:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
    },
    light: {
      ...chateauGreen,
      light: chateauGreen[400],
      main: chateauGreen[500],
      dark: chateauGreen[600],
      contrastText: 'var(--mui-palette-common-white)',
      activated:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
      hovered:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
      selected:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
    },
  },
  neonBlue: {
    dark: {
      ...neonBlue,
      light: neonBlue[300],
      main: neonBlue[400],
      dark: neonBlue[500],
      contrastText: 'var(--mui-palette-common-black)',
      activated:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
      hovered:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
      selected:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
    },
    light: {
      ...neonBlue,
      light: neonBlue[400],
      main: neonBlue[500],
      dark: neonBlue[600],
      contrastText: 'var(--mui-palette-common-white)',
      activated:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
      hovered:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
      selected:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
    },
  },
  royalBlue: {
    dark: {
      ...royalBlue,
      light: royalBlue[300],
      main: royalBlue[400],
      dark: royalBlue[500],
      contrastText: 'var(--mui-palette-common-black)',
      activated:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
      hovered:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
      selected:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
    },
    light: {
      ...royalBlue,
      light: royalBlue[400],
      main: royalBlue[500],
      dark: royalBlue[600],
      contrastText: 'var(--mui-palette-common-white)',
      activated:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
      hovered:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
      selected:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
    },
  },
  tomatoOrange: {
    dark: {
      ...tomatoOrange,
      light: tomatoOrange[300],
      main: tomatoOrange[400],
      dark: tomatoOrange[500],
      contrastText: 'var(--mui-palette-common-black)',
      activated:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
      hovered:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
      selected:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
    },
    light: {
      ...tomatoOrange,
      light: tomatoOrange[400],
      main: tomatoOrange[500],
      dark: tomatoOrange[600],
      contrastText: 'var(--mui-palette-common-white)',
      activated:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
      hovered:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
      selected:
        'rgba(var(--mui-palette-primary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
    },
  },
};

interface Config {
  primaryColor: PrimaryColor;
}

export function colorSchemes(
  config: Config,
): Partial<Record<ColorScheme, ColorSystemOptions>> {
  let primary = primarySchemes[config.primaryColor];

  if (!primary) {
    logger.warn(
      `No primary color found for ${config.primaryColor}. Using neonBlue instead.`,
    );
    primary = primarySchemes.neonBlue;
  }

  return {
    dark: {
      palette: {
        action: { disabledBackground: 'rgba(0, 0, 0, 0.12)' },
        background: {
          default: 'var(--mui-palette-neutral-950)',
          defaultChannel: '9 10 11',
          paper: 'var(--mui-palette-neutral-900)',
          paperChannel: '18 21 23',
          level1: 'var(--mui-palette-neutral-800)',
          level2: 'var(--mui-palette-neutral-700)',
          level3: 'linear-gradient(180deg, #635BFF33, #7578FF33)',
        },
        common: { black: '#000000', white: '#ffffff' },
        divider: 'var(--mui-palette-neutral-700)',
        dividerChannel: '50 56 62',
        error: {
          ...redOrange,
          light: redOrange[300],
          main: redOrange[400],
          dark: redOrange[500],
          contrastText: 'var(--mui-palette-common-black)',
          activated:
            'rgba(var(--mui-palette-error-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-error-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-error-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        info: {
          ...shakespeare,
          light: shakespeare[300],
          main: shakespeare[400],
          dark: shakespeare[500],
          contrastText: 'var(--mui-palette-common-black)',
          activated:
            'rgba(var(--mui-palette-info-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-info-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-info-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        neutral: { ...nevada },
        primary: primary.dark,
        secondary: {
          ...nevada,
          light: nevada[100],
          main: nevada[200],
          dark: nevada[300],
          contrastText: 'var(--mui-palette-common-black)',
          activated:
            'rgba(var(--mui-palette-secondary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-secondary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-secondary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        success: {
          ...kepple,
          light: kepple[300],
          main: kepple[400],
          dark: kepple[500],
          contrastText: 'var(--mui-palette-common-black)',
          activated:
            'rgba(var(--mui-palette-success-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-success-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-success-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        text: {
          primary: 'var(--mui-palette-neutral-100)',
          primaryChannel: '240 244 248',
          secondary: 'var(--mui-palette-neutral-400)',
          secondaryChannel: '159 166 173',
          disabled: 'var(--mui-palette-neutral-600)',
        },
        warning: {
          ...california,
          light: california[300],
          main: california[400],
          dark: california[500],
          contrastText: 'var(--mui-palette-common-black)',
          activated:
            'rgba(var(--mui-palette-warning-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-warning-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-warning-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        shadow: 'rgba(0, 0, 0, 0.5)',
        Avatar: { defaultBg: 'var(--mui-palette-neutral-200)' },
        Backdrop: { bg: 'rgba(0, 0, 0, 0.5)' },
        OutlinedInput: { border: 'var(--mui-palette-neutral-700)' },
        TableCell: { border: 'var(--mui-palette-divider)' },
        Tooltip: { bg: 'rgba(10, 13, 20, 0.75)' },
      },
    },
    light: {
      palette: {
        action: { disabledBackground: 'rgba(0, 0, 0, 0.06)' },
        background: {
          default: 'var(--mui-palette-common-white)',
          defaultChannel: '255 255 255',
          paper: 'var(--mui-palette-common-white)',
          paperChannel: '255 255 255',
          level1: 'var(--mui-palette-neutral-50)',
          level2: 'var(--mui-palette-neutral-100)',
          level3: '',
        },
        common: { black: '#000000', white: '#ffffff' },
        divider: 'var(--mui-palette-neutral-200)',
        dividerChannel: '220 223 228',
        error: {
          ...redOrange,
          light: redOrange[400],
          main: redOrange[500],
          dark: redOrange[600],
          contrastText: 'var(--mui-palette-common-white)',
          activated:
            'rgba(var(--mui-palette-error-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-error-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-error-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        info: {
          ...shakespeare,
          light: shakespeare[400],
          main: shakespeare[500],
          dark: shakespeare[600],
          contrastText: 'var(--mui-palette-common-white)',
          activated:
            'rgba(var(--mui-palette-info-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-info-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-info-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        neutral: { ...stormGrey },
        primary: primary.light,
        secondary: {
          ...nevada,
          light: nevada[600],
          main: nevada[700],
          dark: nevada[800],
          contrastText: 'var(--mui-palette-common-white)',
          activated:
            'rgba(var(--mui-palette-secondary-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-secondary-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-secondary-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        success: {
          ...kepple,
          light: kepple[400],
          main: kepple[500],
          dark: kepple[600],
          contrastText: 'var(--mui-palette-common-white)',
          activated:
            'rgba(var(--mui-palette-success-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-success-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-success-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        text: {
          primary: 'var(--mui-palette-neutral-900)',
          primaryChannel: '33 38 54',
          secondary: 'var(--mui-palette-neutral-500)',
          secondaryChannel: '102 112 133',
          disabled: 'var(--mui-palette-neutral-400)',
        },
        warning: {
          ...california,
          light: california[400],
          main: california[500],
          dark: california[600],
          contrastText: 'var(--mui-palette-common-white)',
          activated:
            'rgba(var(--mui-palette-warning-mainChannel) / var(--mui-palette-action-activatedOpacity))',
          hovered:
            'rgba(var(--mui-palette-warning-mainChannel) / var(--mui-palette-action-hoverOpacity))',
          selected:
            'rgba(var(--mui-palette-warning-mainChannel) / var(--mui-palette-action-selectedOpacity))',
        },
        shadow: 'rgba(0, 0, 0, 0.08)',
        Avatar: { defaultBg: 'var(--mui-palette-neutral-600)' },
        Backdrop: { bg: 'rgb(18, 22, 33, 0.8)' },
        OutlinedInput: { border: 'var(--mui-palette-neutral-200)' },
        TableCell: { border: 'var(--mui-palette-divider)' },
        Tooltip: { bg: 'rgba(10, 13, 20, 0.75)' },
      },
    },
  };
}
