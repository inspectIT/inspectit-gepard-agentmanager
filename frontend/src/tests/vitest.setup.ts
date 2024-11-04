import { setupServer } from "msw/node";
import { afterAll, afterEach, beforeAll, vi } from "vitest";
import { handlers } from "./handlers";

export const server = setupServer(...handlers);

beforeAll(() => {
  server.listen();
});

afterEach(() => {
  server.resetHandlers();
});

afterAll(() => {
  server.close();
});
