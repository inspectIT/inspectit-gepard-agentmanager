import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import DataTable from "@/components/ui/data-table";
import DebouncedInput from "@/components/ui/debounced-input";
import { Connection } from "@/types/Connection";
import { useState } from "react";
import { ConnectionsTableColumns } from "./ConnectionsTableColumns";

interface ConnectionsViewProps {
  connections: Connection[];
}

export default function ConnectionsView({
  connections,
}: Readonly<ConnectionsViewProps>) {
  const [globalFilter, setGlobalFilter] = useState("");

  const [expanded, setExpanded] = useState<true | Record<string, boolean>>({});

  console.log("ConnectionsView", connections);
  return (
    <Card className="w-full border-0 h-full">
      <CardHeader>
        <CardTitle>Connections</CardTitle>
        <CardDescription>All currently connected agents.</CardDescription>
      </CardHeader>
      <CardContent>
        <div className="pb-6">
          <DebouncedInput
            value={globalFilter && ""}
            onChange={(value) => {
              setGlobalFilter(String(value));
            }}
            className="p-2 font-lg shadow border border-block"
            placeholder="Quick Search..."
          />
        </div>
        <DataTable
          columns={ConnectionsTableColumns}
          data={connections}
          setGlobalFilter={setGlobalFilter}
          globalFilter={globalFilter}
          setExpanded={setExpanded}
          expanded={expanded}
          getRowCanExpand={() => true}
        />
      </CardContent>
    </Card>
  );
}
