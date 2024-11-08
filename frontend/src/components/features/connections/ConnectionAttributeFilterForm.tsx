import { Button } from "@/components/ui/shadcn/button";
import { Input } from "@/components/ui/shadcn/input";
import { Label } from "@/components/ui/shadcn/label";
import { Switch } from "@/components/ui/shadcn/switch";
import { Attribute } from "@/types/Attribute";

interface ConnectionAttributeFilterFormProps {
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
  setAttributes: React.Dispatch<React.SetStateAction<Attribute[]>>;
}

export default function ConnectionAttributesFilterForm({
  setAttributes,
  setOpen,
}: Readonly<ConnectionAttributeFilterFormProps>) {
  const handleClick = () => {
    setOpen(false);
    setAttributes((prev: Attribute[]) => [...prev]);
  };
  return (
    <div className="flex items-center gap-4">
      <Input className="w-32" placeholder="key" />
      <Input className="w-32" placeholder="value" />
      <Switch id="regex-mode" />
      <Label className="font-thin" htmlFor="regex-mode">
        Regex
      </Label>
      <Button size={"sm"} onClick={handleClick}>
        Add
      </Button>
    </div>
  );
}
