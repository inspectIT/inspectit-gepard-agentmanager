import { Button } from "@/components/ui/shadcn/button";
import { Dispatch, SetStateAction, useEffect, useState } from "react";
import { QueryParameter } from "./QuickSearch";

interface QuickSearchAutocompleteProps {
  inputValue: string;
  stage: number;
  setStage: Dispatch<SetStateAction<number>>;
  setNewQueryParameter: Dispatch<SetStateAction<QueryParameter>>;
  recommendations: string[];
  setInputValue: Dispatch<SetStateAction<string>>;
}

export default function QuickSearchAutocomplete({
  inputValue,
  setNewQueryParameter,
  stage,
  setStage,
  recommendations,
  setInputValue,
}: Readonly<QuickSearchAutocompleteProps>) {
  const [filteredRecommendations, setFilteredRecommendations] = useState<
    string[]
  >([]);

  useEffect(() => {
    setFilteredRecommendations(
      recommendations.filter((recommendation) =>
        recommendation.toLowerCase().includes(inputValue.toLowerCase())
      )
    );
  }, [inputValue, stage, recommendations]);

  const handleSelectRecommendation = (recommendation: string) => {
    if (stage === 1) {
      setNewQueryParameter((prev: QueryParameter) => ({
        ...prev,
        key: recommendation,
      }));
      setInputValue("");
    }
    if (stage === 2) {
      if (recommendation === "=" || recommendation === "~") {
        setNewQueryParameter((prev: QueryParameter) => ({
          ...prev,
          mode: recommendation,
        }));
        setInputValue("");
      }
    }
    if (stage === 3) {
      setNewQueryParameter((prev: QueryParameter) => ({
        ...prev,
        value: recommendation,
      }));
    }

    setStage((prev) => prev + 1);
  };

  // Stage 1: show all the different connection attributes we can search for
  // Stage 2: show regex or equal sign to select search mode for this attribute
  // Stage 3.1: let user type in the value they want to search for
  // All Stages can be filtered
  return (
    <div className="absolute z-50 top-10 bg-secondary">
      <ul>
        {filteredRecommendations.map((recommendation) => (
          <li key={recommendation}>
            <Button
              variant={"ghost"}
              onClick={() => handleSelectRecommendation(recommendation)}
            >
              {recommendation}
            </Button>
          </li>
        ))}
      </ul>
    </div>
  );
}
