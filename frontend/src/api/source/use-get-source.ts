import { SOURCE_URL } from '../constants';
import { useMutation } from '@tanstack/react-query';
import { z } from 'zod';

interface GetSourceProps {
  sourceId: string;
}

const useGetSource = () =>
  useMutation({
    mutationFn: ({ sourceId }: GetSourceProps) =>
      fetch(`${SOURCE_URL}/${sourceId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((res) => res.text())
        .then((data) => z.string().safeParse(data)),
  });

export default useGetSource;
