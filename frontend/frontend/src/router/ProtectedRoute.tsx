import {Navigate, Outlet} from "react-router-dom";
import {useAuth} from "../auth/AuthContext";
import React from "react";

const ProtectedRoute: React.FC = () => {
    const {token} = useAuth();

    return token ? <Outlet/> : <Navigate to="/login" replace/>;
};

export default ProtectedRoute;