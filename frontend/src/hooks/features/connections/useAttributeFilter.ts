import { Attribute } from "@/types/Attribute";
import { Connection } from "@/types/Connection";
import { Column } from "@tanstack/react-table";
import { useState } from "react";

export const useAttributeFilter = (column: Column<Connection>) => {
  const [attributes, setAttributes] = useState<Attribute[]>([]);

  const columnFilterValues = column.getFilterValue() as string[] | undefined;

  const addAttribute = (attribute: Attribute): boolean => {
    if (attribute.key === "" || attribute.value === "") {
      return false;
    }
    setAttributes((prev: Attribute[]) => [...prev, attribute]);
    if (columnFilterValues === undefined) {
      column.setFilterValue([attribute]);
    } else {
      column.setFilterValue((prev: Attribute[]) => [...prev, attribute]);
    }

    return true;
  };

  const removeAttribute = (attribute: Attribute) => {
    setAttributes((prev: Attribute[]) =>
      prev.filter(
        (prevAttribute) =>
          prevAttribute.key !== attribute.key ||
          prevAttribute.value !== attribute.value
      )
    );
    column.setFilterValue((prev: Attribute[]) => {
      return prev.filter(
        (prevAttribute) =>
          prevAttribute.key != attribute.key ||
          prevAttribute.value != attribute.value
      );
    });
  };

  return { attributes, addAttribute, removeAttribute };
};
