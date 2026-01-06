import {Outlet, useNavigation} from "react-router-dom";
import Navbar from "@/components/Navbar.jsx";
import Sidebar from "@/components/Sidebar.jsx";

const HomeLayout = () => {
    const navigation = useNavigation();
    const isPageLoading = navigation.state === 'loading';

    return (<>
        <div className="grid">
            <Navbar/>
            <div className='py-8 md:px-8'>
                {isPageLoading ? <div className=""/> : <Outlet/>}
            </div>
        </div>
    </>)
}

export default HomeLayout