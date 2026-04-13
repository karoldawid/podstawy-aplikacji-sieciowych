import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { Container, TextField, Button, Typography, Paper, Alert, Box } from '@mui/material';
import { UserApiClient } from '../api/UserApiClient';

export const ChangePassword = () => {
    const navigate = useNavigate();
    const { register, handleSubmit, watch, formState: { errors } } = useForm();
    const [errorMsg, setErrorMsg] = useState<string | null>(null);
    const [successMsg, setSuccessMsg] = useState<string | null>(null);

    const newPassword = watch("newPassword");

    const onSubmit = async (data: any) => {
        setErrorMsg(null);
        try {
            await UserApiClient.changePassword({
                oldPassword: data.oldPassword,
                newPassword: data.newPassword
            });
            setSuccessMsg("Hasło zostało zmienione pomyślnie! Za chwilę nastąpi wylogowanie...");

            // wylogowanie po zmianie
            setTimeout(() => {
                sessionStorage.clear();
                window.location.href = '/login';
            }, 2000);

        } catch (e: any) {
            setErrorMsg(e.response?.data?.message || "Błąd zmiany hasła. Sprawdź stare hasło.");
        }
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 4 }}>
            <Paper elevation={3} sx={{ p: 4 }}>
                <Typography variant="h5" gutterBottom>Zmiana Hasła</Typography>

                {errorMsg && <Alert severity="error" sx={{ mb: 2 }}>{errorMsg}</Alert>}
                {successMsg && <Alert severity="success" sx={{ mb: 2 }}>{successMsg}</Alert>}

                <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>

                    <TextField
                        label="Stare hasło"
                        type="password"
                        {...register("oldPassword", { required: "Podaj stare hasło" })}
                        error={!!errors.oldPassword}
                        helperText={errors.oldPassword?.message as string}
                    />

                    <TextField
                        label="Nowe hasło"
                        type="password"
                        {...register("newPassword", {
                            required: "Podaj nowe hasło",
                            minLength: { value: 4, message: "Minimum 4 znaki" }
                        })}
                        error={!!errors.newPassword}
                        helperText={errors.newPassword?.message as string}
                    />

                    <TextField
                        label="Potwierdź nowe hasło"
                        type="password"
                        {...register("confirmPassword", {
                            validate: value => value === newPassword || "Hasła nie są identyczne"
                        })}
                        error={!!errors.confirmPassword}
                        helperText={errors.confirmPassword?.message as string}
                    />

                    <Button type="submit" variant="contained" size="large" disabled={!!successMsg}>
                        Zmień hasło
                    </Button>
                    <Button variant="outlined" onClick={() => navigate('/')}>
                        Anuluj
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};