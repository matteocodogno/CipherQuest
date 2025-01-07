import Box from '@mui/material/Box';
import { ReactElement } from 'react';
import Rules from '../rules.tsx';
import RulesButton from '../rules-button.tsx';
import StickyBox from '@/components/core/sticky-box.tsx';
import Typography from '@mui/material/Typography';
import useIsMobile from '@/hooks/use-is-mobile.ts';
import { useUser } from '@/hooks/use-user.ts';

export const RulesView = (): ReactElement => {
  const { user } = useUser();
  const isMobile = useIsMobile();

  return (
    <Box>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
        }}
        flex={1}
        paddingBottom={'64px'}
      >
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'start',
            alignItems: 'flex-start',
          }}
          gap={5}
          px={3}
          width={isMobile ? 'auto' : '90%'}
          height={isMobile ? 'auto' : '90%'}
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
            <Typography variant={isMobile ? 'h3' : 'h1'}>
              Here you are, <u>{user?.username}</u>!
            </Typography>
            <Typography variant={isMobile ? 'body1' : 'h5'}>
              You&lsquo;re now part of the rebellion, you&lsquo;re an activist
              in this uprising.
              <br />
              We need your help to bring down Overmind!
            </Typography>
            <Typography variant={isMobile ? 'body1' : 'h5'}>
              After years of searching, we&lsquo;ve finally tracked down an
              Overmind terminal with lower defenses.
              <br />
              Your mission is to access it and uncover a way to shut down this
              oppressor using the information at your disposal.
            </Typography>
          </Box>

          {!isMobile && <RulesButton />}

          <Box
            sx={{
              background: 'var(--mui-palette-background-paper)',
              opacity: 0.9,
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
              What should you know before starting the mission:
            </Typography>
            <Rules />
          </Box>
        </Box>
      </Box>
      {isMobile && (
        <StickyBox>
          <RulesButton />
        </StickyBox>
      )}
    </Box>
  );
};
