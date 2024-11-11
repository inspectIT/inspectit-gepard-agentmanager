import {
  ColumnDef,
  getCoreRowModel,
  useReactTable,
  getPaginationRowModel,
  PaginationState,
  SortingState,
  getSortedRowModel,
  getFilteredRowModel,
  getExpandedRowModel,
  getFacetedRowModel,
  ColumnFiltersState,
  getFacetedMinMaxValues,
  getFacetedUniqueValues,
} from "@tanstack/react-table";
import { Dispatch, SetStateAction, useState } from "react";
import { Table } from "@/components/ui/shadcn/table";
import { Connection } from "@/types/Connection";
import ConnectionTableHeader from "./ConnectionTableHeader";
import ConnectionTableBody from "./ConnectionTableBody";
import TablePagination from "@/components/ui/table-pagination";
import ConnectionFilters from "../filter/ConnectionFilters";
import { attributeEqualsFn, equalsInArrayFn } from "@/lib/react-table-utils";

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
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([
    { id: "connectionStatus", value: ["CONNECTED"] },
  ]);

  const table = useReactTable<Connection>({
    data,
    columns,
    onGlobalFilterChange: setGlobalFilter,
    onColumnFiltersChange: setColumnFilters,
    onExpandedChange: setExpanded,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    onPaginationChange: setPagination,
    onSortingChange: setSorting,
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getExpandedRowModel: getExpandedRowModel(),
    getFacetedRowModel: getFacetedRowModel(),
    getFacetedMinMaxValues: getFacetedMinMaxValues(),
    getFacetedUniqueValues: getFacetedUniqueValues(),
    debugTable: true,
    debugHeaders: true,
    debugColumns: true,
    state: {
      globalFilter,
      sorting,
      pagination,
      expanded,
      columnFilters,
    },
    filterFns: {
      equalsInArray: equalsInArrayFn,
      attributeEquals: attributeEqualsFn,
    },
    getRowCanExpand: () => true,
    initialState: {
      columnVisibility: {
        attributes: false,
      },
    },
  });

  return (
    <div className="flex flex-col gap-4">
      <ConnectionFilters table={table} />
      <Table>
        <ConnectionTableHeader table={table} />
        <ConnectionTableBody table={table} />
      </Table>
      <TablePagination table={table} />

      <pre>
        {JSON.stringify(
          { columnFilters: table.getState().columnFilters },
          null,
          2
        )}
      </pre>
    </div>
  );
}
