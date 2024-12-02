import { ReactElement } from "react";
import { useQuery } from "@tanstack/react-query";
import { ScoreboardHeader } from "@/components/scoreboard/scoreboard-header.tsx";
import { CardContent, Grid2, Stack } from "@mui/material";
import Card from "@mui/material/Card";


type Score = {
  index: number;
  username: string;
  userId: number;
  score: number;
  time: number;
}[]

const convertSecondsToHHMMSS = (seconds: number) => {
  const hours = Math.floor(seconds / 3600);
  seconds %= 3600;
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;

  return String(hours).padStart(2, "0") + ":" +
    String(mins).padStart(2, "0") + ":" +
    String(secs).padStart(2, "0");

};

export const ScoreboardView = (): ReactElement => {

  const { isPending, isSuccess, data } = useQuery({
    queryKey: ["scoreboard"],
    queryFn: (): Promise<Score> =>
      fetch("/api/score", {
        method: "GET",
        headers: {
          "Content-Type": "application/json"
        }
      }).then((res) =>
        res.json()
      ),
    refetchInterval: 5000
  });


  return (
    <Stack
      sx={{
        width: "70%",
        minWidth: "600px",
        paddingX: 7,
        height: "calc(100vh - 62px)"
      }}
      flexDirection="column"
      alignItems={"center"}
      gap={4}
    >
      <ScoreboardHeader />
      {isPending && "Loading..."}
      {isSuccess && data.length > 0 && (
        <Stack gap={3} width={1}>
          <Card
            sx={{
              flexGrow: 1,
              background: "var(--mui-palette-background-paper)",
              borderRadius: "20px",
              display: "flex",
              flexDirection: "column",
              py: 0,
              px: 1,
              alignItems: "start",
              flexShrink: 0,
              alignSelf: "stretch"
            }}
          >
            <CardContent sx={{ p: 0 }}>
              <Grid2 container spacing={2} width={1}>
                <Grid2 size={2}>Position</Grid2>
                <Grid2 size={6}>Player</Grid2>
                <Grid2 size={2}>Score</Grid2>
                <Grid2 size={2}>Time</Grid2>
                {data?.slice(0, 3).map(player => {
                  return (<>
                    <Grid2 size={2}>{player.index + 1}</Grid2>
                    <Grid2 size={6}>{player.username}</Grid2>
                    <Grid2 size={2}>{player.score}</Grid2>
                    <Grid2 size={2}>{convertSecondsToHHMMSS(player.time)}</Grid2></>);
                })}
              </Grid2>
            </CardContent>
          </Card>

          {data.length > 3 &&
            <Card
              sx={{
                background: "var(--mui-palette-background-paper)",
                borderRadius: "20px",
                display: "flex",
                flexDirection: "column",
                py: 0,
                px: 1,
                alignItems: "start",
                flexShrink: 0,
                alignSelf: "stretch"
              }}
            >
              <Grid2 container spacing={2} width={1}>
                {data?.slice(3, data?.length).map(player => {
                  return (<>
                    <Grid2 size={2}>{player.index + 1}</Grid2>
                    <Grid2 size={6}>{player.username}</Grid2>
                    <Grid2 size={2}>{player.score}</Grid2>
                    <Grid2 size={2}>{convertSecondsToHHMMSS(player.time)}</Grid2></>);
                })}
              </Grid2>
            </Card>
          }
        </Stack>
      )}
    </Stack>
  );
};
