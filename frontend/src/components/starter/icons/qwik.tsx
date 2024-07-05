import Logo from "../../../media/th.jpeg";

export const QwikLogo = ({
  width = 35,
  height = 35,
}: {
  width?: number;
  height?: number;
}) => (
  <img src={Logo} width={width} height={height} style={{ borderRadius: "100%" }} />
);
