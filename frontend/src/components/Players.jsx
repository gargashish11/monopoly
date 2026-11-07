import PlayerCard from "@/components/PlayerCard.jsx";
import {orderBy} from "lodash";

const Players = ({players}) => {
    const sortedPlayers = orderBy(players, "player_name", "asc");
    return (<div className="flex mt-5 gap-4">
        {sortedPlayers.map(player => (
            <PlayerCard key={player.player_id} player={player} allPlayers={sortedPlayers}/>))}
    </div>);
};

export default Players;
