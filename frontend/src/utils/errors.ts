import { SIGNUP_ERROR } from '@/api/auth/constants';

export const mapEmailSignupErrors = (error: string) => {
  switch (error) {
    case SIGNUP_ERROR.USERNAME_ALREADY_EXISTS: {
      return 'Email address already exists. Use another address please.';
    }
    default: {
      return 'An error occurred, please try again.';
    }
  }
};

export const mapUsernameSignupErrors = (error: string) => {
  switch (error) {
    case SIGNUP_ERROR.USERNAME_ALREADY_EXISTS: {
      return 'Username already exists, try a different one';
    }
    default: {
      return 'An error occurred, please try again.';
    }
  }
};
