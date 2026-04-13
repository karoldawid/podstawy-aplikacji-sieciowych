import type { AxiosResponse } from 'axios';
import { restApiInstance } from './api.config';
import type { RentalType } from '../model/RentalType';
import type { CreateRentalType } from '../model/CreateRentalType';

export const RentalApiClient = {
    getAll: async (): Promise<AxiosResponse<RentalType[]>> => {
        return await restApiInstance.get<RentalType[]>('/rentals');
    },
    getByClientId: async (clientId: string): Promise<AxiosResponse<RentalType[]>> => {
        return await restApiInstance.get<RentalType[]>(`/rentals/client/${clientId}`);
    },
    create: async (data: CreateRentalType): Promise<AxiosResponse<RentalType>> => {
        return await restApiInstance.post<RentalType>('/rentals/rent', data);
    },
    endRental: async (id: string): Promise<AxiosResponse> => {
        return await restApiInstance.put(`/rentals/finish/${id}`);
    },
    getSelfRentals: async (): Promise<AxiosResponse<RentalType[]>> => {
        return await restApiInstance.get<RentalType[]>('/rentals/self');
    },
    rentSelf: async (data: any): Promise<AxiosResponse> => {
        return await restApiInstance.post('/rentals/rent/self', data);
    }
};