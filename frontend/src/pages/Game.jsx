import {useQuery} from "@tanstack/react-query";
import {getSingleGame} from "@/store/api/gamesApi.js";
import {useParams} from "react-router-dom";
import {GameNameForm, Players, Skeleton, Transactions} from "@/components/index.js";

const Game = () => {
    const {id} = useParams();
    const {data: game, error, isLoading} = useQuery({
        queryKey: ['game', {id}],
        queryFn: ({signal}) => getSingleGame({signal, id})
    })
    let content;
    if (isLoading) {
        content = <Skeleton className="h-8 w-8" times={4}/>;
    } else if (error) {
        content = <div>Error fetching game. Please try again later.</div>
    } else {
        content = <>
            <GameNameForm id={game.id} name={game.name}/>
            <Players players={game.gamePlayers}/>
            <Transactions transactions={game.transactions}/>
        </>;
    }
    return (<>
        {content}
    </>);
};

export default Game;
