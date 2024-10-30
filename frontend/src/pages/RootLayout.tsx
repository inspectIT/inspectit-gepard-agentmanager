import Header from "@/components/features/header/Header";
import Sidebar from "@/components/features/sidebar/Sidebar";
import { Outlet } from "react-router-dom";

export default function RootLayout() {
  return (
    <>
      <Header />
      <div className="flex h-screen border-collapse overflow-hidden">
        <Sidebar />
        <main className="flex-1 h-screen overflow-x-hidden pt-16 bg-secondary/10 pb-1">
          <Outlet />
        </main>
      </div>
    </>
  );
}
