import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {
    Container, Typography, Box, Button, TextField, MenuItem, CircularProgress, Alert
} from '@mui/material';

import { UserApiClient } from '../api/UserApiClient';
import { SportsFacilityApiClient } from '../api/SportsFacilityApiClient';
import { RentalApiClient } from '../api/RentalApiClient';
import { ConfirmDialog } from '../components/ConfirmDialog';

import type { UserType } from '../model/UserType';
import type { SportsFacilityType } from '../model/SportsFacilityType';
import type { CreateRentalType } from '../model/CreateRentalType';

export const CreateRental = () => {
    const navigate = useNavigate();
    const [clients, setClients] = useState<UserType[]>([]);
    const [facilities, setFacilities] = useState<SportsFacilityType[]>([]);
    const [loading, setLoading] = useState(true);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);

    const [dialogOpen, setDialogOpen] = useState(false);
    const [pendingData, setPendingData] = useState<CreateRentalType | null>(null);

    const { register, handleSubmit, watch, formState: { errors } } = useForm<CreateRentalType>();


    const startTimeValue = watch("startTime");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [usersResp, facilitiesResp] = await Promise.all([
                    UserApiClient.getAll(),
                    SportsFacilityApiClient.getAll()
                ]);

                const activeClients = usersResp.data.filter(u =>
                    u.active && u.role === 'CLIENT'
                );

                setClients(activeClients);
                setFacilities(facilitiesResp.data);
            } catch (e) {
                setErrorMsg("Nie udało się pobrać danych.");
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const onSubmit = (data: CreateRentalType) => {
        setPendingData(data);
        setDialogOpen(true);
    };

    const handleConfirmCreate = async () => {
        if (!pendingData) return;
        try {
            await RentalApiClient.create(pendingData);
            navigate('/rentals');
        } catch (e: any) {
            const message = e.response?.data?.message || "Błąd tworzenia wypożyczenia. Sprawdź dostępność obiektu.";
            setErrorMsg(message);
        } finally {
            setDialogOpen(false);
        }
    };

    if (loading) return <Container sx={{ mt: 5 }}><CircularProgress /></Container>;

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>Nowe Wypożyczenie</Typography>

            {errorMsg && <Alert severity="error" sx={{ mb: 2 }}>{errorMsg}</Alert>}

            <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>

                <TextField
                    select
                    label="Klient"
                    defaultValue=""
                    inputProps={register('clientId', { required: "Wybierz klienta" })}
                    error={!!errors.clientId}
                    helperText={errors.clientId?.message}
                >
                    {clients.map((client) => (
                        <MenuItem key={client.id} value={client.id}>
                            {client.login} ({client.firstName} {client.lastName})
                        </MenuItem>
                    ))}
                </TextField>

                <TextField
                    select
                    label="Obiekt Sportowy"
                    defaultValue=""
                    inputProps={register('facilityId', { required: "Wybierz obiekt" })}
                    error={!!errors.facilityId}
                    helperText={errors.facilityId?.message}
                >
                    {facilities.map((fac) => (
                        <MenuItem key={fac.id} value={fac.id}>
                            {fac.name} (ID: {fac.id})
                        </MenuItem>
                    ))}
                </TextField>

                <TextField
                    label="Data i czas rozpoczęcia"
                    type="datetime-local"
                    InputLabelProps={{ shrink: true }}
                    {...register("startTime", {
                        required: "Data startu jest wymagana",
                        validate: (value) => {
                            const selectedDate = new Date(value);
                            const now = new Date();
                            if (selectedDate < now) {
                                return "Data rozpoczęcia musi być w przyszłości!";
                            }
                            return true;
                        }
                    })}
                    error={!!errors.startTime}
                    helperText={errors.startTime?.message}
                />

                <TextField
                    label="Data i czas zakończenia"
                    type="datetime-local"
                    InputLabelProps={{ shrink: true }}
                    {...register("endTime", {
                        required: "Data końca jest wymagana",
                        validate: (value) => {
                            const endDate = new Date(value);
                            const startDate = new Date(startTimeValue);
                            const now = new Date();

                            if (endDate < now) {
                                return "Data zakończenia musi być w przyszłości!";
                            }

                            if (startTimeValue && endDate <= startDate) {
                                return "Koniec musi być później niż początek!";
                            }

                            return true;
                        }
                    })}
                    error={!!errors.endTime}
                    helperText={errors.endTime?.message}
                />

                <Button type="submit" variant="contained" size="large">
                    Zarezerwuj
                </Button>

                <Button variant="outlined" onClick={() => navigate('/rentals')}>
                    Anuluj
                </Button>
            </Box>

            <ConfirmDialog
                open={dialogOpen}
                title="Potwierdź rezerwację"
                content="Czy na pewno chcesz utworzyć nowe wypożyczenie?"
                onConfirm={handleConfirmCreate}
                onCancel={() => setDialogOpen(false)}
            />
        </Container>
    );
};