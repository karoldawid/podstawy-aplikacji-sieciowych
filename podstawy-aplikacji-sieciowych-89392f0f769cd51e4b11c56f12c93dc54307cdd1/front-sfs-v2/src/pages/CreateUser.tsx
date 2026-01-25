import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import type { CreateClientType } from "../model/CreateClientType";
import { UserApiClient } from "../api/UserApiClient";
import { ConfirmDialog } from "../components/ConfirmDialog";
import { Alert, Box, Button, Container, TextField, Typography } from "@mui/material";

// WAŻNE: Tu musi być export const CreateClient
export const CreateClient = () => {
    const navigate = useNavigate();
    const [errorMsg, setErrorMsg] = useState<string | null>(null);

    // --- STANY DIALOGU ---
    const [dialogOpen, setDialogOpen] = useState(false);
    const [pendingData, setPendingData] = useState<CreateClientType | null>(null);

    const { register, handleSubmit, formState: { errors, isValid } } = useForm<CreateClientType>({
        mode: 'onBlur'
    });

    // 1. Formularz poprawny -> otwórz okno
    const onSubmit = (data: CreateClientType) => {
        setPendingData(data);
        setDialogOpen(true);
    };

    // 2. Potwierdzam -> wyślij do API
    const handleConfirmCreate = async () => {
        if (!pendingData) return;
        try {
            await UserApiClient.createClient(pendingData);
            navigate('/');
        } catch (error: any) {
            setErrorMsg("Błąd tworzenia klienta. Może login jest zajęty?");
        } finally {
            setDialogOpen(false);
        }
    };

    return (
        <Container maxWidth="sm" sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>Nowy Klient</Typography>

            {errorMsg && <Alert severity="error" sx={{ mb: 2 }}>{errorMsg}</Alert>}

            <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>

                <TextField
                    label="Login"
                    variant="outlined"
                    {...register("login", {
                        required: "Login jest wymagany",
                        minLength: { value: 3, message: "Login musi mieć min. 3 znaki" },
                        pattern: {
                            value: /^[a-zA-Z0-9]+$/,
                            message: "Login nie może zawierać spacji ani znaków specjalnych"
                        }
                    })}
                    error={!!errors.login}
                    helperText={errors.login?.message}
                />

                <TextField
                    label="Imię"
                    variant="outlined"
                    {...register("firstName", { required: "Imię jest wymagane" })}
                    error={!!errors.firstName}
                    helperText={errors.firstName?.message}
                />

                <TextField
                    label="Nazwisko"
                    variant="outlined"
                    {...register("lastName", {
                        required: "Nazwisko jest wymagane",
                        minLength: { value: 2, message: "Nazwisko za krótkie" }
                    })}
                    error={!!errors.lastName}
                    helperText={errors.lastName?.message}
                />

                <TextField
                    label="Hasło"
                    type="password"
                    variant="outlined"
                    {...register("password", {
                        required: "Hasło jest wymagane",
                        minLength: { value: 5, message: "Hasło musi być dłuższe (min. 5 znaków)" }
                    })}
                    error={!!errors.password}
                    helperText={errors.password?.message}
                />

                <Button
                    type="submit"
                    variant="contained"
                    size="large"
                    disabled={!isValid}
                >
                    Utwórz Klienta
                </Button>

                <Button variant="outlined" onClick={() => navigate('/')}>
                    Anuluj
                </Button>
            </Box>

            {/* OKNO POTWIERDZENIA */}
            <ConfirmDialog
                open={dialogOpen}
                title="Potwierdź utworzenie"
                content={`Czy na pewno chcesz utworzyć klienta ${pendingData?.login}?`}
                onConfirm={handleConfirmCreate}
                onCancel={() => setDialogOpen(false)}
            />
        </Container>
    );
};