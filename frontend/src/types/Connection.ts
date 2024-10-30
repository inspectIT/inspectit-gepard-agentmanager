import { z } from "zod";

export const ConnectionSchema = z.object({
  id: z.string().uuid(),
  registrationTime: z.string().datetime(),
  serviceName: z.string(),
  gepardVersion: z.string(),
  otelVersion: z.string(),
  pid: z.number().int(),
  startTime: z.number().int(),
  javaVersion: z.string(),
  attributes: z.record(z.string()),
});

export type Connection = z.infer<typeof ConnectionSchema>;
