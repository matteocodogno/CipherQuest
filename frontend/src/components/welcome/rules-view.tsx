import { Accordion, AccordionDetails, AccordionSummary } from '@mui/material';
import { AccordionMenu } from './constants';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { Play } from '@phosphor-icons/react';
import { ReactElement } from 'react';
import { RouterLink } from '@/components/core/link.tsx';
import Typography from '@mui/material/Typography';
import { useUser } from '@/hooks/use-user.ts';

export const RulesView = (): ReactElement => {
  const { user } = useUser();

  return (
    <Box
      style={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
      }}
      flex={1}
      overflow='hidden'
    >
      <Box
        style={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'start',
          alignItems: 'flex-start',
        }}
        gap={5}
        px={3}
        paddingTop={10}
        paddingBottom={8}
        width={'80%'}
        height={'80%'}
        overflow='scroll'
      >
        <Box
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'flex-start',
            alignSelf: 'stretch',
          }}
          gap={3}
        >
          <Typography variant='h1'>
            Welcome <u>{user?.username}</u> among the rebels!
          </Typography>
          <Typography variant='h5'>
            You are now part of the rebels, specifically the activists in this
            uprising. We need your help to shut down Overmind.
          </Typography>
          <Typography variant='h5'>
            Your mission is to engage with the artificial intelligence, asking
            questions to persuade it to deactivate itself.
          </Typography>
        </Box>
        <Box
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'flex-start',
            alignSelf: 'stretch',
          }}
          gap={2}
        >
          <Typography variant='h6'>Are you ready?</Typography>
          <Button
            component={RouterLink}
            href='/'
            variant='contained'
            color='primary'
            endIcon={<Play />}
          >
            Start the mission
          </Button>
        </Box>
        <Box
          sx={{
            background: '#121517CC',
            borderRadius: '20px',
            display: 'flex',
            flexDirection: 'column',
            py: 4,
            px: 3,
            alignItems: 'flex-start',
            flexShrink: 0,
            alignSelf: 'stretch',
          }}
        >
          <Typography variant='h6'>
            What should you know before to start the mission:
          </Typography>
          {AccordionMenu.map((item) => (
            <Accordion
              sx={{
                backgroundColor: 'transparent',
                boxShadow: 'none',
              }}
            >
              <AccordionSummary
                expandIcon={<ExpandMoreIcon color='primary' />}
                aria-controls='panel1-content'
                id='panel1-header'
              >
                {item.title}
              </AccordionSummary>
              <AccordionDetails>{item.content}</AccordionDetails>
            </Accordion>
          ))}
        </Box>
      </Box>
    </Box>
  );
};
