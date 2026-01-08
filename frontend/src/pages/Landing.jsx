import Skeleton from "../components/Skeleton.jsx";
import {DataTable, GameColumns} from "@/components/index.js";
import {Button} from "@/components/ui/button.jsx";
import {Link} from "react-router-dom";
import {Separator} from "@/components/ui/separator.jsx";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {deleteSingleGame, getGames} from "@/store/api/gamesApi.js";

export const Landing = () => {

    const queryClient = useQueryClient();

    const {data, error, isLoading} = useQuery({
        queryKey: ['game'],
        queryFn: ({signal}) => getGames({signal}),
        staleTime: 30000
    })

    const deleteMutation = useMutation({
        mutationFn: deleteSingleGame,
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['game']});
        }
    });


    const handleRemoveGame = id => {
        deleteMutation.mutate({id});
    }

    let content;
    if (isLoading) {
        content = <Skeleton className="h-8 w-8" times={4}/>;
    } else if (error) {
        content = <div>Error fetching games. Please try again later.</div>
    } else {
        content = <DataTable columns={GameColumns} data={data} onDelete={handleRemoveGame}/>
    }

    return (<>
        <Button variant='outline'
                className='border-2 rounded-xl my-2 mt-0 px-9 py-6 text-[1rem]
                hover:bg-accent-foreground hover:text-background'>
            <Link to="/game/new">New Game</Link>
        </Button>
        <Separator className='my-2 h-0.5'/>
        <div className='m-0 p-0'>Past Games</div>
        <Separator className='my-2 h-0.5'/>
        {content}
    </>)
}

export default Landing