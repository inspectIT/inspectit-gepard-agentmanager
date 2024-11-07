import SortableTableColumn from "@/components/ui/sortable-table-column";
import { Attribute } from "@/types/Attribute";
import { ColumnDef } from "@tanstack/react-table";

export const AttributesTableColumns: ColumnDef<Attribute>[] = [
  {
    accessorKey: "key",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Key" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-4 align-middle font-medium">
          {row.getValue("key")}
        </div>
      );
    },
  },
  {
    accessorKey: "value",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Value" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-4 align-middle font-medium">
          {row.getValue("value")}
        </div>
      );
    },
  },
];
