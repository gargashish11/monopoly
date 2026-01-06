import MenuAccordion from "@/components/MenuAccordion.jsx";
import Logo from "@/components/Logo.jsx";


const Sidebar = () => {
    return (<aside className='py-4 px-8 bg-muted h-full'>
        <Logo/>
        <div className='flex flex-col mt-20 gap-y-4'>
            <MenuAccordion type={"multiple"}/>
        </div>
    </aside>);
};

export default Sidebar;
