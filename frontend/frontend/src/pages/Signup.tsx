import React, {useState} from "react";
import {auth} from "../api/auth";

const Signup: React.FC = () => {
    const [form, setForm] = useState({
        username: "",
        firstname: "",
        lastname: "",
        email: "",
        phoneNumber: "",
        password: "",
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setForm({...form, [e.target.name]: e.target.value});
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            await auth.register(form);
            alert("Success");
        } catch {
            alert("Registration error");
        }
    };

    return (
        <>
            <h2>Registration</h2>
            <form onSubmit={handleSubmit}>
                {Object.keys(form).map((key) => (
                    <input
                        key={key}
                        name={key}
                        placeholder={key}
                        type={key === "password" ? "password" : "text"}
                        onChange={handleChange}
                    />
                ))}
                <br/>
                <button type="submit">Signup</button>
            </form>
        </>
    );
};

export default Signup;