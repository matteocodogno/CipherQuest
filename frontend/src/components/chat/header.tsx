import { BookOpenText, SignOut } from '@phosphor-icons/react';
import { Button, Divider } from '@mui/material';
import { ReactElement, useState } from 'react';
import Box from '@mui/material/Box';
import { DynamicLogo } from '@/components/core/logo.tsx';
import LogoutDialog from './dialog/logout-dialog.tsx';
import RulesDialog from './dialog/rules-dialog.tsx';
import { Stack } from '@mui/system';
import useIsMobile from '@/hooks/use-is-mobile.ts';
import { useLocation } from 'react-router-dom';

export const Header = (): ReactElement => {
  const isMobile = useIsMobile();
  const location = useLocation();
  const isChat = location.pathname === '/chat';

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
      <Box sx={{ alignContent: 'flex-start' }}>
        <DynamicLogo
          width={isMobile ? 46 : 164}
          height={46}
          showIconLogo={isMobile}
        />
      </Box>
      {isChat && (
        <Stack
          flex={1}
          direction={'row'}
          justifyContent='end'
          alignItems='end'
          gap={1}
        >
          <Button
            variant='text'
            endIcon={<BookOpenText />}
            onClick={() => {
              setShowRules(true);
            }}
          >
            {isMobile ? '' : 'Mission rules'}
          </Button>
          {!isMobile && (
            <Divider orientation='vertical' variant='middle' flexItem />
          )}
          <Button
            variant='text'
            endIcon={<SignOut />}
            sx={{ color: 'var(--mui-palette-error-dark)' }}
            onClick={() => {
              setShowLogout(true);
            }}
          >
            {isMobile ? '' : 'End mission'}
          </Button>
        </Stack>
      )}
      {showLogout && (
        <LogoutDialog
          handleClose={() => {
            setShowLogout(false);
          }}
        />
      )}
      {showRules && (
        <RulesDialog
          handleClose={() => {
            setShowRules(false);
          }}
        />
      )}
    </Box>
  );
};
