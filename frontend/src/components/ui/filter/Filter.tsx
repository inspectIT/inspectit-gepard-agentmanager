import { Column } from "@tanstack/react-table";
import { useMemo } from "react";
import ComboFilter from "./ComboFilter";
import ConnectionAttributesFilter from "@/components/features/connections/ConnectionAttributesFilter";

interface FilterProps<TData> {
  column: Column<TData>;
}

export default function Filter<TData>({ column }: FilterProps<TData>) {
  const { filterVariant } = column.columnDef.meta ?? {};

  const columnFilterValue = column.getFilterValue() as
    | string[]
    | string
    | undefined;

  const getSortedUniqueValues = (): string[] => {
    // Not implemented yet!
    if (filterVariant === "range") {
      return [];
    } else {
      const values = Array.from(column.getFacetedUniqueValues().keys())
        .sort()
        .slice(0, 5000);
      return values as string[];
    }
  };

  const sortedUniqueValues = useMemo(getSortedUniqueValues, [
    filterVariant,
    column,
  ]);

  if (filterVariant === "range") {
    return <div>Range Filter not implemented yet!</div>;
  }

  if (filterVariant === "search") {
    return (
      <ComboFilter
        column={column}
        columnFilterValues={columnFilterValue as string[] | undefined}
        sortedUniqueValues={sortedUniqueValues}
        withSearch
      />
    );
  }

  if (filterVariant === "attributes") {
    return (
      <ConnectionAttributesFilter
        column={column}
        columnFilterValues={columnFilterValue as string[] | undefined}
        sortedUniqueValues={sortedUniqueValues}
      />
    );
  }

  if (filterVariant === "select") {
    return (
      <ComboFilter
        column={column}
        columnFilterValues={columnFilterValue as string[] | undefined}
        sortedUniqueValues={sortedUniqueValues}
      />
    );
  }
}
