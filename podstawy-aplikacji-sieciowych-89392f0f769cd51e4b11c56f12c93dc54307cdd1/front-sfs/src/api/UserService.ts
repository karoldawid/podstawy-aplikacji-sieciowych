import apiClient from './axiosConfig';

export interface User {
    id: string;
    login: string;
    firstName: string;
    lastName: string;
    isActive: boolean;
    phoneNumber?: string;
    accessLevel?: 'ADMIN' | 'CLIENT' | 'RESOURCE_MANAGER';
}

interface CreateUserDto {
    login: string;
    password?: string;
    firstName: string;
    lastName: string;
    phoneNumber?: string;
    accessLevel: string;
}

export const UserService = {
    getAll: async () => {
        const response = await apiClient.get<User[]>('/users');
        return response.data;
    },
    getById: async (id: string) => {
        const response = await apiClient.get<User>(`/users/${id}`);
        return response.data;
    },
    create: async (data: CreateUserDto) => {
        let endpoint = '/clients';
        if (data.accessLevel === 'ADMIN') endpoint = '/admins';
        if (data.accessLevel === 'RESOURCE_MANAGER') endpoint = '/facility-managers';

        const response = await apiClient.post(endpoint, data);
        return response.data;
    },
    update: async (id: string, data: any) => {
        const response = await apiClient.put(`/users/${id}`, data);
        return response.data;
    },
    activate: async (id: string) => {
        await apiClient.put(`/users/${id}/activate`);
    },
    deactivate: async (id: string) => {
        await apiClient.put(`/users/${id}/deactivate`);
    }
};