import { areAttributesMatchingTest } from "@/lib/react-table-utils";
import { Attribute } from "@/types/Attribute";

describe("areAttributesMatching", () => {
  it("should return true, if filter-array is empty and row has no attributes", () => {
    const filterAttributes: Attribute[] = [];
    const rowAttributes: Attribute[] = [];

    const result = areAttributesMatchingTest(rowAttributes, filterAttributes);

    expect(result).toBeTruthy();
  });

  it("should return true, if filter-array is empty and row has attributes", () => {
    const filterAttributes: Attribute[] = [];
    const rowAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
    ];

    const result = areAttributesMatchingTest(rowAttributes, filterAttributes);

    expect(result).toBeTruthy();
  });

  it("should return true, if filter-array has matching row attributes", () => {
    const filterAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
    ];
    const rowAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
    ];

    const result = areAttributesMatchingTest(rowAttributes, filterAttributes);

    expect(result).toBeTruthy();
  });

  it("should return false, if filter-array has non-matching row attributes", () => {
    const filterAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
    ];
    const rowAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.1" },
    ];

    const result = areAttributesMatchingTest(rowAttributes, filterAttributes);

    expect(result).toBeFalsy();
  });

  it("should return true if filter-array has multiple attributes and row has all of them", () => {
    const filterAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
      { key: "java-version", value: "11" },
    ];
    const rowAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
      { key: "java-version", value: "11" },
    ];

    const result = areAttributesMatchingTest(rowAttributes, filterAttributes);

    expect(result).toBeTruthy();
  });

  it("should return true, if filter-array has attribute with multiple values and row has one of them", () => {
    const filterAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
    ];
    const rowAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
      { key: "spring-version", value: "3.3.1" },
    ];

    const result = areAttributesMatchingTest(rowAttributes, filterAttributes);

    expect(result).toBeTruthy();
  });

  it("should return false, if filter-array has attribute with multiple values and row has none of them", () => {
    const filterAttributes: Attribute[] = [
      { key: "spring-version", value: "3.3.0" },
      { key: "spring-version", value: "3.3.1" },
    ];
    const rowAttributes: Attribute[] = [{ key: "spring-version", value: "3" }];

    const result = areAttributesMatchingTest(rowAttributes, filterAttributes);

    expect(result).toBeFalsy();
  });
});
