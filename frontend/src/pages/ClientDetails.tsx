import { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { UserApiClient } from '../api/UserApiClient';
import { RentalApiClient } from '../api/RentalApiClient';
import { SportsFacilityApiClient } from '../api/SportsFacilityApiClient';
import { ConfirmDialog } from '../components/ConfirmDialog';
import type { UserType } from '../model/UserType';
import type { RentalType } from '../model/RentalType';
import {
    Container, Typography, Paper, Grid, Button,
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    CircularProgress, Alert, Chip
} from '@mui/material';

export const ClientDetails = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [user, setUser] = useState<UserType | null>(null);
    const [rentals, setRentals] = useState<RentalType[]>([]);
    const [facilityNames, setFacilityNames] = useState<Record<string, string>>({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [dialogOpen, setDialogOpen] = useState(false);
    const [selectedRentalId, setSelectedRentalId] = useState<string | null>(null);

    // K: rownolegle zapytanie po dane, wypozyczenia + nazwy po id
    const fetchData = useCallback(async () => {
        if (!id) return;
        try {
            const [userResponse, rentalsResponse] = await Promise.all([
                UserApiClient.getById(id),
                RentalApiClient.getByClientId(id)
            ]);

            setUser(userResponse.data);
            const fetchedRentals = rentalsResponse.data;
            setRentals(fetchedRentals);

            const uniqueFacilityIds = Array.from(new Set(fetchedRentals.map(r => r.facilityId)));
            const namesMap: Record<string, string> = {};
            await Promise.all(uniqueFacilityIds.map(async (facId) => {
                try {
                    const facResponse = await SportsFacilityApiClient.getById(facId);
                    namesMap[facId] = facResponse.data.name;
                } catch (e) {
                    namesMap[facId] = "Nieznany obiekt";
                }
            }));
            setFacilityNames(namesMap);

        } catch (err) {
            setError("Nie udało się pobrać danych.");
        } finally {
            setLoading(false);
        }
    }, [id]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const handleEndClick = (rentalId: string) => {
        setSelectedRentalId(rentalId);
        setDialogOpen(true);
    };

    const handleConfirmEnd = async () => {
        if (!selectedRentalId) return;
        try {
            await RentalApiClient.endRental(selectedRentalId);
            await fetchData();
        } catch (e) {
            alert("Nie udało się zakończyć wypożyczenia.");
        } finally {
            setDialogOpen(false);
            setSelectedRentalId(null);
        }
    };

    if (loading) return <Container sx={{ mt: 5 }}><CircularProgress /></Container>;
    if (error) return <Container sx={{ mt: 5 }}><Alert severity="error">{error}</Alert></Container>;
    if (!user) return <Container sx={{ mt: 5 }}><Alert severity="warning">Nie znaleziono</Alert></Container>;

    return (
        <Container sx={{ mt: 5 }}>
            <Paper elevation={3} sx={{ p: 3, mb: 4 }}>
                <Grid container spacing={2}>
                    <Grid item xs={12} sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <Typography variant="h4">Szczegóły Użytkownika</Typography>
                        <Chip
                            label={user.role || "UŻYTKOWNIK"}
                            color="primary"
                            variant="outlined"
                            sx={{ fontWeight: 'bold', fontSize: '1.1rem' }}
                        />
                    </Grid>

                    <Grid item xs={6}>
                        <Typography variant="subtitle2" color="text.secondary">Login:</Typography>
                        <Typography variant="body1">{user.login}</Typography>
                    </Grid>
                    <Grid item xs={6}>
                        <Typography variant="subtitle2" color="text.secondary">Status:</Typography>
                        <Typography color={user.active ? "green" : "red"} fontWeight="bold">
                            {user.active ? "AKTYWNY" : "NIEAKTYWNY"}
                        </Typography>
                    </Grid>
                    <Grid item xs={6}>
                        <Typography variant="subtitle2" color="text.secondary">Imię:</Typography>
                        <Typography variant="body1">{user.firstName}</Typography>
                    </Grid>
                    <Grid item xs={6}>
                        <Typography variant="subtitle2" color="text.secondary">Nazwisko:</Typography>
                        <Typography variant="body1">{user.lastName}</Typography>
                    </Grid>
                </Grid>

                <Button variant="outlined" sx={{ mt: 3 }} onClick={() => navigate('/')}>
                    Wróć do listy
                </Button>
            </Paper>

            <Typography variant="h5" gutterBottom>Historia Wypożyczeń</Typography>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                            <TableCell>Obiekt</TableCell>
                            <TableCell>Start</TableCell>
                            <TableCell>Koniec</TableCell>
                            <TableCell>Akcja</TableCell>
                            <TableCell>ID Wypożyczenia</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rentals.length > 0 ? (
                            rentals.map((rental) => {
                                const isOngoing = new Date(rental.endTime) > new Date();

                                return (
                                    <TableRow key={rental.id}>
                                        <TableCell>
                                            <strong>{facilityNames[rental.facilityId] || "Ładowanie..."}</strong>
                                            <br/>
                                            <span style={{ fontSize: '0.8em', color: 'gray' }}>({rental.facilityId})</span>
                                        </TableCell>
                                        <TableCell>{new Date(rental.startTime).toLocaleString()}</TableCell>
                                        <TableCell>
                                            {new Date(rental.endTime).toLocaleString()}
                                        </TableCell>
                                        <TableCell>
                                            {isOngoing && (
                                                <Button
                                                    variant="contained"
                                                    color="warning"
                                                    size="small"
                                                    onClick={() => handleEndClick(rental.id)}
                                                >
                                                    Zakończ
                                                </Button>
                                            )}
                                        </TableCell>
                                        <TableCell sx={{ color: 'gray', fontSize: '0.8em' }}>{rental.id}</TableCell>
                                    </TableRow>
                                );
                            })
                        ) : (
                            <TableRow>
                                <TableCell colSpan={5} align="center">Brak wypożyczeń</TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </TableContainer>

            <ConfirmDialog
                open={dialogOpen}
                title="Zakończenie wypożyczenia"
                content="Czy na pewno chcesz zakończyć to wypożyczenie?"
                onConfirm={handleConfirmEnd}
                onCancel={() => setDialogOpen(false)}
            />
        </Container>
    );
};