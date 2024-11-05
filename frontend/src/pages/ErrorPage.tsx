import { isRouteErrorResponse, useRouteError } from "react-router-dom";

export default function ErrorPage() {
  const error = useRouteError();
  console.log(error);
  console.log(isRouteErrorResponse(error));
  return (
    <div id="error-page">
      <h1>Oops!</h1>
      <p>Sorry, an unexpected error has occurred.</p>
      <p>
        <i>
          {isRouteErrorResponse(error)
            ? error.statusText
            : error instanceof Error
            ? error.message
            : "error of unknown type occured"}
        </i>
      </p>
    </div>
  );
}
