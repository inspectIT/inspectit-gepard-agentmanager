import { Button } from "@/components/ui/shadcn/button";
import { Input } from "@/components/ui/shadcn/input";
import { useKeyboardShortcut } from "@/hooks/ui/useKeyboardShortcuts";

import { Attribute } from "@/types/Attribute";
import { X } from "lucide-react";
import { useEffect, useRef, useState } from "react";

interface AttributeFilterFormProps {
  addAttribute: (attribute: Attribute) => boolean;
  closeForm: () => void;
  containerRef: React.RefObject<HTMLDivElement>;
}

export default function AttributesFilterForm({
  addAttribute,
  closeForm,
  containerRef,
}: AttributeFilterFormProps) {
  const [key, setKey] = useState("");
  const [value, setValue] = useState("");
  const keyInputRef = useRef<HTMLInputElement>(null);

  const handleSubmitAttribute = () => {
    const attribute: Attribute = { key, value };
    const added = addAttribute(attribute);
    if (added) {
      closeForm();
    }
  };

  useKeyboardShortcut(
    [
      { key: "Enter", callback: handleSubmitAttribute },
      { key: "Escape", callback: closeForm },
    ],
    containerRef.current
  );

  useEffect(() => {
    if (keyInputRef.current) {
      keyInputRef.current.focus();
    }
  }, []);

  return (
    <div className="flex items-center gap-4">
      <Input
        className="w-20"
        ref={keyInputRef}
        onChange={(e) => setKey(e.target.value)}
        placeholder="key"
      />
      <Input
        className="w-20"
        onChange={(e) => setValue(e.target.value)}
        placeholder="value"
      />

      {/* <Switch id="regex-mode" /> */}
      {/* <Label className="font-thin" htmlFor="regex-mode">
        Regex
      </Label> */}
      <div className="flex items-end gap-2">
        <Button size={"sm"} onClick={handleSubmitAttribute}>
          Add
        </Button>
        <Button size={"sm"} onClick={closeForm}>
          <X />
        </Button>
      </div>
    </div>
  );
}
