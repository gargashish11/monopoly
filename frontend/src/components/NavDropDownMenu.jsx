import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {Button} from "@/components/ui/button.jsx";
import {AlignLeft} from "lucide-react";
import links from "@/data/links.js";
import {Link} from "react-router-dom";

const NavDropDownMenu = () => {
    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild className='lg:hidden'>
                <Button variant='outline' size='icon'>
                    <AlignLeft/>
                    <span className='sr-only'>Toggle Links</span>
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-56">
                {links.map(link => {
                    return (<DropdownMenuGroup key={link.label}>
                        <DropdownMenuLabel className='capitalize'>{link.label}</DropdownMenuLabel>
                        <DropdownMenuSeparator className='bg-gray-200'/>
                        {link.hrefs.map(href => {
                            return (
                                <DropdownMenuItem key={href.to} asChild>
                                    <Link to={href.to}>{href.label}</Link>
                                </DropdownMenuItem>
                            )
                        })}
                    </DropdownMenuGroup>)
                })}
            </DropdownMenuContent>
        </DropdownMenu>
    )
}

export default NavDropDownMenu