import { Button } from "./button";

interface UnsortableTableColumnProps {
  title: string;
}

/*
A unsortable table column that can be used in a table header.
Shows an arrow icon to indicate the sorting direction.
*/
export default function SortableTableColumn({
  title,
}: Readonly<UnsortableTableColumnProps>) {
  return (
    <Button className="flex items-center justify-center" variant="ghost">
      {title}
    </Button>
  );
}
