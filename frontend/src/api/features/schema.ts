import { z } from 'zod';

export const FeaturesResponseSchema = z.object({
  sendEmail: z.boolean(),
});
