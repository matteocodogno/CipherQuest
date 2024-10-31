import type { QRL } from "@builder.io/qwik";
import {
  $,
  component$,
  createContextId,
  Slot,
  useContextProvider,
  useStore,
  useTask$,
} from "@builder.io/qwik";
import { getRandomArbitrary } from "~/utility/number";
import {isBrowser} from "@builder.io/qwik/build";

export type User = {
  id: number;
  username: string;
  level: number;
  coins: number;
  startedAt: Date;
};

export const applyDefaultUser = (initialUser?: Partial<User>): User => ({
  id: getRandomArbitrary(1000000000, 9999999999),
  username: "",
  level: 1,
  coins: 25,
  startedAt: new Date(),
  ...initialUser,
});

export type UserContextValue = {
  user: User;
  setLevel: QRL<(level: number) => Promise<void>>;
  setCoins: QRL<(coins: number) => Promise<void>>;
  isLogged: QRL<() => Promise<boolean>>;
};

export const UserContext = createContextId<UserContextValue>("UserContext");

export type UserProviderProps = {
  user: User;
};

export const UserProvider = component$((initialUser: UserProviderProps) => {
  const user = useStore<User>(initialUser.user);

  useTask$(async () => {
    if (isBrowser) {
      const existingJsonUser = localStorage.getItem("user");

      if (existingJsonUser !== null) {
        const existingUser = JSON.parse(existingJsonUser) as User;
        user.id = existingUser.id;
        user.username = existingUser.username;
        user.level = existingUser.level;
        user.coins = existingUser.coins;
        user.startedAt = existingUser.startedAt;
        user.username = existingUser.username;
      }
    }
  });

  useContextProvider(UserContext, {
    user,
    setLevel: $(async (level: number) => {
      user.level = level;
      localStorage.setItem("user", JSON.stringify(user));
    }),
    setCoins: $(async (coins: number) => {
      user.coins = coins;
      localStorage.setItem("user", JSON.stringify(user));
    }),
    isLogged: $(async () => Boolean(localStorage.getItem("user"))),
  });

  return (
    <>
      <Slot />
    </>
  );
});
