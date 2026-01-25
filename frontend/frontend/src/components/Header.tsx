import {Link, useNavigate} from "react-router-dom";
import {useAuth} from "../auth/AuthContext";
import React from "react";

const Header: React.FC = () => {
    const {token, logout} = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();
        navigate("/login");
    };

    return (
        <header style={{padding: "10px", borderBottom: "1px solid #ccc"}}>
            <nav style={{display: "flex", gap: "10px"}}>
                <Link to="/">Home</Link>

                {!token && (
                    <>
                        <Link to="/login">Login</Link>
                        <Link to="/register">Register</Link>
                    </>
                )}

                {token && (
                    <>
                        <Link to="/profile">Profile</Link>
                        <button onClick={handleLogout}>Logout</button>
                    </>
                )}
            </nav>
        </header>
    );
};

export default Header;