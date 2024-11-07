import type { Direction, PrimaryColor, Theme } from './types';
import { colorSchemes } from './color-schemes';
import { components } from './components/components';
import { createTheme } from '@mui/material/styles';
import { shadows } from './shadows';
import { typography } from './typography';

type Config = {
  primaryColor: PrimaryColor;
  direction?: Direction;
};

const customCreateTheme = (config: Config): Theme => createTheme({
  breakpoints: { values: { xs: 0, sm: 600, md: 900, lg: 1200, xl: 1440 } },
  colorSchemes: colorSchemes({ primaryColor: config.primaryColor }),
  components,
  cssVariables: {
    colorSchemeSelector: 'class',
  },
  direction: config.direction,
  shadows,
  shape: { borderRadius: 8 },
  typography,
});

export { customCreateTheme as createTheme };
