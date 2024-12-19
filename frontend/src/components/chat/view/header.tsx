import { Button, Divider } from '@mui/material';
import { ReactElement, useState } from 'react';
import Box from '@mui/material/Box';
import { DynamicLogo } from '@/components/core/logo.tsx';
import LogoutDialog from '../dialog/logout-dialog';
import RulesDialog from '../dialog/rules-dialog';
import { Stack } from '@mui/system';
import { useUser } from '@/hooks/use-user';

export const Header = (): ReactElement => {
  const { user } = useUser();
  const [showLogout, setShowLogout] = useState<boolean>(false);
  const [showRules, setShowRules] = useState<boolean>(false);

  return (
    <Box
      style={{
        display: 'flex',
        alignItems: 'center',
        alignSelf: 'stretch',
      }}
      px={3}
      py={1}
    >
      <DynamicLogo width={164} height={46} />
      {user?.startedAt && (
        <Stack
          flex={1}
          direction={'row'}
          justifyContent='end'
          alignItems='end'
          gap={1}
        >
          <Button
            variant='text'
            endIcon={<Box component='img' src={'/assets/mission-rules.svg'} />}
            onClick={() => {
              setShowRules(true);
            }}
          >
            Mission rules
          </Button>
          <Divider orientation='vertical' variant='middle' flexItem />
          <Button
            variant='text'
            endIcon={<Box component='img' src={'/assets/logout.svg'} />}
            sx={{ color: 'var(--mui-palette-error-dark)' }}
            onClick={() => {
              setShowLogout(true);
            }}
          >
            End mission
          </Button>
        </Stack>
      )}
      <LogoutDialog
        showDialog={showLogout}
        handleClose={() => {
          setShowLogout(false);
        }}
      />{' '}
      <RulesDialog
        showDialog={showRules}
        handleClose={() => {
          setShowRules(false);
        }}
      />
    </Box>
  );
};
