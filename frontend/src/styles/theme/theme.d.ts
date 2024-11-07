import type { ComponentsOverrides, ComponentsProps, ComponentsVariants } from '@mui/material/styles';
import type { TimelineConnectorClassKey, TimelineConnectorProps } from '@mui/lab/TimelineConnector';

declare module '@mui/material/styles/components' {
  interface Components<Theme = unknown> {
    MuiTimelineConnector: {
      defaultProps?: ComponentsProps['MuiTimelineConnector'];
      styleOverrides?: ComponentsOverrides<Theme>['MuiTimelineConnector'];
      variants?: ComponentsVariants<Theme>['MuiTimelineConnector'];
    };
  }
}

declare module '@mui/material/styles/props' {
  interface ComponentsPropsList {
    MuiTimelineConnector: TimelineConnectorProps;
  }
}

declare module '@mui/material/styles/overrides' {
  interface ComponentNameToClassKey {
    MuiTimelineConnector: TimelineConnectorClassKey;
  }
}

declare module '@mui/material/Chip/Chip' {
  interface ChipPropsVariantOverrides {
    soft: true;
  }
}

declare module '@mui/material/Chip/chipClasses' {
  interface ChipClasses {
    soft: string;
    softPrimary: string;
    softSecondary: string;
    softSuccess: string;
    softInfo: string;
    softWarning: string;
    softError: string;
  }
}

declare module '@mui/material/styles/createPalette' {
  interface PaletteRange {
    50: string;
    100: string;
    200: string;
    300: string;
    400: string;
    500: string;
    600: string;
    700: string;
    800: string;
    900: string;
    950: string;
  }

  interface PaletteColor {
    activated: string;
    hovered: string;
    selected: string;
  }

  interface SimplePaletteColorOptions {
    activated?: string;
    hovered?: string;
    selected?: string;
  }

  interface Palette {
    neutral: PaletteRange;
    shadow: string;
    Backdrop: { bg: string };
    OutlinedInput: { border: string };
  }

  interface PaletteOptions {
    neutral?: PaletteRange;
    shadow?: string;
    Backdrop?: { bg?: string };
    OutlinedInput?: { border?: string };
  }

  interface TypeBackground {
    level1: string;
    level2: string;
    level3: string;
  }
}
