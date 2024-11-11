import { Button } from "@/components/ui/shadcn/button";
import { Input } from "@/components/ui/shadcn/input";

import { Attribute } from "@/types/Attribute";
import { Connection } from "@/types/Connection";
import { Column } from "@tanstack/react-table";
import { X } from "lucide-react";
import { useState } from "react";

interface ConnectionAttributeFilterFormProps {
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
  setAttributes: React.Dispatch<React.SetStateAction<Attribute[]>>;
  column: Column<Connection>;
  columnFilterValues: string[] | undefined;
  sortedUniqueValues: string[];
}

export default function ConnectionAttributesFilterForm<({
  setAttributes,
  setOpen,
  column,
  columnFilterValues,
}: Readonly<ConnectionAttributeFilterFormProps>) {
  const [key, setKey] = useState("");
  const [value, setValue] = useState("");

  const handleClick = () => {
    if (key !== "" && value !== "") {
      setOpen(false);
      setAttributes((prev: Attribute[]) => [...prev]);
      if (columnFilterValues === undefined) {
        setAttributes((prev) => [...prev, { key, value }]);
        column.setFilterValue([{ key, value }]);
      } else {
        setAttributes((prev) => [...prev, { key, value }]);
        column.setFilterValue((prev: Attribute[]) => [...prev, { key, value }]);
      }
    }
  };

  const handleCancle = () => {
    setKey("");
    setValue("");
    setOpen(false);
  };
  return (
    <div className="flex items-center gap-4">
      <Input
        onChange={(e) => setKey(e.target.value)}
        className="w-32"
        placeholder="key"
      />
      <Input
        onChange={(e) => setValue(e.target.value)}
        className="w-32"
        placeholder="value"
      />
      {/* <Switch id="regex-mode" /> */}
      {/* <Label className="font-thin" htmlFor="regex-mode">
        Regex
      </Label> */}
      <div className="flex items-end gap-2">
        <Button size={"sm"} onClick={handleClick}>
          Add
        </Button>
        <Button size={"sm"} onClick={handleCancle}>
          <X />
        </Button>
      </div>
    </div>
  );
}
