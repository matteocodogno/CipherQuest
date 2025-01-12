import { ReactElement, ReactNode } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import { ThemeProvider } from '@mui/material/styles';
import { createTheme } from '@/styles/theme/create-theme';

export interface ThemeProviderProps {
  children: ReactNode;
}

const CustomThemeProvider = ({ children }: ThemeProviderProps): ReactElement => {
  const theme = createTheme({
    primaryColor: 'neonBlue',
    direction: 'ltr',
  });

  return (
    <ThemeProvider disableTransitionOnChange theme={theme} defaultMode='dark'>
      <CssBaseline />
      {children}
    </ThemeProvider>
  );
}

export { CustomThemeProvider as ThemeProvider };
