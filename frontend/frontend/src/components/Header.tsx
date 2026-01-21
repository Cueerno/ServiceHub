import React from "react";

const Header: React.FC = () => {
    return (
        <header style={{ padding: "1rem", backgroundColor: "#282c34", color: "white" }}>
            <h1>Добро пожаловать в Innotter</h1>
            <nav>
                <a href="/" style={{ marginRight: "1rem", color: "white" }}>Home</a>
                <a href="/pages" style={{ color: "white" }}>Pages</a>
            </nav>
        </header>
    );
};

export default Header;