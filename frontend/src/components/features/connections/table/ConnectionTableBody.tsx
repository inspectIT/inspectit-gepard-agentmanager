import { TableBody, TableCell, TableRow } from "@/components/ui/shadcn/table";
import { cn } from "@/lib/utils";
import { Connection } from "@/types/Connection";
import { flexRender, Row, Table } from "@tanstack/react-table";
import React from "react";
import ConnectionDetails from "./details/ConnectionDetails";
import { ConnectionsTableColumns } from "./ConnectionsTableColumns";

export default function ConnectionTableBody({
  table,
}: {
  table: Table<Connection>;
}) {
  const isExpanded = (row: Row<Connection>) => {
    return row.getIsExpanded();
  };

  return (
    <TableBody>
      {table.getRowModel().rows.length ? (
        table.getRowModel().rows.map((row) => (
          <React.Fragment key={row.id}>
            <TableRow
              key={row.id}
              data-state={row.getIsSelected() && "selected"}
            >
              {row.getVisibleCells().map((cell) => (
                <TableCell
                  className={cn(
                    "border",
                    row.getIsExpanded() && "bg-secondary"
                  )}
                  key={cell.id}
                >
                  {flexRender(cell.column.columnDef.cell, cell.getContext())}
                </TableCell>
              ))}
            </TableRow>
            {isExpanded(row) && (
              <TableRow className="border " key={row.id + "_details"}>
                <ConnectionDetails connectionId={row.original.connectionId} />
              </TableRow>
            )}
          </React.Fragment>
        ))
      ) : (
        <TableRow>
          <TableCell
            colSpan={ConnectionsTableColumns.length}
            className="h-24 text-center"
          >
            No results.
          </TableCell>
        </TableRow>
      )}
    </TableBody>
  );
}
