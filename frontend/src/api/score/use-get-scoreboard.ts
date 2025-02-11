import { SCOREBOARD_URL } from '@/api/constants.ts';
import { ScoreboardResponseSchema } from '@/api/score/schema.ts';
import { useQuery } from '@tanstack/react-query';

const useGetScoreboard = (params: URLSearchParams) =>
  useQuery({
    queryKey: ['scoreboard', params],
    queryFn: () =>
      fetch(SCOREBOARD_URL + `/?${params}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((res) => res.json())
        .then((data) => ScoreboardResponseSchema.parse(data)),
  });

export default useGetScoreboard;
