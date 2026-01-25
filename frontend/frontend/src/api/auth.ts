import {api} from "./api";
import {UserAuthDto} from "../dto/UserAuthDto";
import {UserRegistrationDto} from "../dto/UserRegistrationDto";

export const auth = {
    login: (data: UserAuthDto) =>
        api.post("/api/v1/auth/login", data),

    register: (data: UserRegistrationDto) =>
        api.post("/api/v1/auth/signup", data),

    refresh: () =>
        api.post("/api/v1/auth/refresh"),

    logout: () =>
        api.post("/api/v1/auth/logout"),
};