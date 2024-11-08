import { Column } from "@tanstack/react-table";
import { useState } from "react";
import { Popover } from "@radix-ui/react-popover";
import { PopoverContent, PopoverTrigger } from "@/components/ui/shadcn/popover";
import { Button } from "@/components/ui/shadcn/button";
import { Check, ChevronsUpDown } from "lucide-react";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from "@/components/ui/shadcn/command";
import { CommandList } from "cmdk";

interface SearchFilterProps<TData, TValue> {
  column: Column<TData, TValue>;
  columnFilterValues: string[] | undefined;
  sortedUniqueValues: string[];
  withSearch?: boolean;
}

export default function ComboFilter<TData, TValue>({
  column,
  columnFilterValues,
  sortedUniqueValues,
  withSearch = false,
}: SearchFilterProps<TData, TValue>) {
  const [open, setOpen] = useState(false);

  const handleOnItemSelect = (value: string) => {
    column.setFilterValue((old: string[] | undefined) => {
      if (old !== undefined) {
        // Check if the value is already in the array
        const index = old.indexOf(value);
        if (index > -1) {
          // Remove the value from the array
          console.log("Remove!");
          return old.filter((item) => item !== value);
        } else {
          console.log("add!");
          // Add the value to the array
          return [...old, value];
        }
      } else {
        return [value];
      }
    });
  };
  return (
    <>
      <p className="text-sm text-muted-foreground mb-2 pl-2">
        {column.columnDef.meta?.title ??
          "Please add a Title to the ColumnDef Meta"}
      </p>

      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger asChild>
          <Button
            variant="outline"
            role="combobox"
            aria-expanded={open}
            className="w-[200px] justify-between"
          >
            {!columnFilterValues
              ? "All"
              : columnFilterValues.length == 1 && columnFilterValues[0] != ""
              ? columnFilterValues[0]
              : columnFilterValues.length == 1 && columnFilterValues[0] == ""
              ? "None"
              : "Multiple"}
            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-[200px] p-0">
          <Command>
            {withSearch && <CommandInput placeholder="Search..." />}
            <CommandList>
              <CommandEmpty>Nothing found.</CommandEmpty>
              <CommandGroup>
                {sortedUniqueValues.map((value: string) => (
                  <CommandItem
                    disabled={false}
                    key={value}
                    onSelect={() => handleOnItemSelect(value)}
                  >
                    <span>{value != "" ? value : "None"}</span>
                    {columnFilterValues?.includes(value) && (
                      <Check className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    )}
                  </CommandItem>
                ))}
              </CommandGroup>
            </CommandList>
          </Command>
        </PopoverContent>
      </Popover>
    </>
  );
}
