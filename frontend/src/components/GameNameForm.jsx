import {z} from "zod";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {Badge} from "@/components/ui/badge.jsx";
import {Form, FormControl, FormField, FormItem, FormMessage} from "@/components/ui/form.jsx";
import {Input} from "@/components/ui/input.jsx";
import {Button} from "@/components/ui/button.jsx";
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {saveGame} from "@/store/api/gamesApi.js";

const GAME_NAME_LENGTH = 5;

const gameNameSchema = z.object({
    name: z.string()
        .trim()
        .min(GAME_NAME_LENGTH, `Name must contain at least ${GAME_NAME_LENGTH} characters`),
})


const GameNameForm = ({id, name}) => {
    const queryClient = useQueryClient();

    const saveCurrentGame = useMutation({
        mutationFn: saveGame,
        onSuccess: () => {
            queryClient.invalidateQueries(
                {queryKey: ['game']}
            );
        }

    })

    const form = useForm({
        resolver: zodResolver(gameNameSchema),
        mode: "onChange",
        defaultValues: {name}
    })

    const onSubmit = (values) => {
        saveCurrentGame.mutate({id, ...values})
    }

    return (<>
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)}
                  className="flex flex-row lg:justify-center w-full">
                <FormItem className="space-y-0 flex flex-col justify-center">
                    <Badge variant="outline"
                           className='sm:text-base md:text-xl border-none mx-2 px-3 my-0 justify-center'>Name</Badge>
                </FormItem>
                <FormField
                    control={form.control}
                    name="name"
                    render={({field}) => (
                        <div className="flex justify-center space-y-0 grow">
                            <FormItem className='flex flex-col justify-center grow'>
                                <FormControl className='text-center space-y-0'>
                                    <Input className="sm:text-sm md:text-base space-y-0" {...field} />
                                </FormControl>
                                <FormMessage className="text-red-500 text-xs"/>
                            </FormItem>
                        </div>
                    )}
                />
                <FormItem>
                    <Button variant='outline' className='sm:text-sm md:text-base border-2 rounded-xl mx-2 px-4 py-2
                hover:bg-accent-foreground hover:text-background'
                            type="submit" disabled={!form.formState.isValid}>Save</Button>
                </FormItem>
            </form>
        </Form>
    </>);
};

export default GameNameForm;
