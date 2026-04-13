import { AppBar, Box, Button, Toolbar, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";

export const Navbar = () => {
    const navigate = useNavigate();

    // sprawdzamy czy jest token
    const isAuthenticated = !!sessionStorage.getItem('token');

    const userJson = sessionStorage.getItem('user');
    const userLogin = userJson ? JSON.parse(userJson).login : '';
    // pobieranei roli
    const userRole = sessionStorage.getItem('role');

    // wylogowowanie
    const handleLogout = () => {
        // usuwamy po użytkowniku
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('role');
        sessionStorage.removeItem('user');
        sessionStorage.removeItem('userId');
        navigate('/login');
        window.location.reload();
    };

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography
                    variant="h6"
                    component="div"
                    sx={{ flexGrow: 1, cursor: 'pointer' }}
                    onClick={() => navigate('/')}
                >
                    System SFS
                </Typography>

                <Box sx={{ display: 'flex', gap: 2 }}>
                    {isAuthenticated ? (
                        <>
                            {(userRole === 'ADMIN' || userRole === 'FACILITYMANAGER') && (
                                <Button color="inherit" onClick={() => navigate('/')}>
                                    Użytkownicy
                                </Button>
                            )}

                            <Button color="inherit" onClick={() => navigate('/rentals')}>
                                Wypożyczenia
                            </Button>

                            <Button color="inherit" onClick={() => navigate('/change-password')}>
                                Hasło
                            </Button>

                            <Button
                                color="inherit"
                                variant="outlined"
                                sx={{ borderColor: 'white' }}
                                onClick={handleLogout}
                            >
                                Wyloguj {userLogin} ({userRole})
                            </Button>

                        </>
                    ) : (
                        <Button color="inherit" variant="outlined" onClick={() => navigate('/login')}>
                            Zaloguj
                        </Button>
                    )}
                </Box>
            </Toolbar>
        </AppBar>
    );
};