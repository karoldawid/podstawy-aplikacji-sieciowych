import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { RentalApiClient } from '../api/RentalApiClient';
import type { RentalType } from '../model/RentalType';
import { ConfirmDialog } from '../components/ConfirmDialog';
import {
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Paper, Typography, Container, Button, Box, Chip
} from '@mui/material';

export const RentalList = () => {
    const navigate = useNavigate();
    const [rentals, setRentals] = useState<RentalType[]>([]);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [selectedRentalId, setSelectedRentalId] = useState<string | null>(null);

    const userRole = sessionStorage.getItem('role');

    const fetchRentals = async () => {
        try {
            let response;
            if (userRole === 'CLIENT') {
                response = await RentalApiClient.getSelfRentals();
            } else {
                response = await RentalApiClient.getAll();
            }
            setRentals(response.data);
        } catch (error) {
            console.error("Błąd pobierania rezerwacji:", error);
        }
    };

    useEffect(() => {
        fetchRentals();
    }, []);

    const handleEndClick = (id: string) => {
        setSelectedRentalId(id);
        setDialogOpen(true);
    };

    const handleConfirmEnd = async () => {
        if (!selectedRentalId) return;
        try {
            await RentalApiClient.endRental(selectedRentalId);
            fetchRentals();
        } catch (e) {
            alert("Nie udało się zakończyć wypożyczenia.");
        } finally {
            setDialogOpen(false);
            setSelectedRentalId(null);
        }
    };

    return (
        <Container sx={{ mt: 4 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                <Typography variant="h4">
                    {userRole === 'CLIENT' ? 'Moje wypożyczenia' : 'Wszystkie wypożyczenia'}
                </Typography>

                {userRole === 'CLIENT' && (
                    <Button variant="contained" onClick={() => navigate('/rent-facility')}>
                        + Zarezerwuj Obiekt
                    </Button>
                )}

                {(userRole === 'ADMIN' || userRole === 'FACILITYMANAGER') && (
                    <Button variant="contained" color="secondary" onClick={() => navigate('/create-rental')}>
                        + Utwórz Wypożyczenie (Admin)
                    </Button>
                )}
            </Box>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                            <TableCell>ID Klienta</TableCell>
                            <TableCell>ID Obiektu</TableCell>
                            <TableCell>Start</TableCell>
                            <TableCell>Koniec / Akcja</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rentals.map((rental) => {
                            const isActive = new Date(rental.endTime) > new Date();
                            return (
                                <TableRow key={rental.id}>
                                    <TableCell>{rental.clientId}</TableCell>
                                    <TableCell>{rental.facilityId}</TableCell>
                                    <TableCell>{new Date(rental.startTime).toLocaleString()}</TableCell>
                                    <TableCell>
                                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                                            {new Date(rental.endTime).toLocaleString()}
                                            {isActive ? (
                                                <Button
                                                    variant="contained" color="warning" size="small"
                                                    onClick={() => handleEndClick(rental.id)}
                                                >
                                                    Zakończ
                                                </Button>
                                            ) : (
                                                <Chip label="Zakończone" size="small" color="default" variant="outlined" />
                                            )}
                                        </Box>
                                    </TableCell>
                                </TableRow>
                            );
                        })}
                    </TableBody>
                </Table>
            </TableContainer>

            <ConfirmDialog
                open={dialogOpen}
                title="Zakończenie wypożyczenia"
                content="Czy na pewno chcesz zakończyć to wypożyczenie przed czasem?"
                onConfirm={handleConfirmEnd}
                onCancel={() => setDialogOpen(false)}
            />
        </Container>
    );
};