import {useRouteError} from "react-router-dom";

const SinglePageError = () => {
    const error = useRouteError();
    return (
        <div className="p-4 text-center">
            <h2 className="text-destructive font-semibold text-lg">
                {error?.message || error?.statusText || "An unexpected error occurred"}
            </h2>
        </div>
    )
}

export default SinglePageError