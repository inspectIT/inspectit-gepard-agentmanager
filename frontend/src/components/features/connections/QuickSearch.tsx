import { Input } from "@/components/ui/shadcn/input";
import { useEffect, useRef, useState } from "react";
import QuickSearchAutocomple from "./QuickSearchAutocomple";
import { ConnectionQuerySchema } from "@/types/Connection";
import { cn } from "@/lib/utils";

export type QueryParameter = {
  key: string;
  mode: "~" | "=" | "";
  value: string;
};

export default function QuickSearch() {
  const [inputValue, setInputValue] = useState("");
  const [autocompleteDropDownOpen, setAutocompleteDropDownOpen] =
    useState(false);

  const [queryParameters, setQueryParameters] = useState<QueryParameter[]>([]);

  const [newQueryParameter, setNewQueryParameter] = useState<QueryParameter>({
    key: "",
    value: "",
    mode: "",
  });

  // All properties of the type ConnectionQuery
  const connectionQueryKeys = Object.keys(ConnectionQuerySchema.shape);
  const [availableQueryKeys, setAvailableQueryKeys] =
    useState(connectionQueryKeys);

  useEffect(() => {
    setAvailableQueryKeys(
      connectionQueryKeys.filter(
        (key) =>
          !queryParameters
            .map((queryParameter) => queryParameter.key)
            .includes(key)
      )
    );
  }, [queryParameters, connectionQueryKeys]);

  const inputRef = useRef<HTMLInputElement>(null);

  const [stage, setStage] = useState(1);

  const [recommendations, setRecommendations] =
    useState<string[]>(availableQueryKeys);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputValue(e.target.value);
  };

  const handleClick = () => {
    setAutocompleteDropDownOpen(true);
  };

  useEffect(() => {
    if (stage === 1) {
      setRecommendations(availableQueryKeys);
    }
    if (stage === 2) {
      setRecommendations(["=", "~"]);
    }
    if (stage === 3) {
      setRecommendations([]);
    }
  }, [stage, connectionQueryKeys, availableQueryKeys]);

  const handleEnter = (
    e: React.KeyboardEvent<HTMLInputElement>,
    inputValue: string
  ) => {
    if (e.key === "Enter" && stage === 3) {
      setQueryParameters((prev) => [
        ...prev,
        { ...newQueryParameter, value: inputValue },
      ]);
      setInputValue("");
      setStage(1);
      setAutocompleteDropDownOpen(false);
      setNewQueryParameter({ key: "", value: "", mode: "" });
    }
  };

  return (
    <div>
      <div className="flex items-center p-4">
        <div className="border-l border-y rounded-md py-1 px-1">
          <span className="text-sm">
            Filter by {newQueryParameter.key} {newQueryParameter.mode}{" "}
            {newQueryParameter.value}
          </span>
        </div>

        <div className="relative">
          <Input
            ref={inputRef}
            value={inputValue}
            onChange={handleInputChange}
            onClick={handleClick}
            autoFocus={true}
            onKeyUp={(e) => {
              handleEnter(e, inputValue);
            }}
            className={cn(
              "w-62 border-l-0 rounded-l-none",
              "px-" + (stage * 2).toString()
            )}
            placeholder=""
          />

          {autocompleteDropDownOpen && (
            <QuickSearchAutocomple
              inputValue={inputValue}
              setNewQueryParameter={setNewQueryParameter}
              stage={stage}
              setStage={setStage}
              recommendations={recommendations}
              setInputValue={setInputValue}
            />
          )}
        </div>
      </div>
      {queryParameters.map((queryParameter, index) => (
        <div
          key={index}
          className="border-l border-y shadow rounded-md py-1 px-1"
        >
          <span className="text-sm">
            {queryParameter.key} {queryParameter.mode} {queryParameter.value}
          </span>
        </div>
      ))}
    </div>
  );
}
