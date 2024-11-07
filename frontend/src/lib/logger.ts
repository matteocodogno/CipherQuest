 

// NOTE: A tracking system such as Sentry should replace the console

export const LogLevel = { NONE: 'NONE', ERROR: 'ERROR', WARN: 'WARN', DEBUG: 'DEBUG', ALL: 'ALL' } as const;

const LogLevelNumber = { NONE: 0, ERROR: 1, WARN: 2, DEBUG: 3, ALL: 4 } as const;

export type LoggerOptions = {
  prefix?: string;
  level?: keyof typeof LogLevel;
  showLevel?: boolean;
}

export function logger({ prefix: _prefix = '', level: _level = LogLevel.ALL, showLevel = true }: LoggerOptions) {
  const levelNumber = LogLevelNumber[_level];

  const debug = (...args: unknown[]): void => {
    if (canWrite(LogLevel.DEBUG)) {
      write(LogLevel.DEBUG, ...args);
    }
  };

  const warn = (...args: unknown[]): void => {
    if (canWrite(LogLevel.WARN)) {
      write(LogLevel.WARN, ...args);
    }
  };

  const error = (...args: unknown[]): void => {
    if (canWrite(LogLevel.ERROR)) {
      write(LogLevel.ERROR, ...args);
    }
  };

  function canWrite(level: keyof typeof LogLevel): boolean {
    return levelNumber >= LogLevelNumber[level];
  }

  function write(level: keyof typeof LogLevel, ...args: unknown[]): void {
    let prefix = _prefix;

    if (showLevel) {
      prefix = `- ${level} ${prefix}`;
    }

    if (level === LogLevel.ERROR) {
      console.error(prefix, ...args);
    } else {
      console.log(prefix, ...args);
    }
  }

  return {
    debug,
    warn,
    error,
  };
}

// This can be extended to create context specific logger (Server Action, Router Handler, etc.)
// to add context information (IP, User-Agent, timestamp, etc.)

export function createLogger({ prefix, level }: LoggerOptions = {}): ReturnType<typeof logger> {
  return logger({ prefix, level });
}
