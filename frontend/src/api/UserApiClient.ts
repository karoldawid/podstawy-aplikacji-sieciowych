import type { AxiosResponse } from 'axios';
import { restApiInstance } from './api.config';
import type { UserType } from '../model/UserType';
import type { CreateClientType } from '../model/CreateClientType';
import type { UpdateUserType } from '../model/UpdateUserType';

export const UserApiClient = {
    getAll: async (): Promise<AxiosResponse<UserType[]>> => {
        return await restApiInstance.get<UserType[]>('/users');
    },
    getById: async (id: string): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.get<UserType>(`/users/${id}`);
    },
    updateUser: async (id: string, data: UpdateUserType, etag: string | null): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.put<UserType>(`/users/${id}`, data, {
            headers: {
                'If-Match': etag
            }
        });
    },
    activateUser: async (id: string): Promise<AxiosResponse> => {
        return await restApiInstance.put(`/users/${id}/activate`);
    },
    deactivateUser: async (id: string): Promise<AxiosResponse> => {
        return await restApiInstance.put(`/users/${id}/deactivate`);
    },
    createClient: async (data: CreateClientType): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.post<UserType>('/clients', data);
    },
    createAdmin: async (data: CreateClientType): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.post<UserType>('/admins', data);
    },
    createManager: async (data: CreateClientType): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.post<UserType>('/facility-managers', data);
    },
    changePassword: async (data: { oldPassword: string, newPassword: string }): Promise<AxiosResponse> => {
        return await restApiInstance.post('/users/change-password', data);
    }
};