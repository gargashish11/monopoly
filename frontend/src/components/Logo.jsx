import classNames from "classnames";
import {Link} from "react-router-dom";

const Logo = ({className}) => {
    const classes = classNames('mx-auto', 'text-2xl', className);
    return (<span className={classes}>
        <Link to="/">Monopoly</Link>
    </span>);
};

export default Logo;
