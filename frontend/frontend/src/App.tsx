import React from 'react';
import './App.css';
import Home from "./pages/Home";
import Layout from "./components/Layout";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./pages/Login";
import Signup from "./pages/Signup";


const App: React.FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route element={<Layout/>}>
                    <Route path="/" element={<Home/>}/>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="/signup" element={<Signup/>}/>
                </Route>
            </Routes>
        </BrowserRouter>
    );
};


export default App;
