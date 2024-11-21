import { z } from "zod";
import { kyInstance } from "./ky.setup";
import {
  Connection,
  ConnectionSchema,
  ServerConnection,
} from "@/types/Connection";
import { transformConnectionsResponse } from "@/lib/data-transformation";

const ROUTES = {
  FIND_ALL: "connections",
  QUERY: "connections/query",
};

export const ConnectionService = {
  findAll: async (): Promise<Connection[]> => {
    const data = await kyInstance
      .get(ROUTES.FIND_ALL)
      .json<ServerConnection[]>();
    const transformedConnections = transformConnectionsResponse(data);
    return z.array(ConnectionSchema).parse(transformedConnections);
  },
};
