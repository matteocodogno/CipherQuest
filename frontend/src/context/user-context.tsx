import { $, component$, createContextId, Slot, useContextProvider, useStore, useVisibleTask$ } from '@builder.io/qwik';
import { getRandomArbitrary } from '~/utility/number';

export type User = {
  id: number;
  level: number;
  coins: number;
  startedAt: Date;
};

export const applyDefaultUser = (initialUser?: Partial<User>): User => ({
  id: getRandomArbitrary(1000000000, 9999999999),
  level: 1,
  coins: 25,
  startedAt: new Date(),
  ...initialUser,
});

export type UserContextValue = {
  user: User;
  setLevel: (level: number) => void;
  redeemCoin: () => void;
}

export const UserContext = createContextId<UserContextValue>("UserContext");

export type UserProviderProps = {
  user: User;
}

export const UserProvider = component$((initialUser: UserProviderProps) => {
  const user = useStore<User>(initialUser.user);

  useVisibleTask$(async () => {
    const existingJsonUser = localStorage.getItem('user');
    console.log('local storage user', existingJsonUser);

    if ( existingJsonUser === null ) {
      localStorage.setItem('user', JSON.stringify(user));
    } else {
      const existingUser = JSON.parse(existingJsonUser) as User;

      user.id = existingUser.id;
      user.level = existingUser.level;
      user.coins = existingUser.coins;
      user.startedAt = existingUser.startedAt;
    }
  });

  useContextProvider(UserContext, {
    user,
    setLevel: $(async (level: number) => {
      user.level = level;
      user.coins += 10;
      localStorage.setItem('user', JSON.stringify(user));
    }),
    redeemCoin: $(async () => {
      user.coins -= 1;
      localStorage.setItem('user', JSON.stringify(user));
    }),
  });

  return (
    <><Slot /></>
  );
});
