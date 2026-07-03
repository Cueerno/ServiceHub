import {api} from "./api";

export const setupInterceptors = (
    getToken: () => string | null,
    setToken: (token: string | null) => void,
    logout: () => Promise<void>
) => {

    api.interceptors.request.use((config) => {
        const token = getToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    });

    api.interceptors.response.use(
        (response) => response,
        async (error) => {
            const originalRequest = error.config;

            if (
                error.response?.status === 401 &&
                !originalRequest._retry
            ) {
                originalRequest._retry = true;

                try {
                    const refreshResponse = await api.post(
                        "/api/v1/auth/refresh"
                    );

                    const newToken = refreshResponse.data.accessToken.jwt;
                    setToken(newToken);

                    originalRequest.headers.Authorization = `Bearer ${newToken}`;

                    return api(originalRequest);
                } catch (refreshError) {
                    await logout();
                    return Promise.reject(refreshError);
                }
            }

            return Promise.reject(error);
        }
    );
};