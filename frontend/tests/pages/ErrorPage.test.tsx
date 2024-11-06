import { render } from "@testing-library/react";
import { afterEach, describe, expect, it, vi } from "vitest";
import { isRouteErrorResponse, useRouteError } from "react-router-dom";
import ErrorPage from "@/pages/ErrorPage";

vi.mock("react-router-dom");

// Test error state
describe("ErrorPage", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  it("renders error.message on normal error", () => {
    vi.mocked(useRouteError).mockReturnValue(new Error("Mock Error"));
    vi.mocked(isRouteErrorResponse).mockReturnValue(false);
    const result = render(<ErrorPage />);
    expect(result.getByText(/Mock Error/i));
  });

  it("renders error.statusText on errorresponse", () => {
    const routeError = {
      status: 500,
      statusText: "Internal Server Error",
      data: "Server error occurred",
      error: new Error("Something went wrong"),
      internal: true,
    };

    vi.mocked(useRouteError).mockReturnValue(routeError);
    vi.mocked(isRouteErrorResponse).mockReturnValue(true);

    const result = render(<ErrorPage />);

    expect(result.getByText(/Internal Server Error/i));
  });

  it("renders fallback message on unknown error type", () => {
    vi.mocked(useRouteError).mockReturnValue({ foo: "bar" });
    vi.mocked(isRouteErrorResponse).mockReturnValue(false);

    const result = render(<ErrorPage />);

    expect(result.getByText(/error of unknown type/i));
  });
});
