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
  const filterableColumns = ["serviceName", "connectionStatus", "javaVersion"];

  // Get the header group (only one header group for emplyoees table)
  const headerGroup = table.getHeaderGroups()[0];
  // Get only headers that are filterable
  const headers = headerGroup.headers.filter((header) => {
    return filterableColumns.includes(header.id);
  });

  return (
    <div className={cn("flex flex-col md:flex-row gap-4", className)}>
      {headers.map((header) => {
        return (
          <div key={header.id}>
            {header.isPlaceholder ? null : (
              <>
                {header.column.getCanFilter() ? (
                  <div>
                    <Filter column={header.column} />
                  </div>
                ) : null}
              </>
            )}
          </div>
        );
      })}
    </div>
  );
}
