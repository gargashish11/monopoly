import links from "@/data/links.js";
import {Accordion, AccordionContent, AccordionItem, AccordionTrigger} from "@/components/ui/accordion.jsx";
import {Link} from "react-router-dom";

const MenuAccordion = ({...props}) => {
    return (
        <Accordion type={props.type ?? "single"} collapsible={props.type ?? "true"}>
            {links.map((link, index) => {
                return (
                    <AccordionItem value={`item-${index + 1}`} key={index} className='p-3'>
                        <AccordionTrigger className='capitalize'>
                            {link.label}
                        </AccordionTrigger>
                        {link.hrefs.map((href, index) => {
                            return (
                                <AccordionContent key={index} className='text-start'>
                                    <Link to={href.to}>{href.label}</Link>
                                </AccordionContent>
                            )
                        })}
                    </AccordionItem>)
            })}
        </Accordion>
    )
}

export default MenuAccordion