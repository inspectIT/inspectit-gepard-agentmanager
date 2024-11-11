import { Button } from "@/components/ui/shadcn/button";
import { useState } from "react";
import ConnectionAttributesFilterForm from "./ConnectionAttributeFilterForm";
import { Attribute } from "@/types/Attribute";
import { Column } from "@tanstack/react-table";
import { X } from "lucide-react";
import { Connection } from "@/types/Connection";

interface ConnectionAttributesFilterProps<TData> {
  column: Column<TData>;
  columnFilterValues: string[] | undefined;
  sortedUniqueValues: string[];
}

export default function ConnectionAttributesFilter<TData>({
  column,
  columnFilterValues,
  sortedUniqueValues,
}: ConnectionAttributesFilterProps<TData>) {
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
            column={column as unknown as Column<Connection>}
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
