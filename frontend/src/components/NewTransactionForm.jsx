import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormMessage} from "@/components/ui/form.jsx";
import {Button} from "@/components/ui/button.jsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.jsx";
import {Input} from "@/components/ui/input.jsx";
import {Badge} from "@/components/ui/badge.jsx";
import {useSelector} from "react-redux";
import {Separator} from "@/components/ui/separator.jsx";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {addTransaction} from "@/store/api/transactionApi.js";
import {useNavigate, useParams} from "react-router-dom";
import {DialogClose} from "@/components/ui/dialog.jsx";
import {Loader2} from "lucide-react";

export const NewTransactionForm = ({payer, newTransactionFormSchema, onClose}) => {
    const queryClient = useQueryClient();
    const payees = useSelector(state => state.payees);
    const navigate = useNavigate();
    const {id} = useParams();

    const newTransaction = useMutation({
        mutationFn: addTransaction,
        onSuccess: (data) => {
            queryClient.invalidateQueries({
                queryKey: ['game', {id}],
            });
            onClose?.();
            if (data?.game_id) {
                navigate(`/game/${data.game_id}`);
            }
        }
    })

    const form = useForm({
        resolver: zodResolver(newTransactionFormSchema),
        mode: "onChange",
        defaultValues: {
            amount: payer.balance || 0,
        }
    })

    const onSubmit = (values) => {
        const payee = payees.find(p => p.player_id === values.payee);
        if (!payee) return;

        const txn = {
            "game_id": payer.game_id,
            "payer_id": payer.player_id,
            "payer_name": payer.player_name,
            "payee_id": payee.player_id,
            "payee_name": payee.player_name,
            "amount": values.amount,
        }
        newTransaction.mutate(txn);
    }

    return (<>
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)}
                  className="flex flex-col">
                <Badge variant="outline" className='text-xl my-2 border-none justify-center'>{payer.player_name}</Badge>
                <Separator className="text-foreground text-2xl"/>
                <Badge variant="outline" className='text-sm my-2 border-none justify-center'>pays</Badge>
                <Separator className="text-foreground text-2xl my-2"/>
                <FormField
                    control={form.control}
                    name="payee"
                    render={({field}) => {
                        return <FormItem className="my-2">
                            <Select onValueChange={field.onChange}>
                                <FormControl className="justify-center">
                                    <SelectTrigger>
                                        <SelectValue placeholder="Payee"/>
                                    </SelectTrigger>
                                </FormControl>
                                <SelectContent>
                                    {payees.map(payee => {
                                        return <SelectItem key={payee.player_id}
                                                           className="justify-center pl-0"
                                                           value={payee.player_id.toString()}>
                                            {payee.player_name}
                                        </SelectItem>
                                    })}
                                </SelectContent>
                            </Select>
                            <FormMessage className="text-center text-red-500 text-xs"/>
                        </FormItem>
                    }}
                />
                <FormField
                    control={form.control}
                    name="amount"
                    render={({field}) => (
                        <FormItem className="my-2">
                            <FormControl className='text-center'>
                                <Input className='my-1' type="number" {...field} />
                            </FormControl>
                            <FormMessage className="text-center text-red-500 text-xs"/>
                        </FormItem>
                    )}
                />
                {newTransaction.isError && (
                    <div className="text-center text-red-500 text-xs mb-2">
                        {newTransaction.error?.response?.data?.message || "Transaction failed"}
                    </div>
                )}
                <div className="flex gap-2 justify-end mt-2">
                    <DialogClose asChild>
                        <Button variant='ghost' type="button">Cancel</Button>
                    </DialogClose>
                    <Button variant='outline'
                            className='border-2 rounded-xl px-9 py-6 text-[1rem] hover:bg-accent-foreground'
                            type="submit"
                            disabled={newTransaction.isPending}>
                        {newTransaction.isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin"/>}
                        Submit
                    </Button>
                </div>
            </form>
        </Form>
    </>)
}
  