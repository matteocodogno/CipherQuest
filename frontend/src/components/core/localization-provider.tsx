import { ReactElement, ReactNode } from 'react';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider as Provider } from '@mui/x-date-pickers/LocalizationProvider';

export type LocalizationProviderProps = {
  children: ReactNode;
};

export function LocalizationProvider({ children }: LocalizationProviderProps): ReactElement {
  return <Provider dateAdapter={AdapterDayjs}>{children}</Provider>;
}
