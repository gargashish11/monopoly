import {useState} from 'react';
import {Loader2} from 'lucide-react';
import {Button} from '@/components/ui/button';
import {Input} from '@/components/ui/input';
import {Label} from '@/components/ui/label';
import {
    Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle,
} from '@/components/ui/dialog';

const EditPlayerContent = ({player, onOpenChange, onSave, isPending}) => {
    const [name, setName] = useState(player?.name || '');

    const handleSave = () => {
        if (name.trim()) onSave(player.id, name.trim());
    };

    return (
        <DialogContent>
            <DialogHeader>
                <DialogTitle>Edit Player</DialogTitle>
                <DialogDescription>Update the player's name below.</DialogDescription>
            </DialogHeader>
            <div className="grid gap-4 py-4">
                <div className="grid gap-2">
                    <Label htmlFor="name">Name</Label>
                    <Input
                        id="name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Enter player name"
                    />
                </div>
            </div>
            <DialogFooter>
                <Button variant="outline" onClick={() => onOpenChange(false)} disabled={isPending}>
                    Cancel
                </Button>
                <Button variant="outline" onClick={handleSave} disabled={!name.trim() || isPending}>
                    {isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin"/>}
                    Save Changes
                </Button>
            </DialogFooter>
        </DialogContent>
    );
};

export const EditPlayerDialog = ({open, onOpenChange, player, onSave, isPending}) => {
    if (!player) return null;

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            {open && (
                <EditPlayerContent
                    key={player.id}
                    player={player}
                    onOpenChange={onOpenChange}
                    onSave={onSave}
                    isPending={isPending}
                />
            )}
        </Dialog>
    );
};
