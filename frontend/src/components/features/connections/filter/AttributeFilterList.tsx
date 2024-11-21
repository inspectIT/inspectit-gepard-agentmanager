import { Button } from "@/components/ui/shadcn/button";
import { Attribute } from "@/types/Attribute";
import { X } from "lucide-react";

interface AttributeFilterListProps {
  attributes: Attribute[];
  removeAttribute: (attribute: Attribute) => void;
}

export default function AttributeFilterList({
  attributes,
  removeAttribute,
}: Readonly<AttributeFilterListProps>) {
  return (
    <>
      {attributes.map((attribute, index) => (
        <div
          key={`${attribute.key}_${index.toString()}`}
          className="flex gap-2 mt-2"
        >
          <Button
            onClick={() => removeAttribute(attribute)}
            size="sm"
            variant="secondary"
          >
            {attribute.key}={attribute.value}
            <X />
          </Button>
        </div>
      ))}
    </>
  );
}
