import { differenceInMilliseconds, format } from 'date-fns';
import { useEffect, useState } from 'react';
import CardInfo from './card-info';
import { CardInfoVariant } from './constants';

interface CardTimeProps {
  time?: Date;
}

const calculateTime = (time?: Date): string => {
  const offset = new Date().getTimezoneOffset();
  const currentDate = new Date();
  const startingDate = time ? new Date(time) : new Date();
  const differenceDate = new Date(
    differenceInMilliseconds(currentDate, startingDate) + offset * 1000 * 60,
  );

  return format(differenceDate, 'HH:mm:ss');
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
      value={formattedTime ?? '--:--:--'}
      svg={'/assets/time.svg'}
      variant={CardInfoVariant.TIME}
    />
  );
};

export default CardTime;
