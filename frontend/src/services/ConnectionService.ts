import { Connection, ConnectionSchema } from "../types/Connection";
import { http } from "./kySetup";
import { z } from "zod";

const ROUTES = {
  FIND_ALL: "/connections",
};

export const ConnectionService = {
  async getAllConnections() {
    const data = await http.get<Connection[]>(ROUTES.FIND_ALL).json();
    return z.array(ConnectionSchema).parse(data);
  },
};
