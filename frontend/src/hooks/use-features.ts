import {
  FeaturesContext,
  FeaturesContextValue,
} from '@/contexts/features/features-context';
import { useContext } from 'react';

export const useFeatures = (): FeaturesContextValue => {
  const context = useContext(FeaturesContext);

  if (!context) {
    throw new Error('useFeatures must be used within a FeaturesProvider');
  }

  return context;
};
