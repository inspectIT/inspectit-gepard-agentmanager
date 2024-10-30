import { cn } from "@/lib/utils";
import { Link, useLocation } from "react-router-dom";
import { buttonVariants } from "@/components/ui/button";
import { NAV_ITEMS } from "./NavItems";
import { useSidebarStore } from "@/stores/useSidebarStore";

interface SideNavProps {
  className?: string;
  setOpen?: (open: boolean) => void;
}

/*
A SideNav component that displays a list of NavItems.
*/
export default function SideNavigation({ className, setOpen }: SideNavProps) {
  const path = useLocation();
  const { isOpen } = useSidebarStore();

  const items = NAV_ITEMS;

  return (
    <nav className="space-y-2">
      {items.map((item) => (
        <Link
          key={item.title}
          to={item.href[0]}
          onClick={() => {
            if (setOpen) setOpen(false);
          }}
          className={cn(
            buttonVariants({ variant: "ghost" }),
            "group relative flex h-12 justify-start",
            item.href.includes(path.pathname) &&
              "bg-muted font-bold hover:bg-muted"
          )}
        >
          <item.icon className={cn("h-5 w-5", item.color)} />
          <span
            className={cn(
              "absolute left-12 text-base duration-200",
              !isOpen && className
            )}
          >
            {item.title}
          </span>
        </Link>
      ))}
    </nav>
  );
}
