import { useEffect, useState } from 'react';
import { UserService } from '../api/UserService';
import { Link } from 'react-router-dom';
import { Button, Table, TableBody, TableCell, TableHead, TableRow, Container, Stack, Paper, TextField, Typography } from '@mui/material';

interface User {
    id: string;
    login: string;
    firstName: string;
    lastName: string;
    isActive?: boolean;
    active?: boolean;
    phoneNumber?: string;
    accessLevel?: string;
}

export const UserList = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [filterText, setFilterText] = useState('');

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        try {
            const data = await UserService.getAll();
            setUsers(data as any);
        } catch (error) {
            console.error("Błąd pobierania danych", error);
        }
    };


    const handleActivate = async (id: string) => {
        if(window.confirm("Czy na pewno chcesz AKTYWOWAĆ tego użytkownika?")) {
            try {
                await UserService.activate(id);
                loadUsers();
            } catch (e) {
                alert("Nie udało się aktywować użytkownika.");
            }
        }
    }


    const handleDeactivate = async (id: string) => {
        if(window.confirm("Czy na pewno chcesz ZABLOKOWAĆ tego użytkownika?")) {
            try {
                await UserService.deactivate(id);
                loadUsers();
            } catch (e) {
                alert("Nie udało się zablokować użytkownika.");
            }
        }
    }

    const isUserActive = (user: User) => {
        return user.active === true || user.isActive === true;
    };

    const filteredUsers = users.filter(user =>
        user.login.toLowerCase().includes(filterText.toLowerCase()) ||
        user.id.includes(filterText)
    );

    return (
        <Container maxWidth="lg">
            <Stack direction="row" justifyContent="space-between" alignItems="center" mb={4}>
                <Typography variant="h4">Lista Użytkowników</Typography>
                <Button
                    component={Link}
                    to="/users/create"
                    variant="contained"
                    color="primary"
                    size="large"
                >
                    Dodaj Użytkownika
                </Button>
            </Stack>

            <Paper sx={{ p: 3, mb: 4 }} elevation={3}>
                <TextField
                    label="Szukaj po Loginie lub ID"
                    variant="filled"
                    fullWidth
                    value={filterText}
                    onChange={(e) => setFilterText(e.target.value)}
                    placeholder="Wpisz login lub fragment ID..."
                />
            </Paper>

            <Paper elevation={3} sx={{ overflow: 'hidden' }}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Login</TableCell>
                            <TableCell>Imię</TableCell>
                            <TableCell>Nazwisko</TableCell>
                            <TableCell>Status</TableCell>
                            <TableCell align="center">Akcje</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {filteredUsers.map((user) => {
                            const active = isUserActive(user);
                            return (
                                <TableRow key={user.id} hover>
                                    <TableCell sx={{ fontWeight: 500 }}>{user.login}</TableCell>
                                    <TableCell>{user.firstName}</TableCell>
                                    <TableCell>{user.lastName}</TableCell>
                                    <TableCell
                                        sx={{ color: active ? '#66bb6a' : '#ef5350', fontWeight: 'bold' }}
                                    >
                                        {active ? 'AKTYWNY' : 'NIEAKTYWNY'}
                                    </TableCell>
                                    <TableCell align="center">
                                        <Stack direction="row" spacing={1} justifyContent="center">
                                            <Button
                                                component={Link}
                                                to={`/users/details/${user.id}`}
                                                variant="outlined"
                                                size="small"
                                            >
                                                Szczegóły
                                            </Button>

                                            <Button
                                                component={Link}
                                                to={`/users/edit/${user.id}`}
                                                variant="contained"
                                                color="info"
                                                size="small"
                                            >
                                                Edytuj
                                            </Button>

                                            {active ? (
                                                <Button
                                                    onClick={() => handleDeactivate(user.id)}
                                                    variant="contained"
                                                    color="error"
                                                    size="small"
                                                >
                                                    Dezaktywuj
                                                </Button>
                                            ) : (
                                                <Button
                                                    onClick={() => handleActivate(user.id)}
                                                    variant="contained"
                                                    color="success"
                                                    size="small"
                                                >
                                                    Aktywuj
                                                </Button>
                                            )}
                                        </Stack>
                                    </TableCell>
                                </TableRow>
                            );
                        })}
                    </TableBody>
                </Table>
            </Paper>
        </Container>
    );
};