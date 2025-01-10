import AccordionItem from '../core/accordion-item';
import { AccordionMenu } from './constants';
import { Box } from '@mui/material';

const Rules = () => {
  return (
    <Box>
      {AccordionMenu.map((item) => (
        <AccordionItem key={item.title} item={item} />
      ))}
    </Box>
  );
};

export default Rules;
