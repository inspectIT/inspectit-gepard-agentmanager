import { z } from "zod";
import { kyInstance } from "./ky.setup";
import { ConnectionSchema } from "@/types/Connection";

const ROUTES = {
  FIND_ALL: "connections",
};

export const ConnectionService = {
  findAll: async () => {
    const data = await kyInstance.get(ROUTES.FIND_ALL).json();
    return z.array(ConnectionSchema).parse(data);
  },
};
