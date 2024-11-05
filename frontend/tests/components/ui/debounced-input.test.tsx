// DebouncedInput.test.jsx
import DebouncedInput from "@/components/ui/debounced-input";
import {
  render,
  screen,
  fireEvent,
  waitFor,
  cleanup,
} from "@testing-library/react";
import { beforeEach, describe, expect, test, vi } from "vitest";

describe("DebouncedInput Component", () => {
  beforeEach(() => {
    cleanup();
  });

  test("renders with initial value", () => {
    // Arrage
    render(<DebouncedInput value="initial" onChange={vi.fn()} />);
    const inputElement = screen.getByDisplayValue("initial");
    // Assert
    expect(inputElement).toBeDefined();
  });

  test("updates value when typing", () => {
    //Arrange
    render(<DebouncedInput value="initial" onChange={vi.fn()} />);
    const inputElement: HTMLInputElement = screen.getByDisplayValue("initial");
    // Act
    fireEvent.change(inputElement, { target: { value: "changed" } });
    // Assert
    expect(inputElement.value).toBe("changed");
  });

  test("calls onChange after debounce period", async () => {
    //Arrage
    //Empty Mock-Function
    const handleChange = vi.fn();
    render(
      <DebouncedInput value="initial" onChange={handleChange} debounce={500} />
    );
    const inputElement: HTMLInputElement = screen.getByDisplayValue("initial");

    //Act
    fireEvent.change(inputElement, { target: { value: "new value" } });

    //Assert
    // Function not called directly
    expect(handleChange).not.toBeCalledWith("new value");
    // But after 600ms. Default debounce is 500ms
    await waitFor(() => expect(handleChange).toBeCalledWith("new value"), {
      timeout: 600,
    });
  });

  test("resets value when initial value changes", () => {
    //Arrange
    const { rerender } = render(
      <DebouncedInput value="initial" onChange={vi.fn()} />
    );
    const inputElement: HTMLInputElement = screen.getByDisplayValue("initial");

    // Act
    fireEvent.change(inputElement, { target: { value: "changed" } });

    // Assert
    expect(inputElement.value).toBe("changed");
    // Change the initial value and rerender the component
    rerender(<DebouncedInput value="new initial" onChange={vi.fn()} />);

    expect(inputElement.value).toBe("new initial");
  });
});
