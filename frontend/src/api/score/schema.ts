import { z } from 'zod';

export const ScoreboardResponseSchema = z.array(
  z.object({
    index: z.number().nonnegative(),
    username: z.string(),
    userId: z.string(),
    score: z.number().nonnegative(),
    time: z.number().nonnegative(),
  }),
);

export const ScoreResponseSchema = z.object({
  score: z.number().nonnegative(),
});

export const PrizesResponseSchema = z
  .array(
    z.object({
      id: z.string(),
      name: z.string(),
      position: z.number().nonnegative(),
    }),
  )
  .optional();
