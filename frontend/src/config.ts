import { LogLevel } from '@/lib/logger.ts';

export interface Config {
  site: {
    name: string;
  };
  logLevel: keyof typeof LogLevel;
  gtm?: { id?: string };
}

export const config = {
  site: {
    name: 'Overmind',
  },
  logLevel: (import.meta.env.VITE_LOG_LEVEL as keyof typeof LogLevel) || LogLevel.ALL,
  gtm: { id: import.meta.env.VITE_GOOGLE_TAG_MANAGER_ID },
} satisfies Config;
