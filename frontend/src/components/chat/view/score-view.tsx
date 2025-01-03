import {
  Breadcrumbs,
  Card,
  CardContent,
  CardHeader,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
} from '@mui/material';
import { ReactElement, useMemo } from 'react';
import { styled, useTheme } from '@mui/material/styles';
import { ArrowLeft } from '@phosphor-icons/react';
import Box from '@mui/material/Box';
import { Match } from 'effect';
import PageHeader from '@/components/core/Headings/page-header.tsx';
import { RouterLink } from '@/components/core/link.tsx';
import Typography from '@mui/material/Typography';
import useGetScoreboard from '@/api/score/use-get-scoreboard.ts';
import { useUser } from '@/hooks/use-user';

const ScoreBreadcrumb = () => {
  const theme = useTheme();
  const { user } = useUser();

  const { path: path, title } =
    user === null
      ? { path: '/auth/custom/sign-in', title: 'back to login' }
      : { path: '/chat', title: 'back to chat' };

  return (
    <Breadcrumbs>
      <RouterLink
        href={path}
        style={{
          display: 'flex',
          gap: 8,
          alignItems: 'center',
          textDecoration: 'none',
          color: theme.palette.neutral['400'],
        }}
      >
        <ArrowLeft />
        <Typography variant='body1'>{title}</Typography>
      </RouterLink>
    </Breadcrumbs>
  );
};

const TableCellHeader = styled(TableCell)(() => ({
  '&.MuiTableCell-root': {
    backgroundColor: 'rgba(255, 255, 255, 0.00)',
  },
}));

const GradientTableRow = styled(TableRow)(({ index }: { index: number }) => ({
  '&.MuiTableRow-root': {
    background: Match.value(index).pipe(
      Match.when(0, () => 'var(--mui-palette-background-level2)'),
      Match.when(1, () => 'var(--mui-palette-background-level1)'),
      Match.when(2, () => 'var(--mui-palette-background-level3)'),
      Match.orElse(() => 'red'),
    ),
  },
}));

export const ScoreView = (): ReactElement => {
  const { isError, isLoading, data, error } = useGetScoreboard();

  const firstThree = useMemo(() => data?.slice(0, 3), [data]);
  const fourToEight = useMemo(() => data?.slice(3, 8), [data]);

  if (isError) {
    console.error('Error loading the scoreboard', error);
    return (
      <Box
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          gap: 16,
        }}
        flex={1}
        paddingY={64}
      >
        <Typography variant='h4'>Error loading the scoreboard</Typography>
        <Typography variant='body1'>{error.message}</Typography>
      </Box>
    );
  }

  if (isLoading) {
    return (
      <Box
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          gap: 16,
        }}
        flex={1}
        paddingY={64}
      >
        <Typography variant='h4'>Loading...</Typography>
      </Box>
    );
  }

  return (
    <Box
      style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        gap: 48,
        width: '100%',
      }}
    >
      <PageHeader title={'Scoreboard'} breadcrumb={<ScoreBreadcrumb />} />
      <Card
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-start',
          alignSelf: 'stretch',
        }}
      >
        <CardHeader title='Rewards 🤑' />
        <CardContent>
          <Typography variant='body1'>
            🥇 Prize for the first place:{' '}
            <strong>LEGO Star Wars TIE Interceptor 🧱</strong>
            <br />
            🥈 Prize for the second place:{' '}
            <strong>WellD Stormtech backpack 🎒</strong>
            <br />
            🥉 Prize for the third place:{' '}
            <strong>WellD Carhart backpack 🎒</strong>
            <br />
          </Typography>
        </CardContent>
      </Card>
      <Box
        style={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-start',
          alignSelf: 'stretch',
          gap: 32,
        }}
      >
        <Card
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'flex-start',
            alignSelf: 'stretch',
            background: 'var(--background-paper-glass)',
          }}
        >
          <Table>
            <TableHead>
              <TableRow>
                <TableCellHeader>Position</TableCellHeader>
                <TableCellHeader>Player</TableCellHeader>
                <TableCellHeader align='right'>Score</TableCellHeader>
                <TableCellHeader align='right'>Time</TableCellHeader>
              </TableRow>
            </TableHead>
            <TableBody>
              {firstThree?.map((row) => (
                <GradientTableRow key={row.index} index={row.index}>
                  <TableCell>{row.index + 1}</TableCell>
                  <TableCell>{row.username}</TableCell>
                  <TableCell align='right'>{row.score}</TableCell>
                  <TableCell align='right'>{row.time}</TableCell>
                </GradientTableRow>
              ))}
            </TableBody>
          </Table>
        </Card>
        <Card
          style={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'flex-start',
            alignSelf: 'stretch',
            background: 'var(--background-paper-glass)',
          }}
        >
          <Table>
            <TableBody>
              {fourToEight?.map((row) => (
                <TableRow key={row.index}>
                  <TableCell>{row.index + 1}</TableCell>
                  <TableCell>{row.username}</TableCell>
                  <TableCell align='right'>{row.score}</TableCell>
                  <TableCell align='right'>{row.time}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Card>
      </Box>
    </Box>
  );
};
