import { createWrapper } from "../../../utils";
import { renderHook, waitFor } from "@testing-library/react";
import { useConnectionsQuery } from "@/hooks/features/connections/useConnections";
import { server } from "../../../vitest.setup";
import { http, HttpResponse } from "msw";

describe("group", () => {
  it("should return data, if network call was successful", async () => {
    const { result } = renderHook(() => useConnectionsQuery(), {
      wrapper: createWrapper(),
    });

    await waitFor(() => {
      expect(result.current.isSuccess).toBe(true);
    });

    expect(result.current.data).toBeDefined();
  });

  it("should return error, if network call was unsuccessful", async () => {
    server.use(
      http.get("http://localhost:8080/api/v1/connections", () => {
        return HttpResponse.json(
          { error: "Internal Server Error" },
          { status: 500 }
        );
      })
    );

    const { result } = renderHook(() => useConnectionsQuery(), {
      wrapper: createWrapper(),
    });

    await waitFor(() => {
      expect(result.current.isError).toBe(true);
    });
  });
});
