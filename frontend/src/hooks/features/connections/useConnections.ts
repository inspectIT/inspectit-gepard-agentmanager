import { ConnectionService } from "@/services/ConnectionService";
import { useQuery } from "@tanstack/react-query";

export const useConnectionsQuery = () => {
  return useQuery({
    queryKey: ["connections"],
    queryFn: ConnectionService.findAll,
  });
};
