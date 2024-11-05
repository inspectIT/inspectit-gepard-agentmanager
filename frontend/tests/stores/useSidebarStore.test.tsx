import { renderHook, act } from "@testing-library/react";
import { useSidebarStore } from "../../src/stores/useSidebarStore";

describe("useSidebarStore", () => {
  it("should initialize with isOpen set to true", () => {
    const { result } = renderHook(() => useSidebarStore());
    expect(result.current.isOpen).toBe(true);
  });

  it("should toggle isOpen state", () => {
    const { result } = renderHook(() => useSidebarStore());

    act(() => {
      result.current.toggle();
    });
    expect(result.current.isOpen).toBe(false);

    act(() => {
      result.current.toggle();
    });
    expect(result.current.isOpen).toBe(true);
  });
});
