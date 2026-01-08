import classNames from "classnames";
import {Link} from "react-router-dom";

const Logo = ({className}) => {
    const classes = classNames('mx-auto', 'text-2xl', className);
    return (
        <div className="flex items-center gap-2">
            <Link to="/" className="font-semibold text-lg">Monopoly</Link>
        </div>
    );
};

export default Logo;