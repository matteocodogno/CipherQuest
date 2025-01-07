import { differenceInMilliseconds, format } from 'date-fns';
import { useEffect, useState } from 'react';
import CardInfo from './card-info';
import { CardInfoVariant } from './constants';
import useIsMobile from '@/hooks/use-is-mobile';

interface CardTimeProps {
  time?: Date;
}

const calculateTime = ({
  time,
  isMobile,
}: {
  time?: Date;
  isMobile: boolean;
}): string => {
  const offset = new Date().getTimezoneOffset();
  const currentDate = new Date();
  const startingDate = time ? new Date(time) : new Date();
  const differenceDate = new Date(
    differenceInMilliseconds(currentDate, startingDate) + offset * 1000 * 60,
  );

  return format(differenceDate, isMobile ? 'mm:ss' : 'HH:mm:ss');
};

const CardTime = ({ time }: CardTimeProps) => {
  const isMobile = useIsMobile();
  const [formattedTime, setFormattedTime] = useState<string>();
  useEffect(() => {
    const interval = setInterval(() => {
      setFormattedTime(calculateTime({ time, isMobile }));
    }, 1000);

    return () => clearInterval(interval);
  }, [isMobile, setFormattedTime, time]);

  const defaultTimeValue = isMobile ? '--:--' : '--:--:--';

  return (
    <CardInfo
      value={formattedTime ?? defaultTimeValue}
      svg={'/assets/time.svg'}
      variant={CardInfoVariant.TIME}
    />
  );
};

export default CardTime;
