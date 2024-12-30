import { Accordion, AccordionDetails, AccordionSummary, Typography } from '@mui/material';
import { useCallback, useRef } from 'react';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

type AccordionItemProps = {
  title: string;
  content: string;
}

const AccordionItem = ({ item }: { item: AccordionItemProps }) => {
  const ref = useRef<HTMLDivElement>(null);

  const handleOnchange = useCallback((expanded: boolean) => {
    if (!ref.current) {
      return;
    }

    if (expanded) {
      //we need to wait for AccordionDetails to have the correct height (initially it's 0)
      setTimeout(
        () => ref.current?.scrollIntoView({ behavior: 'smooth' }),
        150,
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
        <Typography variant='body1' color='text.secondary'>
          <div dangerouslySetInnerHTML={{ __html: item.content }}></div>
        </Typography>
        <div ref={ref}></div>
      </AccordionDetails>
    </Accordion>
  );
};

export default AccordionItem;
