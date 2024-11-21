import { Attribute } from "@/types/Attribute";
import { Connection, ServerConnection } from "@/types/Connection";

export function transformConnectionsResponse(
  apiResponse: ServerConnection[]
): Connection[] {
  return apiResponse.map((item) => {
    const transformedAttributes = transformAttributes(item.attributes);
    const transformedLastFetch = transformDuration(item.timeSinceLastFetch);

    return {
      connectionId: item.connectionId,
      registrationTime: item.registrationTime,
      connectionStatus: item.connectionStatus,
      lastFetch: transformedLastFetch,
      serviceName: item.serviceName,
      gepardVersion: item.gepardVersion,
      otelVersion: item.otelVersion,
      vmId: item.vmId,
      startTime: item.startTime,
      javaVersion: item.javaVersion,
      attributes: transformedAttributes,
    };
  });
}

function transformDuration(durationString: string): string {
  const cleanedString = durationString.replace(/^(PT|S$)/g, "");

  const hours = RegExp(/(\d+)H/).exec(cleanedString)?.[1] ?? "0";
  const minutes = RegExp(/(\d+)M/).exec(cleanedString)?.[1] ?? "0";
  const seconds = RegExp(/(\d+\.\d+)/).exec(cleanedString)?.[1] ?? "0";

  const parts: string[] = [];

  if (hours !== "0") parts.push(`${hours}h`);
  if (minutes !== "0") parts.push(`${minutes}m`);

  const roundedSeconds = Math.round(parseFloat(seconds));
  if (roundedSeconds > 0) parts.push(`${roundedSeconds.toString()}s`);

  return parts.length > 0 ? `${parts.join(" ")} ago` : "just now";
}

function transformAttributes(attributes: Record<string, string>): Attribute[] {
  return Object.entries(attributes).map(([key, value]) => ({
    key,
    value,
  }));
}
