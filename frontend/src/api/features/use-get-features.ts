import { FEATURES_URL } from '../constants';
import { FeaturesResponseSchema } from './schema';
import { useQuery } from '@tanstack/react-query';

const useGetFeatures = () =>
  useQuery({
    queryKey: ['features'],
    queryFn: () =>
      fetch(`${FEATURES_URL}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((res) => res.json())
        .then((data) => FeaturesResponseSchema.parse(data)),
  });

export default useGetFeatures;
