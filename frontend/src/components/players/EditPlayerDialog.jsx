import {useEffect, useState} from 'react';
import {Loader2} from 'lucide-react';
import {Button} from '@/components/ui/button';
import {Input} from '@/components/ui/input';
import {Label} from '@/components/ui/label';
import {
    Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle,
} from '@/components/ui/dialog';

export const EditPlayerDialog = ({open, onOpenChange, player, onSave, isPending}) => {
    const [name, setName] = useState('');

    useEffect(() => {
        if (player) setName(player.name);
    }, [player, open]);

    const handleSave = () => {
        if (name.trim()) onSave(player.id, name.trim());
    };

    if (!player) return null;

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent>
                {/* ... (rest of the code is the same) ... */}
                {/* Just ensure you are editing the Return statement logic if needed,
                   but the guard clause at the top handles it. */}
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
        </Dialog>
    );
};
