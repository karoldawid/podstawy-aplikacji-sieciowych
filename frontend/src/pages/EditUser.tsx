import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import type { UpdateUserType } from '../model/UpdateUserType';
import { UserApiClient } from '../api/UserApiClient';
import { ConfirmDialog } from '../components/ConfirmDialog';
import { TextField, Button, Container, Typography, Box, Alert, CircularProgress } from '@mui/material';

export const EditUser = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [errorMsg, setErrorMsg] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    const [dialogOpen, setDialogOpen] = useState(false);
    const [pendingData, setPendingData] = useState<UpdateUserType | null>(null);

    const [etag, setEtag] = useState<string | null>(null);

    const { register, handleSubmit, formState: { errors, isValid }, reset } = useForm<UpdateUserType>({
        mode: 'onBlur'
    });

    useEffect(() => {
        const loadUser = async () => {
            if (!id) return;
            try {
                const response = await UserApiClient.getById(id);

                reset({
                    firstName: response.data.firstName,
                    lastName: response.data.lastName
                });

                const signature = response.headers['etag'] || response.headers['ETag'];

                console.log("--- DEBUG ETAG ---");
                console.log("Wszystkie nagłówki:", response.headers);
                console.log("Znaleziony ETag:", signature);
                console.log("------------------");

                if (signature) {
                    setEtag(signature);
                } else {
                    console.warn("UWAGA: Nie otrzymano nagłówka ETag! Sprawdź CORS w Backendzie.");
                }

            } catch (error) {
                console.error("Błąd pobierania:", error);
                setErrorMsg("Nie udało się pobrać danych użytkownika.");
            } finally {
                setLoading(false);
            }
        };
        loadUser();
    }, [id, reset]);

    const onSubmit = (data: UpdateUserType) => {
        setPendingData(data);
        setDialogOpen(true);
    };

    const handleConfirmSave = async () => {
        if (!id || !pendingData) return;

        setErrorMsg(null);

        try {
            await UserApiClient.updateUser(id, pendingData, etag);
            navigate('/');
        } catch (error: any) {
            console.error("Błąd edycji:", error);

            if (error.response && error.response.status === 412) {
                setErrorMsg("BŁĄD DANYCH: Ktoś inny zmodyfikował tego użytkownika w międzyczasie! Twoje dane są nieaktualne. Odśwież stronę.");
            } else {
                setErrorMsg(error.response?.data?.message || "Błąd podczas edycji.");
            }
        } finally {
            setDialogOpen(false);
        }
    };

    if (loading) return <Container sx={{ mt: 5 }}><CircularProgress /></Container>;

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>Edycja Użytkownika</Typography>

            {errorMsg && <Alert severity="error" sx={{ mb: 2 }}>{errorMsg}</Alert>}

            <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>

                <TextField
                    label="Imię"
                    variant="outlined"
                    InputLabelProps={{ shrink: true }}
                    {...register("firstName", { required: "Imię jest wymagane" })}
                    error={!!errors.firstName}
                    helperText={errors.firstName?.message}
                />

                <TextField
                    label="Nazwisko"
                    variant="outlined"
                    InputLabelProps={{ shrink: true }}
                    {...register("lastName", {
                        required: "Nazwisko jest wymagane",
                        minLength: { value: 2, message: "Min. 2 znaki" }
                    })}
                    error={!!errors.lastName}
                    helperText={errors.lastName?.message}
                />

                <Button type="submit" variant="contained" size="large" disabled={!isValid}>
                    Zapisz Zmiany
                </Button>

                <Button variant="outlined" onClick={() => navigate('/')}>
                    Anuluj
                </Button>
            </Box>

            <ConfirmDialog
                open={dialogOpen}
                title="Potwierdź edycję"
                content="Czy na pewno chcesz zapisać zmiany w danych użytkownika?"
                onConfirm={handleConfirmSave}
                onCancel={() => setDialogOpen(false)}
            />
        </Container>
    );
};