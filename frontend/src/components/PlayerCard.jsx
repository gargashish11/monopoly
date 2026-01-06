import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card.jsx";
import {Dialog, DialogTrigger} from "@/components/ui/dialog.jsx";
import {NewTransactionModal} from "@/components/index.js";
import {useDispatch} from "react-redux";
import {resetPayees, updatePayees} from "@/store/index.js";

const PlayerCard = ({player, allPlayers}) => {
    const dispatch = useDispatch();

    const changePayees = (dispatch) => {
        dispatch(resetPayees())
        dispatch(updatePayees(allPlayers.filter(p => p.id !== player.id)));
    }

    return (<>
        <Dialog>
            <DialogTrigger onClick={() => changePayees(dispatch)}
                           asChild={player.balance !== 0}
                           disabled={player.balance === 0}>
                <Card className="grow shadow-xl border rounded-xl">
                    <CardHeader className="p-2">
                        <CardTitle className={`text-xl && ${player.balance === 0 ? "text-gray-500":""}`}>{player.player_name}</CardTitle>
                    </CardHeader>
                    <CardContent className="p-2">
                        <p>{player.balance}</p>
                    </CardContent>
                </Card>
            </DialogTrigger>
            <NewTransactionModal payer={player}/>
        </Dialog>
    </>);
};

export default PlayerCard;
