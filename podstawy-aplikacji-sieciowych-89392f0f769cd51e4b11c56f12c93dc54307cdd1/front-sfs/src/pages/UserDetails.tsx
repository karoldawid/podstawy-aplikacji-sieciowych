// @ts-nocheck
import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Container, Card, CardContent, Typography, Button, Table, TableHead, TableRow, TableCell, TableBody, Paper, Stack, Divider } from '@mui/material';
import { UserService } from '../api/UserService';
import { RentalService } from '../api/RentalService';
import { SportsFacilityService } from '../api/SportsFacilityService';

interface SportsFacility { id: string; name: string; }
interface Rental { id: string; beginTime: string; endTime: string; facilityId: string; sportsFacility?: SportsFacility; }
interface User { id: string; login: string; firstName: string; lastName: string; isActive?: boolean; active?: boolean; phoneNumber?: string; accessLevel?: string; role?: string; level?: string; }

export const UserDetails = () => {
    const { id } = useParams();
    const [user, setUser] = useState<User | null>(null);
    const [rentals, setRentals] = useState<Rental[]>([]);
    const [facilities, setFacilities] = useState<SportsFacility[]>([]);

    useEffect(() => { if (id) loadData(); }, [id]);

    const loadData = async () => {
        try {
            const userData = await UserService.getById(id!);
            setUser(userData as any);
            const userRole = userData.accessLevel || userData.role || userData.level;

            if (userRole === 'CLIENT' || (!userRole && userData.phoneNumber)) {
                try {
                    const rentalsData = await RentalService.getByClientId(id!);
                    setRentals(rentalsData as any);
                    const facilitiesData = await SportsFacilityService.getAll();
                    setFacilities(facilitiesData as any);
                } catch (e) { console.log("Brak rezerwacji"); }
            }
        } catch (error) { console.error("Błąd", error); }
    };

    const handleEndRental = async (rentalId: string) => {
        if (window.confirm("Czy na pewno chcesz zakończyć tę rezerwację przed czasem?")) {
            try {
                await RentalService.finish(rentalId);
                loadData();
            } catch (e) {
                alert("Błąd podczas kończenia rezerwacji.");
            }
        }
    };

    const canFinishRental = (endTime: string) => {
        if (!endTime) return true;
        return new Date(endTime) > new Date();
    };

    const getFacilityName = (facilityId: string) => {
        const facility = facilities.find(f => f.id === facilityId);
        return facility ? facility.name : 'Nieznany obiekt';
    };

    if (!user) return <Container sx={{mt: 4, textAlign: 'center'}}>Ładowanie...</Container>;

    const active = user.active === true || user.isActive === true;
    const role = user.accessLevel || user.role || user.level || 'Nieznana';

    return (
        <Container maxWidth="md">
            <Card elevation={3} sx={{ mb: 4, mt: 4 }}>
                <CardContent sx={{ p: 4 }}>
                    <Typography variant="h4" gutterBottom>Szczegóły Użytkownika</Typography>
                    <Divider sx={{ mb: 3 }} />

                    <Stack spacing={1}>
                        <Typography variant="h6">Imię i Nazwisko: <b style={{ color: '#90caf9' }}>{user.firstName} {user.lastName}</b></Typography>
                        <Typography>Login: {user.login}</Typography>
                        <Typography>Rola: <b>{role}</b></Typography>
                        <Typography>Status: <span style={{color: active ? '#66bb6a' : '#ef5350'}}>{active ? 'AKTYWNY' : 'ZABLOKOWANY'}</span></Typography>
                        {user.phoneNumber && <Typography>Telefon: {user.phoneNumber}</Typography>}
                    </Stack>

                    <Stack direction="row" spacing={2} sx={{ mt: 4 }}>
                        <Button component={Link} to={`/users/edit/${user.id}`} variant="contained">Edytuj Dane</Button>
                        <Button component={Link} to="/users" variant="outlined">Powrót</Button>
                    </Stack>
                </CardContent>
            </Card>

            {((role === 'CLIENT') || user.phoneNumber) && (
                <Paper elevation={3} sx={{ p: 3, mb: 4 }}>
                    <Stack direction="row" justifyContent="space-between" alignItems="center" mb={3}>
                        <Typography variant="h5">Lista Rezerwacji</Typography>
                        <Button component={Link} to={`/rentals/create/${user.id}`} variant="contained" color="secondary">
                            Nowa Rezerwacja
                        </Button>
                    </Stack>

                    {rentals.length === 0 ? (
                        <Typography color="text.secondary" align="center">Brak historii rezerwacji.</Typography>
                    ) : (
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Obiekt</TableCell>
                                    <TableCell>Data Rozpoczęcia</TableCell>
                                    <TableCell>Data Zakończenia</TableCell>
                                    <TableCell>Akcje</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {rentals.map((rental) => (
                                    <TableRow key={rental.id}>
                                        <TableCell>{rental.sportsFacility?.name || getFacilityName(rental.facilityId)}</TableCell>
                                        <TableCell>{new Date(rental.beginTime).toLocaleString()}</TableCell>
                                        <TableCell>
                                            {canFinishRental(rental.endTime)
                                                ? (rental.endTime ? new Date(rental.endTime).toLocaleString() : <b style={{color: '#66bb6a'}}>W TRAKCIE</b>)
                                                : <span style={{opacity: 0.6}}>{new Date(rental.endTime).toLocaleString()}</span>
                                            }
                                        </TableCell>
                                        <TableCell>
                                            {canFinishRental(rental.endTime) && (
                                                <Button onClick={() => handleEndRental(rental.id)} color="error" variant="outlined" size="small">
                                                    Zakończ
                                                </Button>
                                            )}
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    )}
                </Paper>
            )}
        </Container>
    );
};