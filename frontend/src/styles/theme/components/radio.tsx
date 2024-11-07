import * as React from 'react';
import type { Components } from '@mui/material/styles';

import type { Theme } from '../types';

function Icon(): React.JSX.Element {
  return (
    <svg fill='none' height='24' viewBox='0 0 24 24' width='24' xmlns='http://www.w3.org/2000/svg'>
      <path
        clipRule='evenodd'
        d='M12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2ZM12 4C7.58172 4 4 7.58172 4 12C4 16.4183 7.58172 20 12 20C16.4183 20 20 16.4183 20 12C20 7.58172 16.4183 4 12 4Z'
        fill='currentColor'
        fillRule='evenodd'
      />
    </svg>
  );
}

function CheckedIcon(): React.JSX.Element {
  return (
    <svg fill='none' height='24' viewBox='0 0 24 24' width='24' xmlns='http://www.w3.org/2000/svg'>
      <rect fill='currentColor' height='20' rx='10' width='20' x='2' y='2' />
      <rect fill='white' height='8' rx='4' width='8' x='8' y='8' />
    </svg>
  );
}

export const MuiRadio = {
  defaultProps: { checkedIcon: <CheckedIcon />, color: 'primary', disableRipple: true, icon: <Icon /> },
  styleOverrides: { root: { padding: 0 } },
} satisfies Components<Theme>['MuiRadio'];
