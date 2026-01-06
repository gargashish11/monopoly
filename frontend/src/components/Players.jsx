import PlayerCard from "@/components/PlayerCard.jsx";
import {orderBy} from "lodash";

const Players = ({players}) => {
    players = orderBy(players,"player_name","asc")
    return (<div className="flex mt-5 gap-4">
        {players.map(player => (
            <PlayerCard key={player.player_id} player={player} allPlayers={players}/>))}
    </div>);
};

export default Players;
