import React, {createContext, useContext, useState} from "react";
import {auth} from "../api/auth";

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
        } catch (e) {
            console.warn("Logout request failed, clearing local state anyway");
        } finally {
            setToken(null);
        }
    };

    return (
        <AuthContext.Provider value={{token, setToken, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within AuthProvider");
    }
    return context;
};