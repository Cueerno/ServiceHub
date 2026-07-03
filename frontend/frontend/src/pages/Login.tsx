import React, {useState} from "react";
import {auth} from "../api/auth";
import {useAuth} from "../auth/AuthContext";
import {useNavigate} from "react-router-dom";

const Login: React.FC = () => {
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const {setToken} = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await auth.login({login, password});
            const accessToken = response.data.accessToken.jwt;
            setToken(accessToken);
            navigate("/");
        } catch (error) {
            alert("Authentication error");
        }
    };

    return (
        <>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <input
                    placeholder="Username or email"
                    value={login}
                    onChange={(e) => setLogin(e.target.value)}
                />
                <br/>
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <br/>
                <button type="submit">Login</button>
            </form>
        </>
    );
};

export default Login;