import { Attribute } from "@/types/Attribute";
import { Connection } from "@/types/Connection";
import { FilterFn } from "@tanstack/react-table";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const equalsInArrayFn: FilterFn<any> = (
  row,
  columnId: string,
  filterValue: string[]
) => {
  return filterValue.includes(row.getValue<string>(columnId));
};

equalsInArrayFn.autoRemove = (val: string) => testFalsey(val) || !val.length;

/**
 * Compares the attributes from the filter with the attrributes of a table row.
 *
 * Attribute Keys are matches with and
 * Values for the same key are matched with or
 * If a key is not present in the row, the row is not matching
 * If at least one value for a key is present in the row, the row is matching
 *
 * @param row
 * @param _columnId
 * @param filterValue
 * @returns
 */
export const attributeEqualsFn: FilterFn<Connection> = (
  row,
  _columnId: string,
  filterValue: Attribute[]
) => {
  if (filterValue.length === 0) {
    return true;
  }
  const rowAttributes = row.original.attributes;

  return areAttributesMatching(rowAttributes, filterValue);
};

attributeEqualsFn.autoRemove = (val: Attribute[]) => val.length === 0;

function areAttributesMatching(
  rowAttributes: Attribute[],
  filterValue: Attribute[]
): boolean {
  const filterMap: Map<string, string[]> = createFilterMap(filterValue);

  for (const [key, values] of filterMap) {
    const rowAttribute = rowAttributes.find(
      (attribute) => attribute.key === key
    );

    if (rowAttribute === undefined) {
      return false;
    }

    if (!values.includes(rowAttribute.value)) {
      return false;
    }
  }

  return true;
}

// export only for unit tests
export const areAttributesMatchingTest = areAttributesMatching;

function testFalsey(val: unknown) {
  return val === undefined || val === null || val === "";
}

function createFilterMap(filterValue: Attribute[]): Map<string, string[]> {
  const filterMap = new Map<string, string[]>();

  filterValue.forEach((attribute: Attribute) => {
    if (filterMap.has(attribute.key)) {
      const prev = filterMap.get(attribute.key) as string[];
      filterMap.set(attribute.key, [...prev, attribute.value]);
    } else {
      filterMap.set(attribute.key, [attribute.value]);
    }
  });

  return filterMap;
}
