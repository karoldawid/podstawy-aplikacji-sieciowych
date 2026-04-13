import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button } from '@mui/material';

interface ConfirmDialogProps {
    open: boolean;
    title: string;
    content: string;
    onConfirm: () => void;
    onCancel: () => void;
}

export const ConfirmDialog = ({ open, title, content, onConfirm, onCancel }: ConfirmDialogProps) => {
    return (
        <Dialog open={open} onClose={onCancel}>
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {content}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={onCancel} color="inherit">
                    Anuluj
                </Button>
                <Button onClick={onConfirm} color="error" variant="contained" autoFocus>
                    Potwierdzam
                </Button>
            </DialogActions>
        </Dialog>
    );
};