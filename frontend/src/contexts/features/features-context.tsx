import { createContext } from 'react';

export type FeaturesContextValue = {
  features: Record<string, boolean>;
};

export const FeaturesContext = createContext<FeaturesContextValue>({
  features: {},
});
