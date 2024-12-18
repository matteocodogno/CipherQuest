import { Outlet, RouteObject } from 'react-router-dom';

import { Layout as ChatLayout } from '@/components/chat/layout';
import { Page as NotFoundPage } from '@/pages/not-found';
import { route as customAuth } from './auth';

export const routes: RouteObject[] = [
  {
    path: '/',
    element: (
      <ChatLayout>
        <Outlet />
      </ChatLayout>
    ),
    children: [
      {
        path: 'chat',
        lazy: async () => {
          const { Page } = await import('@/pages/game/chat');
          return { Component: Page };
        },
      },
      {
        path: 'rules',
        lazy: async () => {
          const { Page } = await import('@/pages/game/rules');
          return { Component: Page };
        },
      },
    ],
  },
  {
    path: 'auth',
    children: [customAuth],
  },
  { path: '*', element: <NotFoundPage /> },
];
