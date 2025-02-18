import { PRIZES_URL } from '../constants';
import { PrizesResponseSchema } from './schema';
import { STORY_NAME } from '@/constants.ts'
import { useQuery } from '@tanstack/react-query';

const useGetPrizes = () => {
  const {
    isLoading,
    isError,
    data: prizes,
  } = useQuery({
    queryKey: ['prizes'],
    queryFn: () =>
      fetch(`${PRIZES_URL}/${STORY_NAME}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((res) => res.json())
        .then((data) => PrizesResponseSchema.parse(data)),
  });

  const sortedPrizes = prizes?.sort((a, b) => a.position - b.position);

  return { isLoading, isError, prizes: sortedPrizes };
};

export default useGetPrizes;
