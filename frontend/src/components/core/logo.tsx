import Box from '@mui/material/Box';
import { NoSsr } from '@/components/core/no-ssr';
import { ReactElement } from 'react';
import { useColorScheme } from '@mui/material/styles';

const HEIGHT = 60;
const WIDTH = 60;

type Color = 'dark' | 'light';

export interface LogoProps {
  color?: Color;
  height?: number;
  width?: number;
  showIconLogo: boolean;
}

export function Logo({
  color = 'dark',
  height = HEIGHT,
  width = WIDTH,
  showIconLogo,
}: LogoProps): ReactElement {
  const logo = color === 'dark' ? '/assets/logo--dark.svg' : '/assets/logo.svg';
  const url = showIconLogo ? '/assets/logo-icon.svg' : logo;

  return (
    <Box alt='logo' component='img' height={height} src={url} width={width} />
  );
}

export interface DynamicLogoProps {
  colorDark?: Color;
  colorLight?: Color;
  emblem?: boolean;
  height?: number;
  width?: number;
  showIconLogo?: boolean;
}

export function DynamicLogo({
  colorDark = 'light',
  colorLight = 'dark',
  height = HEIGHT,
  width = WIDTH,
  showIconLogo = false,
  ...props
}: DynamicLogoProps): ReactElement {
  const { colorScheme } = useColorScheme();
  const color = colorScheme === 'dark' ? colorDark : colorLight;

  return (
    <NoSsr
      fallback={
        <Box
          sx={{
            height: `${height}px`,
            width: `${width}px`,
          }}
        />
      }
    >
      <Logo
        color={color}
        height={height}
        width={width}
        {...props}
        showIconLogo={showIconLogo}
      />
    </NoSsr>
  );
}
