import { Navigate, Outlet } from 'react-router-dom';

interface Props {
    allowedRoles: string[];
}

export const ProtectedRoute = ({ allowedRoles }: Props) => {
    const userRole = sessionStorage.getItem('role');
    const token = sessionStorage.getItem('token');

    if (!token) {
        return <Navigate to="/login" replace />;
    }

    if (userRole && !allowedRoles.includes(userRole)) {
        return <Navigate to="/rentals" replace />;
    }

    return <Outlet />;
};