import { TableCell } from "@/components/ui/shadcn/table";
import { useConnectionsQuery } from "@/hooks/features/connections/useConnections";
import { ConnectionsTableColumns } from "./ConnectionsTableColumns";
import DataTable from "@/components/ui/data-table";
import { AttributesTableColumns } from "./AttributesTableColumns";

interface ConnectionDetailsProps {
  connectionId: string;
}

export default function ConnectionDetails({
  connectionId,
}: Readonly<ConnectionDetailsProps>) {
  const connectionsQuery = useConnectionsQuery();
  const columnCount = ConnectionsTableColumns.length;

  if (connectionsQuery.isSuccess) {
    const connection = connectionsQuery.data.find(
      (connection) => connection.connectionId === connectionId
    );

    if (connection) {
      return (
        <TableCell colSpan={columnCount} className="px-6 py-8 border">
          <div>
            <h1 className="text-lg font-bold">Attributes</h1>
            <p>
              Attributes are passed by JVM Argument and Environment Variables.{" "}
              <br />
              They are used to map configurations.
            </p>

            <div className="mt-8 w-1/4">
              <DataTable
                data={connection.attributes}
                columns={AttributesTableColumns}
              />
            </div>
          </div>
        </TableCell>
      );
    }
  }
}
