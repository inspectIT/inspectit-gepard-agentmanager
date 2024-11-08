import { ArrowUpDown } from "lucide-react";
import { Column } from "@tanstack/react-table";
import { Button } from "@/components/ui/shadcn/button";

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
      className="px-2"
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
