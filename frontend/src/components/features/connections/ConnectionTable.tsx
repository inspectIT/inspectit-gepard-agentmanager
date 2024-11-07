import {
  ColumnDef,
  getCoreRowModel,
  useReactTable,
  flexRender,
  getPaginationRowModel,
  PaginationState,
  SortingState,
  getSortedRowModel,
  getFilteredRowModel,
  getExpandedRowModel,
  Row,
} from "@tanstack/react-table";
import { Dispatch, SetStateAction, useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import React from "react";
import { Connection } from "@/types/Connection";
import ConnectionDetails from "./ConnectionDetails";
import { cn } from "@/lib/utils";

interface DataTableProps {
  columns: ColumnDef<Connection>[];
  data: Connection[];
  globalFilter: string;
  setGlobalFilter: Dispatch<SetStateAction<string>>;
  expanded?: true | Record<string, boolean>;
  setExpanded: Dispatch<SetStateAction<true | Record<string, boolean>>>;
  children?: React.ReactNode;
}

export default function ConnectionTable({
  columns,
  data,
  globalFilter,
  setGlobalFilter,
  expanded,
  setExpanded,
}: Readonly<DataTableProps>) {
  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 10,
  });
  const [sorting, setSorting] = useState<SortingState>([]);

  const table = useReactTable<Connection>({
    data,
    columns,
    onGlobalFilterChange: setGlobalFilter,
    onExpandedChange: setExpanded,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    onPaginationChange: setPagination,
    onSortingChange: setSorting,
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getExpandedRowModel: getExpandedRowModel(),
    debugTable: true,
    debugHeaders: true,
    debugColumns: true,
    state: {
      globalFilter,
      sorting,
      pagination,
      expanded,
    },
    getRowCanExpand: () => true,
  });

  const isExpanded = (row: Row<Connection>) => {
    return row.getIsExpanded();
  };

  return (
    <>
      <Table>
        <TableHeader className="sticky top-0 bg-secondary">
          {table.getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((header) => {
                return (
                  <TableHead className="border" key={header.id}>
                    {header.isPlaceholder
                      ? null
                      : flexRender(
                          header.column.columnDef.header,
                          header.getContext()
                        )}
                  </TableHead>
                );
              })}
            </TableRow>
          ))}
        </TableHeader>
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
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </TableCell>
                  ))}
                </TableRow>
                {isExpanded(row) && (
                  <TableRow className="border " key={row.id + "_details"}>
                    <ConnectionDetails pid={row.original.pid} />
                  </TableRow>
                )}
              </React.Fragment>
            ))
          ) : (
            <TableRow>
              <TableCell colSpan={columns.length} className="h-24 text-center">
                No results.
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
      {/* <ScrollBar orientation="horizontal" /> */}

      <div className="flex items-center justify-end space-x-2 py-4">
        <Button
          variant="outline"
          size="sm"
          onClick={() => {
            table.previousPage();
          }}
          disabled={!table.getCanPreviousPage()}
        >
          Previous
        </Button>
        <Button
          variant="outline"
          size="sm"
          onClick={() => {
            table.nextPage();
          }}
          disabled={!table.getCanNextPage()}
        >
          Next
        </Button>
      </div>
    </>
  );
}
