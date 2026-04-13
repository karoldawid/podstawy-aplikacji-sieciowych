import './App.css'
import { Container } from "@mui/material";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import { UserList } from "./pages/UserList.tsx";
import { CreateUser } from "./pages/CreateUser.tsx";
import { EditUser } from "./pages/EditUser.tsx";
import { ClientDetails } from "./pages/ClientDetails.tsx";
import { RentalList } from "./pages/RentalList.tsx";
import { CreateRental } from "./pages/CreateRental.tsx";
import { LoginPage } from "./pages/LoginPage.tsx";
import { Navbar } from "./components/Navbar.tsx";
import { ProtectedRoute } from "./components/ProtectedRoute.tsx";
import { CreateSelfRental } from "./pages/CreateSelfRental.tsx";
import { ChangePassword } from "./pages/ChangePassword.tsx";

function App() {
    return (
        <BrowserRouter>
            <Navbar />

            <Container sx={{mt: 4}}>
                <Routes>
                    {/* pulblic */}
                    <Route path="/login" element={<LoginPage />} />
                    {/* tylko kleint */}
                    <Route path="/create-client" element={<CreateUser />} />

                    {/* admin */}
                    <Route element={<ProtectedRoute allowedRoles={['ADMIN']} />}>
                        <Route path="/edit/:id" element={<EditUser />} />
                    </Route>

                    {/* admin i managegr */}
                    <Route element={<ProtectedRoute allowedRoles={['ADMIN', 'FACILITYMANAGER']} />}>
                        <Route path="/" element={<UserList />} />
                        <Route path="/users" element={<UserList />} />
                        <Route path="/details/:id" element={<ClientDetails />} />
                        <Route path="/create-rental" element={<CreateRental />} />
                    </Route>

                    {/* zalogowni */}
                    <Route element={<ProtectedRoute allowedRoles={['ADMIN', 'FACILITYMANAGER', 'CLIENT']} />}>
                        <Route path="/rentals" element={<RentalList />} />
                        <Route path="/rent-facility" element={<CreateSelfRental />} />
                        <Route path="/change-password" element={<ChangePassword />} />
                    </Route>

                    <Route path="*" element={<Navigate to="/login" replace />} />

                </Routes>
            </Container>
        </BrowserRouter>
    );
}

export default App;