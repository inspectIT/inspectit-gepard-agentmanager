import { isRouteErrorResponse, useRouteError } from "react-router-dom";

export default function ErrorPage() {
  const error = useRouteError();
  console.error(error);

  const getErrorMessage = (error: unknown): string => {
    if (isRouteErrorResponse(error)) {
      return error.statusText;
    } else if (error instanceof Error) {
      return error.message;
    } else {
      return "error of unknown type occurred";
    }
  };

  return (
    <div id="error-page">
      <h1>Oops!</h1>
      <p>Sorry, an unexpected error has occurred.</p>
      <p>
        <i>{getErrorMessage(error)}</i>
      </p>
    </div>
  );
}
