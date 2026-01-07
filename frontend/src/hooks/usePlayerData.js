import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { deletePlayer, getPlayers, updatePlayer } from "@/store/api/playersApi.js";

export const usePlayerData = () => {
    const queryClient = useQueryClient();

    const { data: players = [], isLoading, isError, error } = useQuery({
        queryKey: ['players'],
        queryFn: getPlayers,
    });

    const updateMutation = useMutation({
        mutationFn: ({ id, player }) => updatePlayer({ id, player }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['players'] });
        },
    });

    const deleteMutation = useMutation({
        mutationFn: ({ id }) => deletePlayer({ id }),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['players'] });
        },
    });

    return {
        players,
        isLoading,
        isError,
        error,
        updatePlayer: updateMutation.mutate,
        deletePlayer: deleteMutation.mutate,
        isUpdating: updateMutation.isPending,
        isDeleting: deleteMutation.isPending
    };
};