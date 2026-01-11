import { useEffect, useState } from 'react';
import { RentalService, type Rental } from '../api/RentalService';
import { UserService, type User } from '../api/UserService';
import { SportsFacilityService, type SportsFacility } from '../api/SportsFacilityService';
import { Container, Typography, Paper, Table, TableHead, TableRow, TableCell, TableBody, Button, Stack, Chip, CircularProgress } from '@mui/material';

export const RentalList = () => {
    const [rentals, setRentals] = useState<Rental[]>([]);
    const [usersMap, setUsersMap] = useState<Record<string, User>>({});
    const [facilitiesMap, setFacilitiesMap] = useState<Record<string, SportsFacility>>({});
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        setLoading(true);
        try {
            const [rentalsData, usersData, facilitiesData] = await Promise.all([
                RentalService.getAll(),
                UserService.getAll(),
                SportsFacilityService.getAll()
            ]);
            const uMap: Record<string, User> = {};
            usersData.forEach(u => uMap[u.id] = u);
            setUsersMap(uMap);

            const fMap: Record<string, SportsFacility> = {};
            facilitiesData.forEach(f => fMap[f.id] = f);
            setFacilitiesMap(fMap);

            const sortedRentals = (rentalsData || []).sort((a, b) =>
                new Date(b.beginTime).getTime() - new Date(a.beginTime).getTime()
            );

            setRentals(sortedRentals);

        } catch (error) {
            console.error("Błąd pobierania danych:", error);
            alert("Nie udało się pobrać listy rezerwacji. Upewnij się, że backend ma zaimplementowaną metodę getAllRentals.");
        } finally {
            setLoading(false);
        }
    };

    const handleEndRental = async (rentalId: string) => {
        if (window.confirm("Czy na pewno chcesz zakończyć tę rezerwację przed czasem?")) {
            try {
                await RentalService.finish(rentalId);
                await loadData();
            } catch (e) {
                alert("Błąd podczas kończenia rezerwacji.");
            }
        }
    };

    const canFinishRental = (endTime: string) => {
        if (!endTime) return true;
        return new Date(endTime) > new Date();
    };

    const formatDate = (dateStr: string) => {
        if (!dateStr) return "-";
        return new Date(dateStr).toLocaleString('pl-PL', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit'
        });
    };

    return (
        <Container maxWidth="lg">
            <Stack direction="row" justifyContent="space-between" alignItems="center" mb={4}>
                <Typography variant="h4">Wszystkie Rezerwacje</Typography>
                <Button onClick={loadData} variant="outlined" size="small">Odśwież</Button>
            </Stack>

            <Paper elevation={3} sx={{ overflow: 'hidden' }}>
                {loading ? (
                    <Stack alignItems="center" sx={{ p: 5 }}>
                        <CircularProgress />
                    </Stack>
                ) : (
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Obiekt Sportowy</TableCell>
                                <TableCell>Klient</TableCell>
                                <TableCell>Początek</TableCell>
                                <TableCell>Koniec</TableCell>
                                <TableCell>Status</TableCell>
                                <TableCell align="center">Akcje</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rentals.length === 0 ? (
                                <TableRow>
                                    <TableCell colSpan={6} align="center">Brak rezerwacji w systemie.</TableCell>
                                </TableRow>
                            ) : (
                                rentals.map((rental) => {
                                    const isActive = canFinishRental(rental.endTime);


                                    const facility = facilitiesMap[rental.facilityId];
                                    const client = usersMap[rental.clientId];

                                    return (
                                        <TableRow key={rental.id} hover>
                                            <TableCell sx={{ fontWeight: 500 }}>
                                                {facility ? facility.name : "Nieznany obiekt"}
                                                <Typography variant="caption" display="block" color="text.secondary">
                                                    {rental.sportsFacility?.name }
                                                </Typography>
                                            </TableCell>
                                            <TableCell>
                                                {client ? (
                                                    <Stack>
                                                        <Typography variant="body2" fontWeight="bold">{client.login}</Typography>
                                                        <Typography variant="caption" color="text.secondary">
                                                            {client.firstName} {client.lastName}
                                                        </Typography>
                                                    </Stack>
                                                ) : (
                                                    <Typography color="error" variant="caption">ID: {rental.clientId}</Typography>
                                                )}
                                            </TableCell>
                                            <TableCell>{formatDate(rental.beginTime)}</TableCell>
                                            <TableCell>
                                                {isActive && !rental.endTime ? (
                                                    <Typography color="primary" variant="body2" fontWeight="bold">W TRAKCIE</Typography>
                                                ) : (
                                                    formatDate(rental.endTime)
                                                )}
                                            </TableCell>
                                            <TableCell>
                                                {isActive ? (
                                                    <Chip label="AKTYWNA" color="success" size="small" variant="outlined" />
                                                ) : (
                                                    <Chip label="ZAKOŃCZONA" color="default" size="small" variant="outlined" />
                                                )}
                                            </TableCell>
                                            <TableCell align="center">
                                                {isActive && (
                                                    <Button
                                                        onClick={() => handleEndRental(rental.id)}
                                                        variant="contained"
                                                        color="error"
                                                        size="small"
                                                    >
                                                        Zakończ
                                                    </Button>
                                                )}
                                            </TableCell>
                                        </TableRow>
                                    );
                                })
                            )}
                        </TableBody>
                    </Table>
                )}
            </Paper>
        </Container>
    );
};