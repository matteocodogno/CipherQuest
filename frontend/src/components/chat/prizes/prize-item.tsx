import { Typography } from '@mui/material';
import { getOrdinalNumber } from '@/utils/number';

interface PrizeItemProps {
  index: number;
  label: string;
}

const PrizeItem = ({ index, label }: PrizeItemProps) => {
  return (
    <Typography variant='body1'>
      Prize for the {getOrdinalNumber(index)} place: <strong>{label}</strong>
    </Typography>
  );
};

export default PrizeItem;
