import { CaretLeft as CaretLeftIcon } from '@phosphor-icons/react/dist/ssr/CaretLeft';
import { CaretRight as CaretRightIcon } from '@phosphor-icons/react/dist/ssr/CaretRight';
import type { Components } from '@mui/material/styles';
import { ReactElement } from 'react';
import type { Theme } from '../types';

function PreviousButtonIcon(): ReactElement {
  return <CaretLeftIcon fontSize='var(--fontSize-md)' />;
}

function NextButtonIcon(): ReactElement {
  return <CaretRightIcon fontSize='var(--fontSize-md)' />;
}

export const MuiTablePagination = {
  defaultProps: {
    slotProps: {
      actions: { nextButtonIcon: { component: NextButtonIcon }, previousButtonIcon: { component: PreviousButtonIcon } },
      select: { size: 'small', variant: 'outlined' },
    },
  },
  styleOverrides: { input: { marginRight: '16px', padding: 0 } },
} satisfies Components<Theme>['MuiTablePagination'];
