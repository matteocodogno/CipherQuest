import { Box, Stack } from '@mui/system';
import { CardInfoVariant } from './constants';
import { Typography } from '@mui/material';

interface CardInfoProps {
  svg: string;
  value: string;
  variant?: CardInfoVariant;
}

const CardInfo = ({
  svg,
  value,
  variant = CardInfoVariant.INFO,
}: CardInfoProps) => {
  return (
    <Stack
      sx={{
        borderRadius: '20px',
        background: 'var(--mui-palette-background-level3)',
        flexDirection: 'row',
        alignItems: variant === CardInfoVariant.TIME ? 'start' : 'center',
        justifyContent: variant === CardInfoVariant.TIME ? 'start' : 'center',
        padding: 2,
      }}
      gap={1}
      flexDirection={'row'}
      width={variant === CardInfoVariant.TIME ? '120px' : 'auto'}
    >
      <Box component='img' src={svg} sx={{ height: '24px', width: 'auto' }} />
      <Box justifyContent='center' alignItems='center'>
        <Typography>{value}</Typography>
      </Box>
    </Stack>
  );
};

export default CardInfo;
