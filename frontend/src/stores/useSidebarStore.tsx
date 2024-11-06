import { create } from "zustand";

interface SidebarState {
  isOpen: boolean;
  toggle: () => void;
}

/*
A store that manages the state of the sidebar.
*/
export const useSidebarStore = create<SidebarState>((set) => ({
  isOpen: true,
  toggle: () => {
    set((state) => ({ isOpen: !state.isOpen }));
  },
}));
