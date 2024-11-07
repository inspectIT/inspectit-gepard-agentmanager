import { Row } from "@tanstack/react-table";
import { ChevronDown, ChevronRight } from "lucide-react";

interface RowExpanderProps<TData> {
  row: Row<TData>;
}

export default function RowExpander<TData>({
  row,
}: Readonly<RowExpanderProps<TData>>) {
  if (row.getCanExpand()) {
    return (
      <div className="px-2 align-middle font-medium">
        <button onClick={row.getToggleExpandedHandler()}>
          {row.getIsExpanded() ? <ChevronDown /> : <ChevronRight />}
        </button>
      </div>
    );
  }
}
