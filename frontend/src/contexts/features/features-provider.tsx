import { ReactElement, ReactNode } from 'react';
import { FeaturesContext } from './features-context';
import useGetFeatures from '@/api/features/use-get-features';

type FeaturesProviderProps = {
  children: ReactNode;
};
const FeaturesProvider = ({
  children,
}: FeaturesProviderProps): ReactElement => {
  const { data: features, isError } = useGetFeatures();

  if (isError) {
    throw new Error('Error getting feature status');
  }

  return (
    <FeaturesContext.Provider value={{ features }}>
      {children}
    </FeaturesContext.Provider>
  );
};

export { FeaturesProvider };
