import { ReactElement, ReactNode } from "react";
import Box from "@mui/material/Box";
import { usePathname } from "@/hooks/use-pathname.ts";
import { Header } from "@/components/chat/view/header.tsx";

type LayoutProps = {
  children: ReactNode;
};


const backgroundMap: Record<string, string> = {
  "/scoreboard": "/assets/rules-background.jpeg"
};

export function ScoreboardLayout({ children }: LayoutProps): ReactElement {
  const pathname = usePathname();

  const background = backgroundMap[pathname] ?? "/assets/background.jpeg";

  return (
    <Box
      style={{
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        backgroundImage: `linear-gradient(0deg, rgba(9, 10, 11, 0.9) 0%, rgba(9, 10, 11, 0.9) 100%), url("${background}")`,
        backgroundRepeat: "no-repeat",
        backgroundPosition: "center",
        backgroundSize: "cover"
      }}
    >
      <Header />
      {children}
    </Box>
  );
}
