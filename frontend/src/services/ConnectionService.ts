import { z } from "zod";
import { kyInstance } from "./ky.setup";
import {
  Connection,
  ConnectionQuery,
  ConnectionSchema,
  ServerConnection,
} from "@/types/Connection";

const ROUTES = {
  FIND_ALL: "connections",
  QUERY: "connections/query",
};

export const ConnectionService = {
  findAll: async (): Promise<Connection[]> => {
    const data = await kyInstance
      .get(ROUTES.FIND_ALL)
      .json<ServerConnection[]>();
    const transformedConnections =
      ConnectionService.transformConnectionsResponse(data);
    return z.array(ConnectionSchema).parse(transformedConnections);
  },
  query: async (query: ConnectionQuery): Promise<Connection[]> => {
    const data = await kyInstance
      .post(ROUTES.QUERY, {
        json: query,
      })
      .json<ServerConnection[]>();
    const transformedConnections =
      ConnectionService.transformConnectionsResponse(data);
    return z.array(ConnectionSchema).parse(transformedConnections);
  },
  transformConnectionsResponse(apiResponse: ServerConnection[]) {
    return apiResponse.map((item) => {
      const transformedAttributes = Object.entries(item.attributes).map(
        ([key, value]) => ({
          key,
          value,
        })
      );

      return {
        connectionId: item.connectionId,
        registrationTime: item.registrationTime,
        connectionStatus: item.connectionStatus,
        serviceName: item.serviceName,
        gepardVersion: item.gepardVersion,
        otelVersion: item.otelVersion,
        vmId: item.vmId,
        startTime: item.startTime,
        javaVersion: item.javaVersion,
        attributes: transformedAttributes,
      };
    });
  },
};
