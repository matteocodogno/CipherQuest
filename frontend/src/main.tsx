import { Outlet, RouterProvider, createBrowserRouter } from 'react-router-dom';
import { Root } from '@/root.tsx';
import { ScrollRestoration } from '@/components/core/scroll-restoration.tsx';
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { routes } from '@/routes';

const router = createBrowserRouter([
  {
    path: '/',
    element: (
      <Root>
        <ScrollRestoration />
        <Outlet />
      </Root>
    ),
    children: [...routes],
  },
]);

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
