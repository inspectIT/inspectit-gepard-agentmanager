import { createBrowserRouter } from "react-router-dom";
import ErrorPage from "./pages/ErrorPage";
import Connections from "./pages/ConnectionsPage";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Connections />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/connections",
    element: <Connections />,
    errorElement: <ErrorPage />,
  },
]);
