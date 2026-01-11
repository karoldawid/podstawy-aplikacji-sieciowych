import apiClient from './axiosConfig';

export interface SportsFacility {
    id: string;
    name: string;

}

export const SportsFacilityService = {
    getAll: async () => {
        const response = await apiClient.get<SportsFacility[]>('/facilities');
        return response.data;
    }
};