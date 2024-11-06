// SortableTableColumn.test.tsx
import SortableTableColumn from "@/components/ui/sortable-table-column";
import { Column } from "@tanstack/react-table";
import { render, screen, fireEvent, cleanup } from "@testing-library/react";
import { beforeEach, describe, expect, test, vi } from "vitest";

// Mock ArrowUpDown component
vi.mock("lucide-react", () => ({
  ArrowUpDown: () => <span>ArrowIcon</span>,
}));

describe("SortableTableColumn Component", () => {
  beforeEach(() => {
    cleanup();
  });

  const mockColumn = {
    getIsSorted: vi.fn(),
    toggleSorting: vi.fn(),
  };

  test("renders with the correct title", () => {
    render(
      <SortableTableColumn
        column={mockColumn as unknown as Column<number>}
        title="Test Title"
      />
    );
    expect(screen.getByText("Test Title"));
    expect(screen.getByText("ArrowIcon"));
  });

  test("calls toggleSorting on click", () => {
    mockColumn.getIsSorted.mockReturnValue("asc");
    render(
      <SortableTableColumn
        column={mockColumn as unknown as Column<number>}
        title="Test Title"
      />
    );
    const buttonElement = screen.getByRole("button");

    fireEvent.click(buttonElement);
    expect(mockColumn.toggleSorting).toHaveBeenCalledWith(true);
  });

  test("calls toggleSorting with correct argument when not sorted", () => {
    mockColumn.getIsSorted.mockReturnValue(false);
    render(
      <SortableTableColumn
        column={mockColumn as unknown as Column<number>}
        title="Test Title"
      />
    );
    const buttonElement = screen.getByRole("button");

    fireEvent.click(buttonElement);
    expect(mockColumn.toggleSorting).toHaveBeenCalledWith(false);
  });
});
