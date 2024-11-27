import { ConnectionsTableColumns } from "@/components/features/connections/table/ConnectionsTableColumns";
import { attributeEqualsFn, equalsInArrayFn } from "@/lib/react-table-utils";
import { Connection } from "@/types/Connection";
import {
  ColumnFiltersState,
  getCoreRowModel,
  getExpandedRowModel,
  getFacetedMinMaxValues,
  getFacetedRowModel,
  getFacetedUniqueValues,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  PaginationState,
  SortingState,
  useReactTable,
} from "@tanstack/react-table";
import { useState } from "react";

export default function useConnectionTable(data: Connection[]) {
  const [sorting, setSorting] = useState<SortingState>([]);
  const [expanded, setExpanded] = useState<true | Record<string, boolean>>({});
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([
    { id: "connectionStatus", value: ["CONNECTED"] },
  ]);
  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 10,
  });

  const columns = ConnectionsTableColumns;

  const table = useReactTable<Connection>({
    data,
    columns,
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
    state: {
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

  return table;
}
