import {api} from "./api";
import {UserResponseDto} from "../dto/UserResponseDto";

export const userApi = {
    me: () =>
        api.get<UserResponseDto>("/api/v1/users/me"),
};