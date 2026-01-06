import {DialogContent, DialogHeader, DialogTitle} from "@/components/ui/dialog.jsx";
import {Separator} from "@/components/ui/separator.jsx";
import {NewTransactionForm} from "@/components/NewTransactionForm.jsx";
import {z} from "zod";

const NewTransactionModal = ({payer}) => {
    const newTransactionFormSchema = z.object({
        amount: z.coerce.number({
            required_error: "Transaction amount is required",
            invalid_type_error: "Transaction amount must be a number",
        }).min(1).max(payer.balance, `Transaction amount must be less than or equal to ${payer.balance}`),
        payee: z.coerce.number()
    })
    return (
        <DialogContent>
            <DialogHeader>
                <DialogTitle className="my-2 text-center">New Transaction</DialogTitle>
                <Separator/>
                <NewTransactionForm payer={payer} newTransactionFormSchema={newTransactionFormSchema}/>
            </DialogHeader>
        </DialogContent>
    );
};

export default NewTransactionModal;
