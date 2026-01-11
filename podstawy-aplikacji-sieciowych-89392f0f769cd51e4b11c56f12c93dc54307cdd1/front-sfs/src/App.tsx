import { BrowserRouter, Routes, Route, Navigate, Link } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import theme from './theme';
import { UserList } from './pages/UserList';
import { UserForm } from './pages/UserForm';
import { UserDetails } from './pages/UserDetails';
import { RentalForm } from './pages/RentalForm';
import { RentalList } from './pages/RentalList';
import { AppBar, Toolbar, Typography, Box, Container, Button, Stack } from '@mui/material';

function App() {
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <BrowserRouter>
                <Box
                    sx={{
                        display: 'flex',
                        flexDirection: 'column',
                        minHeight: '100vh',
                        width: '100vw',
                        bgcolor: 'background.default',
                        overflowX: 'hidden'
                    }}
                >
                    {}
                    <AppBar
                        position="static"
                        color="transparent"
                        elevation={0}
                        sx={{ borderBottom: '1px solid rgba(255,255,255,0.08)' }}
                    >
                        <Container maxWidth="lg">
                            <Toolbar disableGutters sx={{ justifyContent: 'space-between' }}>
                                <Typography
                                    variant="h6"
                                    component={Link}
                                    to="/"
                                    sx={{
                                        fontWeight: 'bold',
                                        color: 'primary.main',
                                        textDecoration: 'none',
                                        mr: 4
                                    }}
                                >
                                    System Obiektów Sportowych
                                </Typography>

                                {}
                                <Stack direction="row" spacing={2}>
                                    <Button component={Link} to="/users" color="inherit">
                                        Użytkownicy
                                    </Button>
                                    <Button component={Link} to="/rentals" color="inherit">
                                        Alokacje (Rezerwacje)
                                    </Button>
                                </Stack>
                            </Toolbar>
                        </Container>
                    </AppBar>

                    {}
                    <Box
                        component="main"
                        sx={{
                            flexGrow: 1,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            width: '100%',
                            py: 4,
                        }}
                    >
                        <Routes>
                            <Route path="/" element={<Navigate to="/users" />} />
                            <Route path="/users" element={<UserList />} />
                            <Route path="/users/create" element={<UserForm />} />
                            <Route path="/users/edit/:id" element={<UserForm />} />
                            <Route path="/users/details/:id" element={<UserDetails />} />

                            {}
                            <Route path="/rentals" element={<RentalList />} /> {}
                            <Route path="/rentals/create/:userId" element={<RentalForm />} />
                        </Routes>
                    </Box>

                    {}
                    <Box component="footer" sx={{ py: 3, textAlign: 'center', opacity: 0.5 }}>
                        <Typography variant="caption">
                            © 2026 Zespół06 • Karol Dawid, Mateusz Chodulski
                        </Typography>
                    </Box>
                </Box>
            </BrowserRouter>
        </ThemeProvider>
    );
}

export default App;