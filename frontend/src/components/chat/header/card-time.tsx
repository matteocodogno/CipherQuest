import { useEffect, useState } from 'react';
import CardInfo from './card-info';
import { CardInfoVariant } from './constants';
import { differenceInMilliseconds } from 'date-fns';

interface CardTimeProps {
  time?: Date;
}

const calculateTime = (time?: Date): string => {
  const currentDate = new Date();
  const startingDate = time ? new Date(time) : new Date();
  const differenceDate = new Date(
    differenceInMilliseconds(currentDate, startingDate),
  );

  const formattedDate = new Date(differenceDate).toTimeString();

  return formattedDate.split(' ')[0];
};

const CardTime = ({ time }: CardTimeProps) => {
  const [formattedTime, setFormattedTime] = useState<string>();
  useEffect(() => {
    const interval = setInterval(() => {
      setFormattedTime(calculateTime(time));
    }, 1000);

    return () => clearInterval(interval);
  }, [setFormattedTime, time]);

  return (
    <CardInfo
      value={formattedTime ?? '00:00'}
      svg={'/assets/time.svg'}
      variant={CardInfoVariant.TIME}
    />
  );
};

export default CardTime;
