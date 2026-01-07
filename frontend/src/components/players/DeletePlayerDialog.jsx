import {useEffect, useState} from 'react';
import {Loader2} from 'lucide-react';
import {Button} from '@/components/ui/button';
import {Input} from '@/components/ui/input';
import {Label} from '@/components/ui/label';
import {
    Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle,
} from '@/components/ui/dialog';

export const DeletePlayerDialog = ({open, onOpenChange, player, onConfirm, isPending}) => {
    if (!player) return null;

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Delete Player</DialogTitle>
                    <DialogDescription>
                        {/* The ?. is safe, but the crash was likely in the onConfirm execution context or prop passing */}
                        Are you sure you want to delete {player?.name}? This action cannot be undone.
                    </DialogDescription>
                </DialogHeader>
                <DialogFooter>
                    <Button variant="outline" onClick={() => onOpenChange(false)} disabled={isPending}>
                        Cancel
                    </Button>
                    {/* The error specifically came from reading 'id' of null here */}
                    <Button variant="outline" onClick={() => onConfirm(player.id)} className="text-red-600"
                            disabled={isPending}>
                        {isPending && <Loader2 className="mr-2 h-4 w-4 animate-spin"/>}
                        Delete
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
};