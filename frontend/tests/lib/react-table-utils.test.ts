import { attributeEqualsFn } from "@/lib/react-table-utils";
import { Attribute } from "@/types/Attribute";
import { Connection } from "@/types/Connection";
import { CoreRow, Row } from "@tanstack/react-table";

describe("attributeEqualsFn", () => {
  it("should return true, if filter-array is empty and row has no attributes", () => {
    const mockRow = createMockRow("service-name", []);

    const result = attributeEqualsFn(
      () => createMockRow("service-name", []),
      "attributes",
      []
    );

    expect(result).toBeTruthy();
  });

  it("should return true, if filter-array is empty and row has attributes", () => {
    const result = attributeEqualsFn(
      () =>
        createMockRow("service-name", [
          { key: "spring-version", value: "3.3.0" },
        ]),
      "attributes",
      []
    );

    expect(result).toBeTruthy();
  });

  it("should return true, if filter-array has matching row attributes", () => {
    const result = attributeEqualsFn(
      () =>
        createMockRow("service-name", [
          { key: "spring-version", value: "3.3.0" },
        ]),
      "attributes",
      [{ key: "spring-version", value: "3.3.0" }]
    );

    expect(result).toBeTruthy();
  });
});

const createMockConnection = (
  serviceName: string,
  attributes: Attribute[]
): Connection => {
  return {
    connectionId: serviceName,
    serviceName: serviceName,
    vmId: "test",
    javaVersion: "17",
    gepardVersion: "1",
    registrationTime: Date.now().toString(),
    connectionStatus: "CONNECTED",
    otelVersion: "1",
    startTime: Date.now().toString(),
    attributes: attributes,
  };
};

const createMockRow = (
  serviceName: string,
  attribtues: Attribute[]
): Row<Connection> => {
  return {
    // Create a mock Row object
    id: "1", // Row ID (string)
    original: createMockConnection(serviceName, attribtues),
    index: 0,
    depth: 0,
    subRows: [],
    parentId: undefined,
    getValue: (columnId: string) => {
      return createMockConnection(serviceName, attribtues)[
        columnId as keyof Connection
      ];
    },
    getIsSelected: () => false,
    getCanSelect: () => true,
    getIsGrouped: () => false,
    getCanGroup: () => false,
    getCanSort: () => true,
    getCanFilter: () => true,
    getIsFiltered: () => false,
    toggleSelected: () => {},
    getVisibleCells: () => [],
    getAllCells: () => [],
    getParentRow: () => null,
    getLeafRows: () => [],
  };
};
