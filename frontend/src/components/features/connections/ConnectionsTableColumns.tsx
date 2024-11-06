import SortableTableColumn from "@/components/ui/sortable-table-column";
import { Connection } from "@/types/Connection";
import { ColumnDef } from "@tanstack/react-table";

export const ConnectionsTableColumns: ColumnDef<Connection>[] = [
  {
    accessorKey: "serviceName",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Service Name" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-4 align-middle font-medium">
          {row.getValue("serviceName")}
        </div>
      );
    },
  },
  {
    accessorKey: "pid",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Process ID" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-4 align-middle font-medium">
          {row.getValue("pid")}
        </div>
      );
    },
    enableGlobalFilter: true,
  },
  {
    accessorKey: "javaVersion",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Java Version" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-4 align-middle font-medium">
          {row.getValue("javaVersion")}
        </div>
      );
    },
  },
  {
    accessorKey: "otelVersion",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Otel Version" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-4 align-middle font-medium">
          {row.getValue("otelVersion")}
        </div>
      );
    },
  },
  {
    accessorKey: "gepardVersion",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Gepard Version" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-4 align-middle font-medium">
          {row.getValue("gepardVersion")}
        </div>
      );
    },
  },
  {
    accessorKey: "startTime",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="JVM Start Time" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-4 align-middle font-medium">
          {row.getValue("startTime")}
        </div>
      );
    },
  },
  {
    accessorKey: "attributes",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Attributes" />;
    },
    cell: ({ row }) => {
      console.log(row.getValue("attributes"));
      return row.getCanExpand() ? (
        <button
          onClick={row.getToggleExpandedHandler()}
          style={{ cursor: "pointer" }}
        >
          {row.getIsExpanded() ? "👇" : "👉"}
        </button>
      ) : (
        ""
      );
    },
  },
];
