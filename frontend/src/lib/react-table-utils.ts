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

function testFalsey(val: unknown) {
  return val === undefined || val === null || val === "";
}
