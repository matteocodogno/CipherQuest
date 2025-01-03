import { SCOREBOARD_URL } from '../constants';
import { ScoreResponseSchema } from './schema';
import { useQuery } from '@tanstack/react-query';

const useGetScore = (userId?: string) =>
  useQuery({
    queryKey: [`scoreboard-${userId}`],
    queryFn: () =>
      fetch(`${SCOREBOARD_URL}/${userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((res) => res.json())
        .then((data) => ScoreResponseSchema.parse(data)),
    enabled: !!userId,
  });

export default useGetScore;
