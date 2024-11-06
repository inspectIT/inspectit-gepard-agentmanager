import ConnectionsPage from "@/pages/ConnectionsPage";

export const routes = [
  {
    path: "/",
    element: <ConnectionsPage />,
    errorElement: <div>An error has occurred</div>,
  },
];
