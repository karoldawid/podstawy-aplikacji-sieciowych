// @ts-nocheck
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router-dom';
import { TextField, Button, Container, Select, MenuItem, InputLabel, FormControl, Typography, Stack, Paper, Alert } from '@mui/material';
import { SportsFacilityService } from '../api/SportsFacilityService';
import { RentalService } from '../api/RentalService';

export const RentalForm = () => {
    const { userId } = useParams();
    const navigate = useNavigate();
    const [facilities, setFacilities] = useState([]);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);

    const { register, handleSubmit, formState: { errors } } = useForm();

    useEffect(() => {
        SportsFacilityService.getAll().then(data => {
            setFacilities(data);
        }).catch(err => console.error("Błąd pobierania obiektów", err));
    }, []);

    const onSubmit = async (data) => {
        setErrorMsg(null);

        if (!window.confirm("Czy na pewno chcesz zatwierdzić tę rezerwację?")) {
            return;
        }

        try {
            const rentalRequest = {
                clientId: userId,
                facilityId: data.sportsFacilityId,
                startTime: data.beginTime + ":00",
                endTime: data.endTime + ":00"
            };

            await RentalService.create(rentalRequest);
            alert("Rezerwacja utworzona pomyślnie!");
            navigate(`/users/details/${userId}`);
        } catch (error) {
            console.error("Szczegóły błędu:", error);
            if (error.response) {
                const status = error.response.status;
                const responseData = error.response.data;

                if (status === 409) {
                    setErrorMsg("Konflikt terminów! Ten obiekt jest już zajęty w wybranych godzinach.");
                } else if (status === 400) {
                    let detailedError = "Sprawdź poprawność formularza.";
                    if (responseData.message) detailedError = responseData.message;
                    else if (responseData.errors && Array.isArray(responseData.errors)) detailedError = responseData.errors.map((e: any) => e.defaultMessage).join(", ");
                    setErrorMsg(`Błąd danych (400): ${detailedError}`);
                } else {
                    setErrorMsg(`Wystąpił błąd serwera: ${status}`);
                }
            } else {
                setErrorMsg("Nie udało się połączyć z serwerem.");
            }
        }
    };

    return (
        <Container maxWidth="sm">
            <Typography variant="h5" gutterBottom sx={{ mt: 4 }}>Nowa Rezerwacja</Typography>

            <Paper elevation={3} sx={{ p: 4, mb: 4 }}>
                {errorMsg && (
                    <Alert severity="error" sx={{ mb: 2 }}>
                        {errorMsg}
                    </Alert>
                )}

                <form onSubmit={handleSubmit(onSubmit)}>
                    <Stack spacing={3}>
                        <FormControl fullWidth required variant="filled">
                            <InputLabel>Wybierz Obiekt Sportowy</InputLabel>
                            <Select
                                label="Wybierz Obiekt Sportowy"
                                defaultValue=""
                                {...register("sportsFacilityId", { required: true })}
                            >
                                {facilities.map(facility => (
                                    <MenuItem key={facility.id} value={facility.id}>
                                        {facility.name} (ID: {facility.id})
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        <TextField
                            variant="filled"
                            label="Data i czas rozpoczęcia"
                            type="datetime-local"
                            InputLabelProps={{ shrink: true }}
                            {...register("beginTime", { required: true })}
                        />

                        <TextField
                            variant="filled"
                            label="Data i czas zakończenia"
                            type="datetime-local"
                            InputLabelProps={{ shrink: true }}
                            {...register("endTime", { required: true })}
                        />

                        <Button type="submit" variant="contained" size="large" color="primary">
                            Zarezerwuj
                        </Button>
                        <Button variant="outlined" onClick={() => navigate(`/users/details/${userId}`)}>
                            Anuluj
                        </Button>
                    </Stack>
                </form>
            </Paper>
        </Container>
    );
};