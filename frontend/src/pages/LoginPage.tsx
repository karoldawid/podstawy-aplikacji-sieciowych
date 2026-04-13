import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { Container, Box, TextField, Button, Typography, Alert, Paper } from '@mui/material';
import { AuthApiClient, type AuthRequest } from '../api/AuthApiClient';

export const LoginPage = () => {
    const navigate = useNavigate();
    const [errorMsg, setErrorMsg] = useState<string | null>(null);
    const { register, handleSubmit, formState: { errors } } = useForm<AuthRequest>();

    const onSubmit = async (data: AuthRequest) => {
        setErrorMsg(null);
        try {
            const response = await AuthApiClient.login(data);

            // ODBIERANIE TOKENA WYGENEROWANEGO A AUTHRESTCONTROLLER
            // zapis w localstorage pamieci przegladarki i wyciagnie info
            sessionStorage.setItem('token', response.data.token);
            sessionStorage.setItem('role', response.data.role);
            sessionStorage.setItem('user', JSON.stringify(response.data));

            // pozniej navbar odczytuje i decyduje co pokazac
            const role = response.data.role;
            if (role === 'ADMIN') {
                navigate('/');
            } else {
                navigate('/rentals');
            }

            window.location.reload();
        } catch (e: any) {
            // WYŚWIETLANIE KONKRETNEGO BŁĘDU Z BACKENDU
            const backendError = e.response?.data?.error || e.response?.data?.message;
            setErrorMsg(backendError || "Błędny login lub hasło");
        }
    };

    return (
        <Container maxWidth="xs" sx={{ mt: 10 }}>
            <Paper elevation={3} sx={{ p: 4 }}>
                <Typography variant="h5" align="center" gutterBottom>Logowanie</Typography>

                {errorMsg && <Alert severity="error" sx={{ mb: 2 }}>{errorMsg}</Alert>}

                <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                    <TextField
                        label="Login"
                        fullWidth
                        {...register("login", { required: "Login wymagany" })}
                        error={!!errors.login}
                        helperText={errors.login?.message}
                    />
                    <TextField
                        label="Hasło"
                        type="password"
                        fullWidth
                        {...register("password", { required: "Hasło wymagane" })}
                        error={!!errors.password}
                        helperText={errors.password?.message}
                    />
                    <Button type="submit" variant="contained" size="large" fullWidth>
                        Zaloguj się
                    </Button>

                    <Button
                        variant="text"
                        size="small"
                        fullWidth
                        onClick={() => navigate('/create-client')}
                    >
                        Nie masz konta? Zarejestruj się
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};