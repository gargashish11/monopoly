import {Button} from "@/components/ui/button.jsx";
import {ArrowUpDown, CircleMinus} from "lucide-react"
import formatDate from "@/lib/utils.js";


const GameColumns = [
    {
        accessorKey: "name",
        header: ({column}) => {
            return (
                <Button variant="ghost"
                        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>
                    Game
                    <ArrowUpDown className="ml-4 h-4 w-4"/>
                </Button>
            )
        },
        cell: ({row}) => <div className="text-center font-medium">
            {row.getValue("name")}
        </div>
    },
    {
        accessorKey: "lastModifiedDate",
        header: ({column}) => {
            return (
                <Button variant="ghost"
                        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}>
                    Last Played On
                    <ArrowUpDown className="ml-4 h-4 w-4"/>
                </Button>
            )
        },
        cell: ({row}) => <div className="text-center font-medium">
            {formatDate(row.getValue("lastModifiedDate"))}
        </div>
    },
    {
        accessorKey: "id",
        header: ({column}) => {
            return (
                <div>
                    Delete
                </div>
            )
        },
        cell: ({row, table}) =>
            <div className="cursor-pointer"
                 onClick={e => {
                     e.stopPropagation();
                     // Call the function passed via meta
                     table.options.meta?.removeGame(row.original.id);
                 }}
            >
                <CircleMinus color="#fff" className='mx-auto fill-red-700'/>
            </div>
    }
]

export default GameColumns;
