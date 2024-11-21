import { Button } from "@/components/ui/shadcn/button";

interface ConnectionAttributesButtonProps {
  onClick: () => void;
}

export default function ConnectionAttributesButton({
  onClick,
}: ConnectionAttributesButtonProps) {
  return (
    <Button size={"sm"} onClick={onClick}>
      New Attribute Filter
    </Button>
  );
}
