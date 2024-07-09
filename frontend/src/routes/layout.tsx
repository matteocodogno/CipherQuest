import { component$, Slot, useContextProvider, useStore, useVisibleTask$ } from "@builder.io/qwik";
import { routeLoader$ } from "@builder.io/qwik-city";
import type { RequestHandler } from "@builder.io/qwik-city";

import Header from "../components/starter/header/header";
import Footer from "../components/starter/footer/footer";
import { type User, UserContext } from '~/context/user-context';
import { getRandomArbitrary } from '~/utility/number';

export const onGet: RequestHandler = async ({ cacheControl }) => {
  // Control caching for this request for best performance and to reduce hosting costs:
  // https://qwik.dev/docs/caching/
  cacheControl({
    // Always serve a cached response by default, up to a week stale
    staleWhileRevalidate: 60 * 60 * 24 * 7,
    // Max once every 5 seconds, revalidate on the server to get a fresh version of this page
    maxAge: 5,
  });
};

export const useServerTimeLoader = routeLoader$(() => {
  return {
    date: new Date().toISOString(),
  };
});

export default component$(() => {
  const user = useStore<User>({
    id: getRandomArbitrary(1000000000, 9999999999),
    level: 1,
    startedAt: new Date(),
  });

  useContextProvider(UserContext, user);

  useVisibleTask$(async () => {
    const existingJsonUser = localStorage.getItem('user');

    if ( existingJsonUser === null ) {
      localStorage.setItem('user', JSON.stringify(user))
    } else {
      const existingUser = JSON.parse(existingJsonUser) as User;

      user.id = existingUser.id;
      user.level = existingUser.level;
      user.startedAt = existingUser.startedAt;
    }
  });

  return (
    <>
      <Header />
      <main class="flex flex-col h-[calc(100%-8rem)] items-center">
        <Slot />
      </main>
      <Footer />
    </>
  );
});
