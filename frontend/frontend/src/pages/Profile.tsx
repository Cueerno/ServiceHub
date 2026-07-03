import React, { useEffect, useState } from "react";
import { userApi } from "../api/user";
import { UserResponseDto } from "../dto/UserResponseDto";

const Profile: React.FC = () => {
    const [user, setUser] = useState<UserResponseDto | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await userApi.me();
                setUser(response.data);
            } catch (e) {
                setError("Не удалось загрузить профиль");
            } finally {
                setLoading(false);
            }
        };

        fetchProfile();
    }, []);

    if (loading) return <p>Загрузка профиля...</p>;
    if (error) return <p>{error}</p>;
    if (!user) return null;

    return (
        <>
            <h2>Profile</h2>

            <p><b>Username:</b> {user.username}</p>
            <p><b>Firstname:</b> {user.firstname}</p>
            <p><b>Lastname:</b> {user.lastname}</p>
            <p><b>Email:</b> {user.email}</p>
            <p><b>Phone number:</b> {user.phoneNumber}</p>
            <p>
                <b>Registered:</b>{" "}
                {new Date(user.createdAt).toLocaleString()}
            </p>
        </>
    );
};

export default Profile;