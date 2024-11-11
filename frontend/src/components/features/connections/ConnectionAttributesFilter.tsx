import { Button } from "@/components/ui/shadcn/button";
import { useState } from "react";
import ConnectionAttributesFilterForm from "./ConnectionAttributeFilterForm";
import { Badge } from "@/components/ui/shadcn/badge";
import { Attribute } from "@/types/Attribute";
import { Column, Table } from "@tanstack/react-table";
import { Connection } from "@/types/Connection";
import { Cross, Delete, X } from "lucide-react";
import { ExitIcon } from "@radix-ui/react-icons";

interface ConnectionAttributesFilterProps {
  column: Column<Connection>;
  columnFilterValues: string[] | undefined;
  sortedUniqueValues: string[];
}

export default function ConnectionAttributesFilter({
  column,
  columnFilterValues,
  sortedUniqueValues,
}: ConnectionAttributesFilterProps) {
  const [open, setOpen] = useState(false);
  console.log(sortedUniqueValues);
  const [attributes, setAttributes] = useState<Attribute[]>([]);

  const handleDeleteAttribute = (targetAttribute: Attribute) => {
    console.log(targetAttribute);
    setAttributes((prev: Attribute[]) => {
      console.log(prev);
      return prev.filter(
        (prevAttribute) =>
          prevAttribute.key != targetAttribute.key &&
          prevAttribute.value != targetAttribute.value
      );
    });

    column.setFilterValue((prev: Attribute[]) => {
      console.log(prev);
      return prev.filter(
        (prevAttribute) =>
          prevAttribute.key != targetAttribute.key &&
          prevAttribute.value != targetAttribute.value
      );
    });
  };

  return (
    <div className="flex gap-2 items-end">
      <div className="flex gap-2">
        {!open && (
          <Button size={"sm"} onClick={() => setOpen((prev) => !prev)}>
            New Attribute Filter
          </Button>
        )}

        {open && (
          <ConnectionAttributesFilterForm
            setOpen={setOpen}
            setAttributes={setAttributes}
            column={column}
            sortedUniqueValues={sortedUniqueValues}
            columnFilterValues={columnFilterValues}
          />
        )}
      </div>
      {attributes.map((attribute, index) => (
        <div
          key={`${attribute.key}_${index.toString()}`}
          className="flex gap-2 mt-2"
        >
          <Button
            onClick={() => handleDeleteAttribute(attribute)}
            size="sm"
            variant="secondary"
          >
            {attribute.key}={attribute.value}
            <X />
          </Button>
        </div>
      ))}
    </div>
  );
}
