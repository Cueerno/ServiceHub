import {Outlet} from 'react-router-dom';
import Header from "./Header";
import Footer from "./Footer";
import React from "react";

const Layout: React.FC = () => {
    return (
        <>
            <Header/>
            <main style={{minHeight: "80vh", padding: "2rem"}}>
                <Outlet/>
            </main>
            <Footer/>
        </>
    );
};

export default Layout;