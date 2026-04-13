import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import type { CreateClientType } from "../model/CreateClientType";
import { UserApiClient } from "../api/UserApiClient";
import { ConfirmDialog } from "../components/ConfirmDialog";
import { Alert, Box, Button, Container, TextField, Typography, MenuItem } from "@mui/material";

export const CreateUser = () => {
    const navigate = useNavigate();
    const [errorMsg, setErrorMsg] = useState<string | null>(null);

    const [dialogOpen, setDialogOpen] = useState(false);
    const [pendingData, setPendingData] = useState<CreateClientType | null>(null);

    const [selectedRole, setSelectedRole] = useState("CLIENT");

    const [isAdmin, setIsAdmin] = useState(false);

    useEffect(() => {
        const role = sessionStorage.getItem('role');
        setIsAdmin(role === 'ADMIN');
    }, []);

    const { register, handleSubmit, formState: { errors, isValid } } = useForm<CreateClientType>({
        mode: 'onBlur'
    });

    const onSubmit = (data: CreateClientType) => {
        setPendingData(data);
        setDialogOpen(true);
    };

    const handleConfirmCreate = async () => {
        if (!pendingData) return;
        try {
            if (selectedRole === "ADMIN") {
                await UserApiClient.createAdmin(pendingData);
            } else if (selectedRole === "MANAGER") {
                await UserApiClient.createManager(pendingData);
            } else {
                await UserApiClient.createClient(pendingData);
            }
            if (!isAdmin) {
                navigate('/login');
            } else {
                navigate('/');
            }
        } catch (error: any) {
            if (error.response && error.response.data && error.response.data.message) {
                setErrorMsg(error.response.data.message);
            } else if (error.response?.status === 409) {
                setErrorMsg("Ten login jest już zajęty. Wybierz inny.");
            } else {
                setErrorMsg("Wystąpił problem z połączeniem z serwerem. Spróbuj później.");
            }
        } finally {
            setDialogOpen(false);
        }
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>
                {isAdmin ? "Kreator Użytkownika (Admin)" : "Rejestracja"}
            </Typography>

            {errorMsg && <Alert severity="error" sx={{ mb: 2 }}>{errorMsg}</Alert>}

            <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>

                {isAdmin && (
                    <TextField
                        select
                        label="Rola w systemie"
                        value={selectedRole}
                        onChange={(e) => setSelectedRole(e.target.value)}
                        helperText="Wybierz poziom uprawnień"
                    >
                        <MenuItem value="CLIENT">Klient</MenuItem>
                        <MenuItem value="ADMIN">Administrator</MenuItem>
                        <MenuItem value="MANAGER">Zarządca (Facility Manager)</MenuItem>
                    </TextField>
                )}

                <TextField
                    label="Login"
                    variant="outlined"
                    {...register("login", {
                        required: "Pole login nie może być puste",
                        minLength: { value: 3, message: "Za krótki login (min. 3 znaki)" },
                        maxLength: { value: 20, message: "Za długi login (max. 20 znaków)" },
                        pattern: {
                            value: /^[a-zA-Z0-9]+$/,
                            message: "Używaj tylko liter i cyfr"
                        }
                    })}
                    error={!!errors.login}
                    helperText={errors.login?.message}
                />

                <TextField
                    label="Imię"
                    variant="outlined"
                    {...register("firstName", { required: "Podaj imię użytkownika" })}
                    error={!!errors.firstName}
                    helperText={errors.firstName?.message}
                />

                <TextField
                    label="Nazwisko"
                    variant="outlined"
                    {...register("lastName", { required: "Nazwisko jest wymagane" })}
                    error={!!errors.lastName}
                    helperText={errors.lastName?.message}
                />

                <TextField
                    label="Hasło"
                    type="password"
                    variant="outlined"
                    {...register("password", {
                        required: "Hasło jest wymagane",
                        minLength: { value: 5, message: "Hasło jest za słabe (min. 5 znaków)" }
                    })}
                    error={!!errors.password}
                    helperText={errors.password?.message}
                />

                <Button type="submit" variant="contained" size="large" disabled={!isValid}>
                    {isAdmin ? "Utwórz Użytkownika" : "Zarejestruj się"}
                </Button>

                <Button variant="outlined" onClick={() => navigate(isAdmin ? '/' : '/login')}>
                    Anuluj
                </Button>
            </Box>

            <ConfirmDialog
                open={dialogOpen}
                title="Potwierdź"
                content={`Czy na pewno chcesz utworzyć użytkownika ${pendingData?.login}?`}
                onConfirm={handleConfirmCreate}
                onCancel={() => setDialogOpen(false)}
            />
        </Container>
    );
};