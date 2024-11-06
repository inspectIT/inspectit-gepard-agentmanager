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
} from "./table";
import { Button } from "./button";
import { ScrollArea } from "@radix-ui/react-scroll-area";
import { ScrollBar } from "./scroll-area";
import React from "react";

interface DataTableProps<TData, TValue> {
  columns: ColumnDef<TData, TValue>[];
  data: TData[];
  globalFilter: string;
  setGlobalFilter: Dispatch<SetStateAction<string>>;
  expanded?: true | Record<string, boolean>;
  setExpanded: Dispatch<SetStateAction<true | Record<string, boolean>>>;
  getRowCanExpand: (row: Row<TData>) => boolean;
}

export default function DataTable<TData, TValue>({
  columns,
  data,
  globalFilter,
  setGlobalFilter,
  expanded,
  setExpanded,
  getRowCanExpand,
}: Readonly<DataTableProps<TData, TValue>>) {
  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 10,
  });
  const [sorting, setSorting] = useState<SortingState>([]);

  const table = useReactTable<TData>({
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
    getSubRows: (row) => (row as TData & { children?: TData[] }).children ?? [],
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
    getRowCanExpand,
  });

  table.getAllLeafColumns().forEach((column) => {
    console.log("Column", column.id, column);
  });

  return (
    <>
      <ScrollArea className="sm:h-[60vh] 2xl:h-full">
        <Table className="border">
          <TableHeader className="sticky top-0 bg-secondary">
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
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
                // <TableRow
                //   key={row.id}
                //   data-state={row.getIsSelected() && "selected"}
                // >
                //   {row.getVisibleCells().map((cell) => (
                //     <TableCell key={cell.id}>
                //       {flexRender(
                //         cell.column.columnDef.cell,
                //         cell.getContext()
                //       )}
                //     </TableCell>
                //   ))}
                // </TableRow>
                <React.Fragment key={row.id}>
                  {/* Normal row UI */}
                  <TableRow
                    key={row.id}
                    data-state={row.getIsSelected() && "selected"}
                  >
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id}>
                        {flexRender(
                          cell.column.columnDef.cell,
                          cell.getContext()
                        )}
                      </TableCell>
                    ))}
                  </TableRow>
                  {/* If the row is expanded, render the expanded UI as a separate row with a single cell that spans the width of the table */}
                  {row.getIsExpanded() &&
                    row.getValue("attributes").length > 0 && (
                      <TableRow key={row.id}>
                        <TableCell colSpan={columns.length}>
                          <div className="p-4">
                            <div>
                              {row.getValue("attributes").map((attr) => (
                                <div key={attr.key}>
                                  <div className="font-semibold">
                                    {attr.key}
                                  </div>
                                  <div>{attr.value}</div>
                                </div>
                              ))}
                            </div>
                          </div>
                        </TableCell>
                      </TableRow>
                    )}
                </React.Fragment>
              ))
            ) : (
              <TableRow>
                <TableCell
                  colSpan={columns.length}
                  className="h-24 text-center"
                >
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
        <ScrollBar orientation="horizontal" />
      </ScrollArea>

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
