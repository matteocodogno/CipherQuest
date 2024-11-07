import { CaretDown as CaretDownIcon } from '@phosphor-icons/react/dist/ssr/CaretDown';
import type { Components } from '@mui/material/styles';
import { ReactElement } from 'react';
import type { Theme } from '../types';

function IconComponent(): ReactElement {
  return (
    <CaretDownIcon
      fontSize='1em'
      style={{ flex: '0 0 auto', pointerEvents: 'none', position: 'absolute', right: '7px', top: 'calc(50% - .5em)' }}
    />
  );
}

export const MuiSelect = {
  defaultProps: { displayEmpty: true, IconComponent, MenuProps: { sx: { mt: '4px' } } },
  styleOverrides: { select: { height: 'auto', minHeight: 'auto' } },
} satisfies Components<Theme>['MuiSelect'];
