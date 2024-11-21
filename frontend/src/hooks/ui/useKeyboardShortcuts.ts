import { useEffect } from "react";

export const useKeyboardShortcut = (
  targetKeys: { key: string; callback: () => void }[],
  targetElement?: HTMLElement | null
) => {
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      const shortcut = targetKeys.find((target) => target.key === e.key);
      if (shortcut) {
        shortcut.callback();
      }
    };

    const element = targetElement || document;
    element.addEventListener("keydown", handleKeyDown as EventListener);

    return () => {
      element.removeEventListener("keydown", handleKeyDown as EventListener);
    };
  }, [targetKeys, targetElement]);
};
