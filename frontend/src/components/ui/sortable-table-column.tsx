import { ArrowUpDown } from "lucide-react";
import { Column } from "@tanstack/react-table";
import { Button } from "./button";

interface SortableTableColumnProps<T> {
  column: Column<T>;
  title: string;
}

/*
A sortable table column that can be used in a table header.
Shows an arrow icon to indicate the sorting direction.
*/
export default function SortableTableColumn<T>({
  column,
  title,
}: Readonly<SortableTableColumnProps<T>>) {
  return (
    <Button
      variant="ghost"
      // when the button is clicked, the sorting direction is toggled
      onClick={() => {
        column.toggleSorting(column.getIsSorted() === "asc");
      }}
    >
      {title}
      <ArrowUpDown className="ml-2 h-4 w-4" />
    </Button>
  );
}