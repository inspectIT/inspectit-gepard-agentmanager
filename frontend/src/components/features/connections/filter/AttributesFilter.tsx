import { useReducer, useRef } from "react";
import { Attribute } from "@/types/Attribute";
import { Column } from "@tanstack/react-table";
import { Connection } from "@/types/Connection";
import { useAttributeFilter } from "@/hooks/features/connections/useAttributeFilter";
import AttributeFilterList from "./AttributeFilterList";
import AttributesFilterButton from "./AttributesFilterButton";
import AttributesFilterForm from "./AttributeFilterForm";

interface AttributesFilterProps {
  column: Column<Connection>;
}

interface FilterState {
  isOpen: boolean;
  currentAttribute: Attribute;
}

export type FilterAction =
  | { type: "OPEN_FILTER" }
  | { type: "CLOSE_FILTER" }
  | { type: "SET_ATTRIBUTE"; payload: Attribute }
  | { type: "CLEAR_ATTRIBUTE" };

const filterReducer = (
  state: FilterState,
  action: FilterAction
): FilterState => {
  switch (action.type) {
    case "OPEN_FILTER":
      return { ...state, isOpen: true };
    case "CLOSE_FILTER":
      return { ...state, isOpen: false };
    case "SET_ATTRIBUTE":
      return { ...state, currentAttribute: action.payload };
    case "CLEAR_ATTRIBUTE":
      return { ...state, currentAttribute: { key: "", value: "" } };
    default:
      return state;
  }
};

export default function AttributesFilter({
  column,
}: Readonly<AttributesFilterProps>) {
  const [state, dispatch] = useReducer(filterReducer, {
    isOpen: false,
    currentAttribute: { key: "", value: "" },
  });

  const { attributes, addAttribute, removeAttribute } =
    useAttributeFilter(column);

  const containerRef = useRef<HTMLDivElement>(null);

  return (
    <div className="flex gap-2 items-end" tabIndex={-1} ref={containerRef}>
      <div className="flex gap-2">
        {!state.isOpen && (
          <AttributesFilterButton
            onClick={() => dispatch({ type: "OPEN_FILTER" })}
          />
        )}

        {state.isOpen && (
          <AttributesFilterForm
            addAttribute={addAttribute}
            closeForm={() => dispatch({ type: "CLOSE_FILTER" })}
            containerRef={containerRef}
          />
        )}
      </div>

      <AttributeFilterList
        attributes={attributes}
        removeAttribute={removeAttribute}
      />
    </div>
  );
}
