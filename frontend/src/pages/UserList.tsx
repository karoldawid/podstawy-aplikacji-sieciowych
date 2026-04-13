import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { UserType } from '../model/UserType';
import { UserApiClient } from '../api/UserApiClient';
import { ConfirmDialog } from '../components/ConfirmDialog';
import {
    Table, TableBody, TableCell, TableContainer,
    TableHead, TableRow, Paper, Typography, CircularProgress, Alert, Container, Button, TextField, Box, Chip
} from '@mui/material';

const getRoleColor = (role: string | undefined) => {
    switch (role?.toUpperCase()) {
        case 'ADMIN': return 'error';
        case 'FACILITYMANAGER': return 'secondary';
        case 'MANAGER': return 'secondary';
        case 'CLIENT': return 'primary';
        default: return 'default';
    }
};

export const UserList = () => {
    const navigate = useNavigate();
    const [users, setUsers] = useState<UserType[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [searchText, setSearchText] = useState("");
    const userRole = sessionStorage.getItem('role');
    const [dialogOpen, setDialogOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState<UserType | null>(null);

    const fetchUsers = async () => {
        try {
            const response = await UserApiClient.getAll();
            setUsers(response.data);
        } catch (err) {
            setError("Nie udało się pobrać użytkowników.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleClickAction = (user: UserType) => {
        setSelectedUser(user);
        setDialogOpen(true);
    };

    const handleConfirmAction = async () => {
        if (!selectedUser) return;
        try {
            if (selectedUser.active) {
                await UserApiClient.deactivateUser(selectedUser.id);
            } else {
                await UserApiClient.activateUser(selectedUser.id);
            }
            await fetchUsers();
        } catch (err) {
            alert("Błąd zmiany statusu!");
        } finally {
            setDialogOpen(false);
            setSelectedUser(null);
        }
    };

    // K: logika filtrowania po loginie lub id
    const filteredUsers = users.filter(user => {
        const lowerSearch = searchText.toLowerCase();
        const matchesSearch = user.login.toLowerCase().includes(lowerSearch) ||
            user.id.toLowerCase().includes(lowerSearch);

        if (userRole === 'FACILITYMANAGER' && user.role === 'ADMIN') {
            return false;
        }

        return matchesSearch;
    });

    if (loading) return <Container sx={{ mt: 5, textAlign: 'center' }}><CircularProgress /></Container>;
    if (error) return <Container sx={{ mt: 5 }}><Alert severity="error">{error}</Alert></Container>;

    return (
        <Container sx={{ mt: 5 }}>
            <Typography variant="h4" gutterBottom>Lista Użytkowników</Typography>

            <Box sx={{ mb: 3 }}>
                <TextField
                    label="Szukaj (Login lub ID)"
                    variant="outlined" fullWidth value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                />
            </Box>

            <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }}>
                    <TableHead>
                        <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
                            <TableCell>Login</TableCell>
                            <TableCell>Imię</TableCell>
                            <TableCell>Nazwisko</TableCell>
                            <TableCell>Rola</TableCell>
                            <TableCell>Status / Akcja</TableCell>
                            <TableCell>ID</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {filteredUsers.map((user) => (
                            <TableRow key={user.id} hover>
                                <TableCell>{user.login}</TableCell>

                                <TableCell>{user.firstName}</TableCell>

                                <TableCell>{user.lastName}</TableCell>

                                <TableCell>
                                    <Chip
                                        label={user.role || "CLIENT"}
                                        color={getRoleColor(user.role) as any}
                                        size="small"
                                        variant="outlined"
                                    />
                                </TableCell>

                                <TableCell>
                                    <Box sx={{ display: 'flex', gap: 1 }}>
                                        <Button variant="outlined" color="info" size="small"
                                                onClick={() => navigate(`/details/${user.id}`)}>
                                            Info
                                        </Button>

                                        {userRole === 'ADMIN' && (
                                            <>
                                                <Button variant="outlined" size="small"
                                                        onClick={() => navigate(`/edit/${user.id}`)}>
                                                    Edytuj
                                                </Button>
                                                <Button variant="contained" size="small"
                                                        color={user.active ? "error" : "success"}
                                                        onClick={() => handleClickAction(user)}
                                                >
                                                    {user.active ? "Dezaktywuj" : "Aktywuj"}
                                                </Button>
                                            </>
                                        )}
                                    </Box>
                                </TableCell>

                                <TableCell sx={{ color: 'gray', fontSize: '0.8em' }}>{user.id}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <ConfirmDialog
                open={dialogOpen}
                title={selectedUser?.active ? "Potwierdź dezaktywację" : "Potwierdź aktywację"}
                content={`Czy na pewno chcesz zmienić status użytkownika ${selectedUser?.login}?`}
                onConfirm={handleConfirmAction}
                onCancel={() => setDialogOpen(false)}
            />
        </Container>
    );
};