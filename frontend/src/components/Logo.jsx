import {Link} from "react-router-dom";
import {cn} from "@/lib/utils.js";

const Logo = ({className}) => {
    return (
        <div className={cn("flex items-center gap-2", className)}>
            <Link to="/" className="font-semibold text-lg">Monopoly</Link>
        </div>
    );
};

export default Logo;