import {DataTable, GameColumns} from "@/components/index.js";
import Skeleton from "@/components/Skeleton.jsx";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {deleteSingleGame, getGames} from "@/store/api/gamesApi.js";

const RecentGames = () => {
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

    const handleRemoveGame = (id) => {
        deleteMutation.mutate({id});
    };

    let content;
    if (isLoading) {
        content = <Skeleton className="h-8 w-8" times={4}/>;
    } else if (error) {
        content = <div>Error fetching games. Please try again later.</div>
    } else if (data.length === 0) {
        content = <div>No games found.</div>
    } else {
        content = <DataTable columns={GameColumns} data={data} onDelete={handleRemoveGame}/>
    }

    return (<>{content}</>)
}

export default RecentGames