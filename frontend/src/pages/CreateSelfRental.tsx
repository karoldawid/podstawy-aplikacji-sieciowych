import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { Container, TextField, Button, Typography, MenuItem, Paper, Alert, Box } from '@mui/material';
import { SportsFacilityApiClient } from '../api/SportsFacilityApiClient';
import { RentalApiClient } from '../api/RentalApiClient';

interface SelfRentalForm {
    facilityId: string;
    startTime: string;
    endTime: string;
}

export const CreateSelfRental = () => {
    const navigate = useNavigate();
    const { register, handleSubmit, formState: { errors } } = useForm<SelfRentalForm>();

    const [facilities, setFacilities] = useState<any[]>([]);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);

    useEffect(() => {
        SportsFacilityApiClient.getAll()
            .then(res => setFacilities(res.data))
            .catch(err => console.error(err));
    }, []);

    const onSubmit = async (data: SelfRentalForm) => {
        try {
            await RentalApiClient.rentSelf({
                facilityId: data.facilityId,
                startTime: data.startTime,
                endTime: data.endTime
            });
            navigate('/rentals');
        } catch (e: any) {
            setErrorMsg("Błąd rezerwacji: " + (e.response?.data?.message || "Sprawdź dostępność terminu"));
        }
    };

    const getFacilityLabel = (fac: any) => {
        return `${fac.name}`;
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 4 }}>
            <Paper elevation={3} sx={{ p: 4 }}>
                <Typography variant="h5" gutterBottom>Zarezerwuj Obiekt</Typography>

                {errorMsg && <Alert severity="error" sx={{ mb: 2 }}>{errorMsg}</Alert>}

                <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>

                    <TextField
                        select
                        label="Wybierz obiekt"
                        defaultValue=""
                        {...register("facilityId", { required: "Wybierz obiekt" })}
                        error={!!errors.facilityId}
                        helperText={errors.facilityId?.message}
                    >
                        {facilities.map((fac) => (
                            <MenuItem key={fac.id} value={fac.id}>
                                {getFacilityLabel(fac)}
                            </MenuItem>
                        ))}
                    </TextField>

                    <TextField
                        label="Początek"
                        type="datetime-local"
                        InputLabelProps={{ shrink: true }}
                        {...register("startTime", { required: "Podaj start" })}
                    />

                    <TextField
                        label="Koniec"
                        type="datetime-local"
                        InputLabelProps={{ shrink: true }}
                        {...register("endTime", { required: "Podaj koniec" })}
                    />

                    <Button type="submit" variant="contained" size="large">
                        Zarezerwuj
                    </Button>
                    <Button variant="outlined" onClick={() => navigate('/rentals')}>
                        Anuluj
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};