import apiClient from './axiosConfig';


export interface SportsFacility {
    id: string;
    name: string;
}

export interface ClientSimple {
    id: string;
    login: string;
    firstName: string;
    lastName: string;
}

export interface Rental {
    id: string;
    beginTime: string;
    endTime: string;
    sportsFacility?: SportsFacility;
    facilityId: string;
    clientId: string;
    client?: any;
}



export const RentalService = {
    getAll: async () => {
        const response = await apiClient.get<Rental[]>('/rentals');
        return response.data;
    },
    getByClientId: async (clientId: string) => {
        const response = await apiClient.get<Rental[]>(`/rentals/client/${clientId}`);
        return response.data;
    },

    create: async (data: any) => {
        await apiClient.post('/rentals/rent', data);
    },

    finish: async (rentalId: string) => {
        await apiClient.put(`/rentals/finish/${rentalId}`);
    }
};