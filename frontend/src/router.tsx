import { createBrowserRouter } from "react-router-dom";
import ErrorPage from "./pages/ErrorPage";
import Connections from "./pages/ConnectionsPage";
import RootLayout from "./pages/RootLayout";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    children: [
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
    ],
  },
]);
