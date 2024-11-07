export const paths = {
  home: '/',
  auth: {
    custom: {
      signIn: '/auth/custom/sign-in',
    },
  },
  settings: {
    account: '/settings/account',
  },
  game: {
    rules: '/rules',
    // overview: '/game/overview',
  },
  notAuthorized: '/errors/not-authorized',
  notFound: '/errors/not-found',
  internalServerError: '/errors/internal-server-error',
} as const;
