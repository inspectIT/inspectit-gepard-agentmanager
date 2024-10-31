import { Connection } from "@/types/Connection";

export default function ConnectionsView({
  connections,
}: {
  connections: Connection[];
}) {
  return <h1>View {connections.at(0)?.id}</h1>;
}
