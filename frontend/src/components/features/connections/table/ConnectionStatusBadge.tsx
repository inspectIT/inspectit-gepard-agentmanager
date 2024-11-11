import { Badge } from "@/components/ui/shadcn/badge";
import { ConnectionStatus } from "@/types/Connection";

interface ConnectionStatusBadgeProps {
  status: ConnectionStatus;
}

export default function ConnectionStatusBadge({
  status = "DISCONNECTED",
}: Readonly<ConnectionStatusBadgeProps>) {
  let variant: "default" | "secondary" | "destructive";
  let additionalClasses = "";

  switch (status) {
    case "CONNECTED":
      variant = "default";
      additionalClasses = "bg-green-500 hover:bg-green-600 cursor-default";
      break;
    case "DISCONNECTED":
      variant = "secondary";
      additionalClasses =
        "text-white bg-gray-500 hover:bg-gray-600 cursor-default";
      break;
    case "LOST_CONNECTION":
      variant = "destructive";
      break;
  }

  return (
    <Badge
      variant={variant}
      className={`${additionalClasses} font-semibold`}
      aria-label={`Connection status: ${status}`}
    >
      {status}
    </Badge>
  );
}
