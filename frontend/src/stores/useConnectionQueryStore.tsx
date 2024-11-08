import { ConnectionQuery } from "@/types/Connection";
import { create } from "zustand";

interface ConnectionQueryState {
  query: ConnectionQuery;
  setQuery: (query: ConnectionQuery) => void;
}
/*
A store that manages the state of the sidebar.
*/
export const useConnectionQueryStore = create<ConnectionQueryState>((set) => ({
  query: {},
  setQuery: (newQuery) => set({ query: newQuery }),
}));
