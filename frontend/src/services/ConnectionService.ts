import { z } from "zod";
import { kyInstance } from "./ky.setup";
import {
  Connection,
  ConnectionSchema,
  ServerConnection,
} from "@/types/Connection";

const ROUTES = {
  FIND_ALL: "connections",
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
  transformConnectionsResponse(apiResponse: ServerConnection[]) {
    return apiResponse.map((item) => {
      const transformedAttributes = Object.entries(item.attributes).map(
        ([key, value]) => ({
          key,
          value,
        })
      );

      return {
        id: item.id,
        registrationTime: item.registrationTime,
        serviceName: item.serviceName,
        gepardVersion: item.gepardVersion,
        otelVersion: item.otelVersion,
        pid: item.pid,
        startTime: item.startTime,
        javaVersion: item.javaVersion,
        attributes: transformedAttributes,
      };
    });
  },
};
