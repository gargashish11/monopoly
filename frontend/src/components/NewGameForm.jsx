import {z} from "zod";
import {useFieldArray, useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormMessage} from "@/components/ui/form.jsx";
import {Input} from "@/components/ui/input.jsx";
import {Button} from "@/components/ui/button.jsx";
import {Separator} from "@/components/ui/separator.jsx";
import {Badge} from "@/components/ui/badge.jsx";
import {CircleMinus, CirclePlus} from "lucide-react";
import {useNavigate} from "react-router-dom";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {addGame} from "@/store/api/gamesApi.js";

const PLAYER_NAME_LENGTH = 3;

const formSchema = z.object({
    initialBalance: z.coerce.number({
        required_error: "Starting balance is required",
        invalid_type_error: "Starting balance must be a number",
    }).min(0).max(10000, "Starting balance must be less than or equal to 10000"),
    players: z.array(
        z.object(
            {
                name: z.string()
                    .trim()
                    .min(PLAYER_NAME_LENGTH, `Name must contain at least ${PLAYER_NAME_LENGTH} characters`)
            }
        )
    ).min(3, "Minimum 3 players are required ")
})

const NewGameForm = ({playersData}) => {
    const queryClient = useQueryClient();

    const startNewGame = useMutation({
        mutationFn: addGame,
        onSuccess: () => {
            queryClient.invalidateQueries(
                {queryKey: ['game']}
            );
            queryClient.invalidateQueries(
                {queryKey: ['players']}
            );
        }
    });

    const navigate = useNavigate();

    const form = useForm({
        resolver: zodResolver(formSchema),
        mode: "onChange",
        defaultValues: {
            initialBalance: 1000,
            players: playersData
        }
    })

    const {fields, append, remove} = useFieldArray({
        name: "players",
        control: form.control,
        rules: {minLength: 4, required: true}
    });

    const onSubmit = (values) => {
        startNewGame.mutate(values, {
            onSuccess: (data) => {
                navigate(`/game/${data.data}`)
            }
        })
    }

    const onRemove = (e, index) => {
        e.preventDefault();
        remove(index)
    }
    return (<>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)}>
                    <Badge variant="outline" className='text-xl mb-1 border-none'>Starting Balance</Badge>
                    <FormField
                        control={form.control}
                        name="initialBalance"
                        render={({field}) => (
                            <div className="flex justify-center">
                                <FormItem className='flex flex-col align-middle'>
                                    <FormControl className='text-center w-100'>
                                        <Input className='my-1' {...field} />
                                    </FormControl>
                                    <FormMessage className="text-red-500 text-xs my-2"/>
                                </FormItem>
                            </div>
                        )}
                    />
                    <Separator className='my-1'/>
                    <Badge variant="outline" className='text-2xl my-1 border-none'>Add/Remove Players</Badge>
                    {fields.map((field, index) => {
                        return (
                            <FormField
                                control={form.control}
                                key={field.id}
                                name={`players.${index}.name`}
                                render={({field}) => (
                                    <div className="flex justify-center">
                                        <Button onClick={(e) => onRemove(e, index)}
                                                disabled={field.value === 'Bank'}
                                                className='bg-background mx-4 hover:bg-red-700 px-2 py-1'>
                                            <CircleMinus className='fill-red-700'/>
                                        </Button>
                                        <FormItem>
                                            <FormControl className='text-center w-60'>
                                                <Input disabled={field.value === 'Bank'} className='my-1' {...field}/>
                                            </FormControl>
                                            <FormMessage className="text-red-500 text-xs"/>
                                        </FormItem>
                                        <Button onClick={() => append({id: null, name: ""})}
                                                className='bg-background mx-4 hover:bg-green-700 px-2 py-1'>
                                            <CirclePlus className='fill-green-700'/>
                                        </Button>
                                    </div>
                                )}
                            />
                        )
                    })}
                    <FormItem>
                        <FormMessage></FormMessage>
                        <Button variant='outline' className='border-2 rounded-xl my-2 px-9 py-6 text-[1rem]
                hover:bg-accent-foreground w-80 mx-auto'
                                type="submit" disabled={!form.formState.isValid}>Start Game</Button>
                    </FormItem>
                </form>
            </Form>
        </>
    )
};

export default NewGameForm;
