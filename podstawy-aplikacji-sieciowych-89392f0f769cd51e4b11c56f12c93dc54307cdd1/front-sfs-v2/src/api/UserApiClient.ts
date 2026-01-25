import type {AxiosResponse} from "axios";
import type {UserType} from "../model/UserType.ts";
import {restApiInstance} from "./api.config.ts";
import type {CreateClientType} from "../model/CreateClientType.ts";
import type {UpdateUserType} from "../model/UpdateUserType.ts";

// Promise to puste pudelko, ktore bedzie obiecuje ze bedzie wynik (dane z serwera)
// async nie zwroci wyniku od razu ale zwroci Promise
export const UserApiClient = {
    // AxiosResponse<T> cala odpowiedz HTTP (status + body)
    // pobierz uzytkownikow
    getAll: async (): Promise<AxiosResponse<UserType[]>> => {
        try {
            // await czeka na odpowiedz z zapytania i nie pojdzie dalej
            const response = await restApiInstance.get<UserType[]>('/users');
            return response;
        } catch (error) {
            console.error("Błąd pobierania użytkowników:", error);
            throw error;
        }
    },
    // stworz uzytkownika
    createClient: async (data: CreateClientType): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.post<UserType>('/clients', data);
    },

    // aktywuj
    activateUser: async (id: string): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.put<UserType>(`/users/${id}/activate`);
    },

    // dezaktywuj
    deactivateUser: async (id: string): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.put<UserType>(`/users/${id}/deactivate`);
    },

    getById: async (id: string): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.get<UserType>(`/users/${id}`);
    },

    updateUser: async (id: string, data: UpdateUserType): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.put<UserType>(`/users/${id}`, data);
    },

    createAdmin: async (data: CreateClientType): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.post<UserType>('/admins', data);
    },
    
    createManager: async (data: CreateClientType): Promise<AxiosResponse<UserType>> => {
        return await restApiInstance.post<UserType>('/facility-managers', data);
    }

};