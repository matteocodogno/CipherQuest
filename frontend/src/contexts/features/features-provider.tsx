import { ReactElement, ReactNode } from 'react';
import { FeaturesContext } from './features-context';

type FeaturesProviderProps = {
  children: ReactNode;
};
const FeaturesProvider = ({
  children,
}: FeaturesProviderProps): ReactElement => {
  const features = {};

  return (
    <FeaturesContext.Provider value={{ features }}>
      {children}
    </FeaturesContext.Provider>
  );
};

export { FeaturesProvider };
