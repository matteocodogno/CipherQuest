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
}: CardInfoProps) => (
  <Stack
    sx={{
      borderRadius: '20px',
      background: 'var(--mui-palette-background-level3)',
      flexDirection: 'row',
      alignItems: 'start',
      justifyContent: 'start',
      padding: 2,
    }}
    gap={1}
    flexDirection={'row'}
    minWidth={variant === CardInfoVariant.TIME ? '120px' : '60px'}
  >
    <Box component='img' src={svg} sx={{ height: '24px', width: 'auto' }} />
    <Box justifyContent='center' alignItems='center'>
      <Typography>{value}</Typography>
    </Box>
  </Stack>
);

export default CardInfo;
