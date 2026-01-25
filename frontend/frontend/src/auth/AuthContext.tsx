import React, {createContext, useContext, useEffect, useState} from "react";
import {auth} from "../api/auth";
import {setupInterceptors} from "../api/interceptors";

interface AuthContextType {
    token: string | null;
    setToken: (token: string | null) => void;
    logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const [token, setTokenState] = useState<string | null>(
        localStorage.getItem("accessToken")
    );

    const setToken = (token: string | null) => {
        setTokenState(token);
        if (token) {
            localStorage.setItem("accessToken", token);
        } else {
            localStorage.removeItem("accessToken");
        }
    };

    const logout = async () => {
        try {
            await auth.logout();
        } finally {
            setToken(null);
        }
    };

    useEffect(() => {
        setupInterceptors(
            () => token,
            setToken,
            logout
        );
    }, [token]);

    return (
        <AuthContext.Provider value={{token, setToken, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const ctx = useContext(AuthContext);
    if (!ctx) {
        throw new Error("useAuth must be used within AuthProvider");
    }
    return ctx;
};