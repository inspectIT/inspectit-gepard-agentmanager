import { Table } from "@/components/ui/shadcn/table";
import { Table as ReactTable } from "@tanstack/react-table";
import { Connection } from "@/types/Connection";
import ConnectionTableHeader from "./ConnectionTableHeader";
import ConnectionTableBody from "./ConnectionTableBody";

interface ConnectionTableProps {
  table: ReactTable<Connection>;
}

export default function ConnectionTable({
  table,
}: Readonly<ConnectionTableProps>) {
  return (
    <div className="flex flex-col gap-4">
      <Table>
        <ConnectionTableHeader table={table} />
        <ConnectionTableBody table={table} />
      </Table>
    </div>
  );
}
