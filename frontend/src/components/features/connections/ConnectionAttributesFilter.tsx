import { Button } from "@/components/ui/shadcn/button";
import { useState } from "react";
import ConnectionAttributesFilterForm from "./ConnectionAttributeFilterForm";
import { Badge } from "@/components/ui/shadcn/badge";
import { Attribute } from "@/types/Attribute";

export default function ConnectionAttributesFilter() {
  const [open, setOpen] = useState(false);
  const [attributes, setAttributes] = useState<Attribute[]>([]);
  return (
    <div>
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
          />
        )}
      </div>
      {attributes.map((attribute, index) => (
        <div
          key={`${attribute.key}_${index.toString()}`}
          className="flex gap-2 mt-2"
        >
          <Badge key={attribute.key}>{attribute.value}</Badge>
        </div>
      ))}
    </div>
  );
}
