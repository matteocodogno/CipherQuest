import { Outlet, RouteObject } from "react-router-dom";

import { Layout as ChatLayout } from "@/components/chat/layout";
import { Page as NotFoundPage } from "@/pages/not-found";
import { route as customAuth } from "./auth";
import { ScoreboardLayout } from "@/components/scoreboard/layout.tsx";

export const routes: RouteObject[] = [
  {
    path: "/",
    element: (
      <ChatLayout>
        <Outlet />
      </ChatLayout>
    ),
    children: [
      {
        path: 'chat',
        lazy: async () => {
          const { Page } = await import("@/pages/game/chat");
          return { Component: Page };
        }
      },
      {
        path: "rules",
        lazy: async () => {
          const { Page } = await import("@/pages/game/rules");
          return { Component: Page };
        }
      }
    ]
  },
  {
    path: "auth",
    children: [customAuth]
  },
  {
    path: "scoreboard",
    element: (
      <ScoreboardLayout>
        <Outlet />
      </ScoreboardLayout>
    ),
    children: [
      {
        index: true,
        lazy: async () => {
          const { Page } = await import("@/pages/scoreboard/scoreboard.tsx");
          return { Component: Page };
        }
      }
    ]
  },
  { path: "*", element: <NotFoundPage /> }
];
