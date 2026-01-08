import {useState} from 'react';
import {Loader2, Search, User} from 'lucide-react';
import {Input} from '@/components/ui/input';
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from '@/components/ui/card';

import {usePlayerData} from '@/hooks/usePlayerData';
import {PlayerListItem} from "@/components/players/PlayerListItem.jsx";
import {EditPlayerDialog} from "@/components/players/EditPlayerDialog.jsx";
import {DeletePlayerDialog} from "@/components/players/DeletePlayerDialog.jsx";

const PlayerList = () => {
    // 1. Data Layer
    const {
        players, isLoading, isError, error,
        updatePlayer, deletePlayer, isUpdating, isDeleting
    } = usePlayerData();

    // 2. Local UI State
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedPlayer, setSelectedPlayer] = useState(null);
    const [activeModal, setActiveModal] = useState(null); // 'edit' | 'delete' | null

    // 3. Handlers
    const handleEditOpen = (player) => {
        setSelectedPlayer(player);
        setActiveModal('edit');
    };

    const handleDeleteOpen = (player) => {
        setSelectedPlayer(player);
        setActiveModal('delete');
    };

    const handleCloseModal = () => {
        setActiveModal(null);
        setSelectedPlayer(null);
    };

    const onEditConfirm = (id, name) => {
        updatePlayer({id, player: {id, name}}, {onSuccess: handleCloseModal});
    };

    const onDeleteConfirm = (id) => {
        deletePlayer({id}, {onSuccess: handleCloseModal});
    };

    // 4. Render Logic
    if (isLoading) return <LoadingState/>;
    if (isError) return <ErrorState message={error.message}/>;

    const filteredPlayers = players.filter(player =>
        player.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="w-full max-w-4xl mx-auto p-4">
            <Card>
                <CardHeader>
                    <CardTitle className="text-2xl">Players</CardTitle>
                    <CardDescription>Manage your player roster ({players.length} total players)</CardDescription>
                </CardHeader>
                <CardContent>
                    {/* Search Bar */}
                    <div className="mb-6 relative">
                        <Search
                            className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4"/>
                        <Input
                            type="text"
                            placeholder="Search by name..."
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className="pl-10"
                        />
                    </div>

                    {/* Player List */}
                    <div className="space-y-3">
                        {filteredPlayers.map((player) => (
                            <PlayerListItem
                                key={player.id}
                                player={player}
                                onEdit={handleEditOpen}
                                onDelete={handleDeleteOpen}
                            />
                        ))}
                    </div>

                    {/* Empty State */}
                    {filteredPlayers.length === 0 && (
                        <div className="text-center py-12">
                            <User className="mx-auto h-12 w-12 text-muted-foreground mb-4"/>
                            <h3 className="font-semibold text-lg mb-2">No players found</h3>
                            <p className="text-muted-foreground">Try adjusting your search terms</p>
                        </div>
                    )}
                </CardContent>
            </Card>

            {/* Dialogs */}
            <EditPlayerDialog
                open={activeModal === 'edit'}
                onOpenChange={(open) => !open && handleCloseModal()}
                player={selectedPlayer}
                onSave={onEditConfirm}
                isPending={isUpdating}
            />

            <DeletePlayerDialog
                open={activeModal === 'delete'}
                onOpenChange={(open) => !open && handleCloseModal()}
                player={selectedPlayer}
                onConfirm={onDeleteConfirm}
                isPending={isDeleting}
            />
        </div>
    );
};

// Simple internal components for clean main render
const LoadingState = () => (
    <div className="w-full max-w-4xl mx-auto p-4">
        <Card><CardContent className="pt-6 flex flex-col items-center justify-center py-12">
            <Loader2 className="h-8 w-8 animate-spin text-primary mb-4"/>
            <p className="text-muted-foreground">Loading players...</p>
        </CardContent></Card>
    </div>
);

const ErrorState = ({message}) => (
    <div className="w-full max-w-4xl mx-auto p-4">
        <Card><CardContent className="pt-6 flex flex-col items-center justify-center py-12">
            <p className="text-destructive font-semibold mb-2">Error loading players</p>
            <p className="text-muted-foreground text-sm">{message}</p>
        </CardContent></Card>
    </div>
);

export default PlayerList;