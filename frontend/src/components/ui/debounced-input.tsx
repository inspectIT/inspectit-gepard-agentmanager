import { InputHTMLAttributes, useEffect, useState } from "react";
import { Input } from "./input";

// A typical debounced input react component
export default function DebouncedInput({
  value: initialValue,
  onChange,
  debounce = 500,
  ...props
}: {
  value: string | number;
  onChange: (value: string | number) => void;
  debounce?: number;
} & Omit<InputHTMLAttributes<HTMLInputElement>, "onChange">) {
  const [value, setValue] = useState(initialValue);

  useEffect(() => {
    setValue(initialValue);
  }, [initialValue]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      console.log(value);
      onChange(value);
    }, debounce);

    return () => clearTimeout(timeout);
  }, [value]);

  return (
    <>
      <Input
        {...props}
        value={value}
        onChange={(e) => setValue(e.target.value)}
      />
    </>
  );
}
