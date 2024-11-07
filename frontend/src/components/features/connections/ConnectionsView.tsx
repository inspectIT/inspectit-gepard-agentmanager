import DebouncedInput from "@/components/ui/debounced-input";
import { Connection } from "@/types/Connection";
import { useState } from "react";
import { ConnectionsTableColumns } from "./ConnectionsTableColumns";
import ConnectionTable from "./ConnectionTable";
import {
  Page,
  PageContent,
  PageDescription,
  PageHeader,
  PageTitle,
} from "@/components/ui/page";

interface ConnectionsViewProps {
  connections: Connection[];
}

export default function ConnectionsView({
  connections,
}: Readonly<ConnectionsViewProps>) {
  const [globalFilter, setGlobalFilter] = useState("");

  const [expanded, setExpanded] = useState<true | Record<string, boolean>>({});

  return (
    <>
      <Page>
        <PageHeader>
          <PageTitle>Connections</PageTitle>
          <PageDescription>All currently connected agents.</PageDescription>
        </PageHeader>
        <PageContent>
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
          <ConnectionTable
            columns={ConnectionsTableColumns}
            data={connections}
            setGlobalFilter={setGlobalFilter}
            globalFilter={globalFilter}
            setExpanded={setExpanded}
            expanded={expanded}
          />
        </PageContent>
      </Page>
    </>
  );
}
