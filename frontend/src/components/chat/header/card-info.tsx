import { Box, Stack } from '@mui/system';
import { Typography } from '@mui/material';

interface CardInfoProps {
  svg: string;
  value: string;
}

const CardInfo = ({ svg, value }: CardInfoProps) => (
  <Stack
    sx={{
      borderRadius: '20px',
      background: 'var(--mui-palette-background-level3)',
      flexDirection: 'row',
      alignItems: 'center',
      justifyContent: 'center',
      padding: 2,
    }}
    gap={1}
    flexDirection={'row'}
  >
    <Box
      alt='Internal server error'
      component='img'
      src={svg}
      sx={{ height: '24px', width: 'auto' }}
    />
    <Typography>{value}</Typography>
  </Stack>
);

export default CardInfo;
