import { cn } from "@/lib/utils";
import { Link, useLocation } from "react-router-dom";
import { buttonVariants } from "@/components/ui/button";
import { NAV_ITEMS } from "./NavItems";

interface SideNavProps {
  className?: string;
  setOpen?: (open: boolean) => void;
}

/*
A SideNav component that displays a list of NavItems.
*/
export default function SideNavigation({ className, setOpen }: SideNavProps) {
  const path = useLocation();
  //   const { isOpen } = useSidebarStore();

  const items = NAV_ITEMS;

  return (
    <nav className="space-y-2">
      {items.map((item) => (
        <Link
          key={item.title}
          to={item.href}
          onClick={() => {
            if (setOpen) setOpen(false);
          }}
          className={cn(
            buttonVariants({ variant: "ghost" }),
            "group relative flex h-12 justify-start",
            path.pathname === item.href && "bg-muted font-bold hover:bg-muted"
          )}
        >
          <item.icon className={cn("h-5 w-5", item.color)} />
          <span
            className={cn(
              "absolute left-12 text-base duration-200"
              //   !isOpen && className
            )}
          >
            {item.title}
          </span>
        </Link>
      ))}
    </nav>
  );
}
