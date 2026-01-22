import axios from "axios";
import { UserAuthDto } from "../dto/UserAuthDto";
import { UserRegistrationDto } from "../dto/UserRegistrationDto";

const api = axios.create({
    baseURL: "http://localhost:8080/api/v1/auth",
    withCredentials: true,
});

export const auth = {
    login: (data: UserAuthDto) =>
        api.post("/login", data),

    register: (data: UserRegistrationDto) =>
        api.post("/signup", data),

    refresh: () =>
        api.post("/refresh"),

    logout: () =>
        api.post("/logout"),
}