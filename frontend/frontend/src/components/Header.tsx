import React from "react";

const Header: React.FC = () => {
    return (
        <header style={{padding: "1rem", backgroundColor: "#282c34", color: "white"}}>
            <h1>Welcome</h1>
            <nav>
                <a href="/" style={{margin: "1rem", color: "white"}}>Home</a>
                <a href="/signup" style={{margin: "1rem", color: "white"}}>Signup</a>
                <a href="/login" style={{margin: "1rem", color: "white"}}>Login</a>
                <a href="/profile" style={{margin: "1rem", color: "white"}}>Profile</a>
            </nav>
        </header>
    );
};

export default Header;