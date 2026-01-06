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

export const NewTransactionForm = ({payer, newTransactionFormSchema}) => {
    const queryClient = useQueryClient();
    const payees = useSelector(state => state.payees);
    const navigate = useNavigate();
    const {id} = useParams();

    const newTransaction = useMutation({
        mutationFn: addTransaction,
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ['game', {id}],
            })
        }
    })

    const form = useForm({
        resolver: zodResolver(newTransactionFormSchema),
        mode: "onChange",
        defaultValues: {
            amount: payer.balance,
        }
    })

    const onSubmit = (values) => {
        const [payee] = payees.filter(payee => payee.player_id === values.payee)
        const txn = {
            "game_id": payer.game_id,
            "payer_id": payer.player_id,
            "payer_name": payer.player_name,
            "payee_id": payee.player_id,
            "payee_name": payee.player_name,
            "amount": values.amount,
        }
        newTransaction.mutate(txn, {
            onSuccess: (data) => {
                navigate(`/game/${data.id}`)
            }
        })
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
                            <FormMessage/>
                        </FormItem>
                    }}
                />
                <FormField
                    control={form.control}
                    name="amount"
                    render={({field}) => (
                        <FormItem className="my-2">
                            <FormControl className='text-center'>
                                <Input className='my-1' {...field} />
                            </FormControl>
                            <FormMessage className="text-center text-red-500 text-xs"/>
                        </FormItem>
                    )}
                />
                <DialogClose asChild>
                    <Button type="submit">Submit</Button>
                </DialogClose>
            </form>
        </Form>
    </>)
}
  