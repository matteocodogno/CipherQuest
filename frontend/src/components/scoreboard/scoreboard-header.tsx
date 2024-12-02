import Card from "@mui/material/Card";
import { CardContent, CardHeader } from "@mui/material";
import Typography from "@mui/material/Typography";

export const ScoreboardHeader = () => {
  return (
    <Card
      sx={{
        background: "var(--mui-palette-background-paper)",
        borderRadius: "20px",
        display: "flex",
        flexDirection: "column",
        px: 3,
        py: 1,
        alignItems: "start",
        flexShrink: 0,
        alignSelf: "stretch"
      }}
    >
      <CardHeader sx={{ p: 0 }} title={"Rewards"} />
      <CardContent sx={{ px: 0, py: 1 }}>
        <Typography>Lorem ipsum</Typography>
      </CardContent>

    </Card>
  );
};

