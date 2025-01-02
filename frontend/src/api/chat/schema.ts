import { SenderType, UserStatus } from './types';
import { z } from 'zod';

const SourceSchema = z.array(z.object({ id: z.string(), title: z.string() }));

const InfoSchema = z
  .object({
    isLevelUp: z.boolean(),
    sources: SourceSchema,
    status: z.enum([UserStatus.IN_PROGRESS, UserStatus.CHEATED, UserStatus.GAME_OVER, UserStatus.WIN]),
  })
  .nullable();

export const ChatHistorySchema = z.array(
  z.object({
    index: z.number(),
    message: z.string(),
    sender: z.enum([SenderType.USER, SenderType.OVERMIND]),
    timestamp: z.string(),
    info: InfoSchema,
  }),
);

export const ChatResponseSchema = z.object({
  message: z.string(),
  level: z.number(),
  coins: z.number(),
  info: InfoSchema,
});
