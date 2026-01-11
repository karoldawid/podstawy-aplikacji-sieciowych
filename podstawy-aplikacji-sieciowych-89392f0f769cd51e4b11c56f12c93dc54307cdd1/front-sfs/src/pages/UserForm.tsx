// @ts-nocheck
import { useEffect, useMemo } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router-dom';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { TextField, Button, Container, Select, MenuItem, InputLabel, FormControl, FormHelperText, Stack, Typography, Paper } from '@mui/material';
import { UserService } from '../api/UserService';


export const UserForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isEditMode = !!id;

    const validationSchema = useMemo(() => {
        return yup.object().shape({
            login: yup.string().required('Login jest wymagany').min(3, 'Minimum 3 znaki'),
            firstName: yup.string().required('Imię jest wymagane'),
            lastName: yup.string().required('Nazwisko jest wymagane'),
            accessLevel: yup.string().required('Rola jest wymagana'),
            password: yup.string().test('password-req', 'Hasło wymagane (min. 5 znaków)', (value) => {
                if (!isEditMode) {
                    return !!value && value.length >= 5;
                }
                if (value && value.length > 0 && value.length < 5) return false;
                return true;
            }),
            phoneNumber: yup.string().test('phone-req', 'Telefon wymagany dla Klienta', function(value) {
                if (this.parent.accessLevel === 'CLIENT' && (!value || value.length === 0)) return false;
                return true;
            }),
        });
    }, [isEditMode]);

    const { handleSubmit, control, formState: { errors }, setValue, watch, reset } = useForm({
        resolver: yupResolver(validationSchema),
        defaultValues: {
            login: '',
            firstName: '',
            lastName: '',
            password: '',
            accessLevel: '',
            phoneNumber: ''
        }
    });

    const selectedRole = watch("accessLevel");

    useEffect(() => {
        if (isEditMode && id) {
            UserService.getById(id).then(user => {
                const role = user.accessLevel || user.role || 'CLIENT';
                const phone = user.phoneNumber || '';
                const formData = {
                    ...user,
                    accessLevel: role,
                    phoneNumber: phone,
                    password: ''
                };
                reset(formData);
                if (role === 'CLIENT') {
                    setTimeout(() => { setValue('phoneNumber', phone); }, 100);
                }
            }).catch(err => console.error(err));
        }
    }, [id, isEditMode, reset, setValue]);

    const onSubmit = async (data) => {

        const actionName = isEditMode ? "zaktualizować dane" : "utworzyć nowego użytkownika";
        if (!window.confirm(`Czy na pewno chcesz ${actionName}?`)) {
            return;
        }

        try {
            const payload = { ...data };
            if (isEditMode && !payload.password) delete payload.password;
            if (payload.accessLevel !== 'CLIENT') delete payload.phoneNumber;

            if (isEditMode) {
                await UserService.update(id, payload);
                alert('Zaktualizowano użytkownika!');
            } else {
                await UserService.create(payload);
                alert('Utworzono użytkownika!');
            }
            navigate('/users');
        } catch (error) {
            console.error(error);
            const msg = error.response?.data?.message || 'Wystąpił błąd podczas zapisu.';
            alert('Błąd: ' + msg);
        }
    };

    return (
        <Container maxWidth="sm">
            <Typography variant="h4" gutterBottom sx={{ mt: 4 }}>{isEditMode ? 'Edycja Użytkownika' : 'Nowy Użytkownik'}</Typography>

            <Paper elevation={3} sx={{ p: 4, mb: 4 }}>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <Stack spacing={3}>
                        <Controller
                            name="login"
                            control={control}
                            render={({ field }) => (
                                <TextField {...field} variant="filled" label="Login" error={!!errors.login} helperText={errors.login?.message} disabled={isEditMode} />
                            )}
                        />
                        <Controller
                            name="password"
                            control={control}
                            render={({ field }) => (
                                <TextField {...field} variant="filled" label={isEditMode ? "Hasło (zostaw puste aby nie zmieniać)" : "Hasło"} type="password" error={!!errors.password} helperText={errors.password?.message} />
                            )}
                        />
                        <FormControl fullWidth error={!!errors.accessLevel} variant="filled">
                            <InputLabel id="role-label">Rola</InputLabel>
                            <Controller
                                name="accessLevel"
                                control={control}
                                render={({ field }) => (
                                    <Select {...field} labelId="role-label" label="Rola" disabled={isEditMode}>
                                        <MenuItem value="CLIENT">Klient</MenuItem>
                                        <MenuItem value="ADMIN">Administrator</MenuItem>
                                        <MenuItem value="RESOURCE_MANAGER">Manager Zasobów</MenuItem>
                                    </Select>
                                )}
                            />
                            <FormHelperText>{errors.accessLevel?.message}</FormHelperText>
                        </FormControl>
                        <Controller
                            name="firstName"
                            control={control}
                            render={({ field }) => (
                                <TextField {...field} variant="filled" label="Imię" error={!!errors.firstName} helperText={errors.firstName?.message} />
                            )}
                        />
                        <Controller
                            name="lastName"
                            control={control}
                            render={({ field }) => (
                                <TextField {...field} variant="filled" label="Nazwisko" error={!!errors.lastName} helperText={errors.lastName?.message} />
                            )}
                        />
                        {selectedRole === 'CLIENT' && (
                            <Controller
                                name="phoneNumber"
                                control={control}
                                render={({ field }) => (
                                    <TextField {...field} variant="filled" label="Numer Telefonu" error={!!errors.phoneNumber} helperText={errors.phoneNumber?.message} />
                                )}
                            />
                        )}
                        <Button type="submit" variant="contained" size="large">Zapisz</Button>
                        <Button variant="outlined" onClick={() => navigate('/users')}>Anuluj</Button>
                    </Stack>
                </form>
            </Paper>
        </Container>
    );
};