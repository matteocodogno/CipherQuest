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
    chat: '/chat',
  },
  notAuthorized: '/errors/not-authorized',
  notFound: '/errors/not-found',
  internalServerError: '/errors/internal-server-error',
} as const;
