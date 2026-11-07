import {useState} from "react";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card.jsx";
import {Dialog, DialogTrigger} from "@/components/ui/dialog.jsx";
import {NewTransactionModal} from "@/components/index.js";
import {useDispatch} from "react-redux";
import {resetPayees, updatePayees} from "@/store/index.js";
import {cn} from "@/lib/utils.js";

const PlayerCard = ({player, allPlayers}) => {
    const dispatch = useDispatch();
    const [open, setOpen] = useState(false);

    const changePayees = () => {
        dispatch(resetPayees())
        dispatch(updatePayees(allPlayers.filter(p => p.player_id !== player.player_id)));
    }

    return (<>
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger onClick={changePayees}
                           asChild={player.balance !== 0}
                           disabled={player.balance === 0}>
                <Card className="grow shadow-xl border rounded-xl">
                    <CardHeader className="p-2">
                        <CardTitle className={cn("text-xl", player.balance === 0 && "text-gray-500")}>
                            {player.player_name}
                        </CardTitle>
                    </CardHeader>
                    <CardContent className="p-2">
                        <p>{player.balance}</p>
                    </CardContent>
                </Card>
            </DialogTrigger>
            <NewTransactionModal payer={player} onClose={() => setOpen(false)}/>
        </Dialog>
    </>);
};

export default PlayerCard;
