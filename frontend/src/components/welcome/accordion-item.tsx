import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Typography,
} from '@mui/material';
import { useCallback, useRef } from 'react';
import { AccordionMenu } from './constants';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

const AccordionItem = ({ item }: { item: (typeof AccordionMenu)[number] }) => {
  const ref = useRef<HTMLDivElement>(null);

  const handleOnchange = useCallback((expanded: boolean) => {
    if (!ref.current) {
      return;
    }

    if (expanded) {
      const { y, height } = ref.current.getBoundingClientRect();
      //we need to wait for AccordionDetails to have a height (initially it's 0)
      setTimeout(
        () => window.scrollTo({ top: y + height, behavior: 'smooth' }),
        100,
      );
    }
  }, []);

  return (
    <Accordion
      sx={{
        backgroundColor: 'transparent',
        boxShadow: 'none',
      }}
      onChange={(_event, expanded) => handleOnchange(expanded)}
    >
      <AccordionSummary
        expandIcon={<ExpandMoreIcon color='primary' />}
        aria-controls='panel1-content'
        id='panel1-header'
      >
        {item.title}
      </AccordionSummary>
      <AccordionDetails>
        <div ref={ref}>
          <Typography variant='body1' color='text.secondary'>
            {item.content}
          </Typography>
        </div>
      </AccordionDetails>
    </Accordion>
  );
};

export default AccordionItem;
