import { useState } from "react";
import { MenuIcon } from "lucide-react";
import SideNavigation from "./SideNavigation";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";

/*
This components is used, if the viewport is smaller than md.
Also includes the SideNav component, like Sidebar.
But only opens up on click on the MenuIcon (Burger Button).
*/
export default function MobileSidebar() {
  const [open, setOpen] = useState(false);

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <div className="flex items-center justify-center gap-2">
          <MenuIcon />
          <h1 className="text-lg font-semibold">Gepard Configuration Server</h1>
        </div>
      </SheetTrigger>
      <SheetContent side="left" className="w-72">
        <div className="px-1 py-6 pt-16">
          <SideNavigation setOpen={setOpen} />
        </div>
      </SheetContent>
    </Sheet>
  );
}
