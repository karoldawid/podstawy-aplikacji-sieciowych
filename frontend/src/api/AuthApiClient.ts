import type { AxiosResponse } from 'axios';
import { restApiInstance } from './api.config';

export interface AuthRequest {
    login: string;
    password?: string;
}

export interface AuthResponse {
    token: string;
    userId: string;
    role: string;
    login: string;
}

export const AuthApiClient = {
    login: async (data: AuthRequest): Promise<AxiosResponse<AuthResponse>> => {
        // wysyla dane na endpiint /auth/login
        return await restApiInstance.post<AuthResponse>('/auth/login', data);
    }
};