"use client";
import { useState } from "react";
import { BsArrowLeftShort } from "react-icons/bs";
// import SideNav from "./Sidenav";
import { cn } from "@/lib/utils";
import SideNavigation from "./SideNavigation";
// import { useSidebarStore } from "./useSidebarStore";
interface SidebarProps {
  className?: string;
}

/*
Sidebar component that displays the navigation items.
As soon the viewport is smaller than md, the sidebar is hidden and a mobile sidebar is shown in the header.
You can toggle the sidebar with the arrow icon.
The big version with a width of 52rem is default. Toggling the sidebar will change the width to 78px and only show the Icon.
*/
export default function Sidebar({ className }: SidebarProps) {
  // const { isOpen, toggle } = useSidebarStore();
  const isOpen = true;
  const [status, setStatus] = useState(false);

  const handleToggle = () => {
    setStatus(true);
    // toggle();
    setTimeout(() => setStatus(false), 500);
  };

  return (
    <nav
      role="sidebar"
      className={cn(
        `relative hidden h-screen border-r pt-20 md:block`,
        status && "duration-500",
        isOpen ? "w-52" : "w-[78px]",
        className
      )}
    >
      <BsArrowLeftShort
        role="sidebar-toggle"
        className={cn(
          "absolute -right-3 top-20 cursor-pointer rounded-full border bg-background text-3xl text-foreground",
          !isOpen && "rotate-180"
        )}
        onClick={handleToggle}
      />
      <div className="space-y-4 py-4">
        <div className="px-3 py-2">
          <div className="mt-3 space-y-1">
            <SideNavigation className="text-background opacity-0 transition-all duration-300 group-hover:z-50 group-hover:ml-4 group-hover:rounded group-hover:bg-foreground group-hover:p-2 group-hover:opacity-100" />
          </div>
        </div>
      </div>
    </nav>
  );
}
