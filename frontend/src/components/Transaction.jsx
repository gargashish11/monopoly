import {CirclePlus} from "lucide-react";
import formatDate from "@/lib/utils/format.js";

const Transaction = ({transaction}) => {
    return (
        <div className="flex justify-center gap-5 shadow-xl border rounded py-2">
            <CirclePlus className="fill-green-700 text-white"/>
            <div className="flex gap-2 text-base">
                <span>{transaction.payer_name}</span>
                <span>Paid</span>
                <span>{transaction.payee_name}</span>
                <span>{transaction.amount}</span>
            </div>
            <sub className="bottom-[-1.25rem]">{formatDate(transaction.createdDate)}</sub>
        </div>
    );
};

export default Transaction;
