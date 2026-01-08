import { Edit, Trash2 } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Avatar, AvatarFallback } from '@/components/ui/avatar';

export const PlayerListItem = ({ player, onEdit, onDelete }) => {
    const getInitials = (name) => name.substring(0, 1).toUpperCase();

    return (
        <div className="flex items-center justify-between p-4 rounded-lg border bg-card hover:bg-accent transition-colors">
            <div className="flex items-center gap-4">
                <Avatar>
                    <AvatarFallback className="bg-primary text-primary-foreground">
                        {getInitials(player.name)}
                    </AvatarFallback>
                </Avatar>
                <div>
                    <h3 className="font-semibold text-base">{player.name}</h3>
                </div>
            </div>
            <div className="flex items-center gap-2">
                <Button variant="ghost" size="icon" onClick={() => onEdit(player)}>
                    <Edit className="h-4 w-4" />
                </Button>
                <Button variant="ghost" size="icon" onClick={() => onDelete(player)}>
                    <Trash2 className="h-4 w-4 text-destructive" />
                </Button>
            </div>
        </div>
    );
};