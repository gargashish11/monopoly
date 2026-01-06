import {Logo} from "@/components/index.js";


const Navbar = () => {
    return (
        <nav className='bg-muted p-4 flex items-center justify-between'>
            <Logo/>
        </nav>
    );
};

export default Navbar;
