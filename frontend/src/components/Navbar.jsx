import {useState} from 'react';
import {Menu, X, Home, Info, Settings, User, Swords} from 'lucide-react';
import {Button} from '@/components/ui/button';
import {
    Sheet,
    SheetContent,
    SheetHeader,
    SheetTitle,
    SheetTrigger,
} from '@/components/ui/sheet';
import {Link} from "react-router-dom";
import Logo from "@/components/Logo.jsx";

const Navbar = () => {
    const [isOpen, setIsOpen] = useState(false);

    const navItems = [
        {name: 'Games', href: '/game/all', icon: Swords},
        {name: 'Players', href: '/players', icon: User},
    ];

    return (
        <nav className="bg-muted p-4 border-b sticky top-0 z-50">
            <div className="max-w-7xl mx-auto flex items-center justify-between">
                <Logo/>

                {/* Desktop Navigation */}
                <div className="hidden md:flex items-center gap-2">
                    {navItems.map((item) => {
                        const Icon = item.icon;
                        return (
                            <Button
                                key={item.name}
                                variant="ghost"
                                asChild
                                className="gap-2"
                            >
                                <a href={item.href}>
                                    <Icon className="h-4 w-4"/>
                                    {item.name}
                                </a>
                            </Button>
                        );
                    })}
                    <Button variant="default" size="sm" className="ml-2">
                        Get Started
                    </Button>
                </div>

                {/* Mobile Navigation */}
                <div className="md:hidden">
                    <Sheet open={isOpen} onOpenChange={setIsOpen}>
                        <SheetTrigger asChild>
                            <Button variant="ghost" size="icon">
                                {isOpen ? <X className="h-6 w-6"/> : <Menu className="h-6 w-6"/>}
                            </Button>
                        </SheetTrigger>
                        <SheetContent side="right" className="w-[300px] sm:w-[400px] fixed top-0 right-0 h-full">
                            <SheetHeader className="text-left">
                                <SheetTitle>Menu</SheetTitle>
                            </SheetHeader>
                            <div className="flex flex-col gap-4 mt-8">
                                {navItems.map((item) => {
                                    const Icon = item.icon;
                                    return (
                                        <Link
                                            key={item.name}
                                            to={item.href}
                                            onClick={() => setIsOpen(false)}
                                            className="flex items-center gap-3 px-4 py-3 rounded-lg hover:bg-accent transition-colors"
                                        >
                                            <Icon className="h-5 w-5"/>
                                            <span className="text-base font-medium">{item.name}</span>
                                        </Link>
                                    );
                                })}
                            </div>
                        </SheetContent>
                    </Sheet>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;