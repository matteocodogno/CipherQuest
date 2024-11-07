import { Outlet } from 'react-router-dom';
import type { RouteObject } from 'react-router-dom';

export const route: RouteObject = {
  path: 'custom',
  element: <Outlet />,
  children: [
    {
      path: 'sign-in',
      lazy: async () => {
        const { Page } = await import('@/pages/auth/sign-in');
        return { Component: Page };
      },
    },
  ],
};
