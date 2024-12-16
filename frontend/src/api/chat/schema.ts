import { SenderType } from './types';
import { z } from 'zod';

export const ChatHistorySchema = z.array(
  z.object({
    index: z.number(),
    message: z.string(),
    sender: z.enum([SenderType.USER, SenderType.OVERMIND]),
    timestamp: z.string(),
    info: z.object({ isLevelUp: z.boolean() }),
  }),
);
