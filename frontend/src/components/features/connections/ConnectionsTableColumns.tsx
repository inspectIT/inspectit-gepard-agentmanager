import RowExpander from "@/components/ui/row-expander";
import SortableTableColumn from "@/components/ui/sortable-table-column";
import UnsortableTableColumn from "@/components/ui/unsortable-table-column";
import { Connection } from "@/types/Connection";
import { ColumnDef } from "@tanstack/react-table";
import ConnectionStatusBadge from "./ConnectionStatusBadge";

export const ConnectionsTableColumns: ColumnDef<Connection>[] = [
  {
    accessorKey: "serviceName",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Service Name" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-2 align-middle font-medium">
          {row.getValue("serviceName")}
        </div>
      );
    },
  },
  {
    accessorKey: "connectionStatus",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Connection Status" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-2 align-middle font-medium">
          <ConnectionStatusBadge status={row.getValue("connectionStatus")} />
        </div>
      );
    },
  },
  // {
  //   accessorKey: "vmId",
  //   header: ({ column }) => {
  //     return <SortableTableColumn column={column} title="VM ID" />;
  //   },
  //   cell: ({ row }) => {
  //     return (
  //       <div className="px-4 align-middle font-medium">
  //         {row.getValue("vmId")}
  //       </div>
  //     );
  //   },
  //   enableGlobalFilter: true,

  // },
  {
    accessorKey: "javaVersion",
    header: ({ column }) => {
      return <SortableTableColumn column={column} title="Java Version" />;
    },
    cell: ({ row }) => {
      return (
        <div className="px-2 align-middle font-medium">
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
        <div className="px-2 align-middle font-medium">
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
        <div className="px-2 align-middle font-medium">
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
        <div className="px-2 align-middle font-medium">
          {row.getValue("startTime")}
        </div>
      );
    },
  },
  {
    accessorKey: "details",
    header: () => {
      return <UnsortableTableColumn title="Details" />;
    },
    cell: ({ row }) => {
      return <RowExpander row={row} />;
    },
  },
];
