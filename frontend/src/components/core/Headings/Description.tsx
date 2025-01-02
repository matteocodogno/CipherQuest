import { PropsWithChildren } from 'react';
import Typography from '@mui/material/Typography';

const Description = ({ children }: PropsWithChildren) => (
  <Typography variant='body2'>{children}</Typography>
);

export default Description;
