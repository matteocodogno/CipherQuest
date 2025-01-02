import { SCOREBOARD_URL } from '@/api/constants.ts';
import { ScoreResponseSchema } from '@/api/score/schema.ts';
import { useQuery } from '@tanstack/react-query';

const useGetScoreboard = () => useQuery({
  queryKey: ['scoreboard'],
  queryFn: () =>
    fetch(SCOREBOARD_URL, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((res) => res.json())
      .then((data) => ScoreResponseSchema.parse(data)),
});

export default useGetScoreboard;
