import { ServerConnection } from "@/types/Connection";
import { http, HttpResponse } from "msw";
import { generateMockConnection } from "./mocks-data";

export const handlers = [
  http.get<never, never, ServerConnection[]>(
    "http://localhost:8080/api/v1/connections",
    () => {
      const connections = [
        generateMockConnection("service-a"),
        generateMockConnection("service-b"),
      ];
      return HttpResponse.json(connections);
    }
  ),
];
