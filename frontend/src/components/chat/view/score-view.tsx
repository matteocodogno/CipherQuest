import {
  Breadcrumbs,
  Card,
  CardContent,
  CardHeader,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  SelectChangeEvent,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
} from '@mui/material';
import { ReactElement, useCallback, useEffect, useMemo, useState } from 'react';
import { styled, useTheme } from '@mui/material/styles';
import { ArrowLeft } from '@phosphor-icons/react';
import Box from '@mui/material/Box';
import { Match } from 'effect';
import PageHeader from '@/components/core/Headings/page-header.tsx';
import { RouterLink } from '@/components/core/link.tsx';
import { ScoreboardPeriod } from '../constants';
import Typography from '@mui/material/Typography';
import useGetPrizes from '@/api/score/use-get-prizes';
import useGetScoreboard from '@/api/score/use-get-scoreboard.ts';
import useIsMobile from '@/hooks/use-is-mobile';
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
  const [currentPeriod, setCurrentPeriod] = useState<ScoreboardPeriod>(
    ScoreboardPeriod.TODAY,
  );
  const {
    isError,
    isLoading: isLoadingScoreboard,
    data,
    error,
    refetch,
  } = useGetScoreboard(
    new URLSearchParams({
      timeFrameFilter: currentPeriod,
    }),
  );

  const { isLoading: isLoadingPrizes, prizes } = useGetPrizes();

  const isMobile = useIsMobile();

  const firstThree = useMemo(() => data?.slice(0, 3), [data]);
  const fourToEight = useMemo(() => data?.slice(3, 8), [data]);

  const handleChange = useCallback((event: SelectChangeEvent) => {
    setCurrentPeriod(event.target.value as ScoreboardPeriod);
  }, []);

  useEffect(() => {
    refetch();
  }, [currentPeriod, refetch]);

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

  if (isLoadingScoreboard || isLoadingPrizes) {
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
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
        gap: 6,
        width: '100%',
        px: isMobile ? 2 : 0,
        paddingBottom: 8,
      }}
    >
      <PageHeader title={'Scoreboard'} breadcrumb={<ScoreBreadcrumb />} />
      {prizes && prizes.length > 0 && (
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
              🥇 Prize for the first place: <strong>{prizes[0].name}</strong>
              <br />
              {prizes.length > 1 ? (
                <>
                  🥈 Prize for the second place:{' '}
                  <strong>{prizes[1].name}</strong>
                  <br />{' '}
                </>
              ) : null}
              {prizes.length > 2 ? (
                <>
                  🥉 Prize for the third place:{' '}
                  <strong>{prizes[2].name}</strong>
                  <br />{' '}
                </>
              ) : null}
            </Typography>
          </CardContent>
        </Card>
      )}
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
          <Stack direction={'column'} flex={1} alignSelf={'stretch'}>
            <Box sx={{ py: 2, px: 3 }}>
              <FormControl sx={{ minWidth: 200 }}>
                <InputLabel id='period-filter'>Filter by period</InputLabel>
                <Select
                  labelId='period-filter'
                  id='period-filter-select'
                  value={currentPeriod}
                  onChange={handleChange}
                >
                  <MenuItem value={ScoreboardPeriod.ALL}>All</MenuItem>
                  <MenuItem value={ScoreboardPeriod.TODAY}>Today</MenuItem>
                  <MenuItem value={ScoreboardPeriod.LAST_WEEK}>
                    Last week
                  </MenuItem>
                  <MenuItem value={ScoreboardPeriod.LAST_MONTH}>
                    Last month
                  </MenuItem>
                  <MenuItem value={ScoreboardPeriod.LAST_YEAR}>
                    Last year
                  </MenuItem>
                </Select>
              </FormControl>
            </Box>
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
                    <TableCell width={'30%'}>{row.index + 1}</TableCell>
                    <TableCell width={'100%'}>{row.username}</TableCell>
                    <TableCell align='right'>{row.score}</TableCell>
                    <TableCell align='right'>{row.time}'</TableCell>
                  </GradientTableRow>
                ))}
              </TableBody>
            </Table>
          </Stack>
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
                  <TableCell width={'30%'}>{row.index + 1}</TableCell>
                  <TableCell width={'100%'}>{row.username}</TableCell>
                  <TableCell align='right'>{row.score}</TableCell>
                  <TableCell align='right'>{row.time}'</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Card>
      </Box>
    </Box>
  );
};
