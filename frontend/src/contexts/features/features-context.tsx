import { Features } from '@/api/features/types';
import { createContext } from 'react';

export type FeaturesContextValue = {
  features: Features | undefined;
};

export const FeaturesContext = createContext<FeaturesContextValue>({
  features: { sendEmail: false },
});
