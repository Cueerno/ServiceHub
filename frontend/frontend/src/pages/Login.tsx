import React, {useState} from "react";
import {auth} from "../api/auth";

const Login: React.FC = () => {
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const response = await auth.login({login, password});
            console.log("ACCESS TOKEN:", response.data.accessToken.jwt);
            alert("Success");
        } catch (error) {
            alert("Authentication error");
        }
    };

    return (
        <>
            <h2>Вход</h2>
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
                <button type="submit">Войти</button>
            </form>
        </>
    );
};

export default Login;