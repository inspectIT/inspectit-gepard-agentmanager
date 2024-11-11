import Filter from "@/components/ui/filter/Filter";
import { cn } from "@/lib/utils";
import { Connection } from "@/types/Connection";
import { Table } from "@tanstack/react-table";

interface ConnectionFiltersProps {
  className?: string;
  table: Table<Connection>;
}

export default function ConnectionFilters({
  className,
  table,
}: ConnectionFiltersProps) {
  // Define columns that are filterable
  const filterableColumnIds = [
    "serviceName",
    "connectionStatus",
    "javaVersion",
    "attributes",
  ];

  const columns = table.getAllColumns();

  // Get only headers that are filterable
  const filterableColumns = columns.filter((column) => {
    return filterableColumnIds.includes(column.id);
  });

  return (
    <div className={cn("flex flex-col md:flex-row gap-4 items-end", className)}>
      {filterableColumns.map((column) => {
        return (
          <div key={column.id}>
            {column.getCanFilter() ? (
              <div>
                <Filter column={column} />
              </div>
            ) : null}
          </div>
        );
      })}
    </div>
  );
}
