import type { AxiosResponse } from 'axios';
import { restApiInstance } from './api.config';
import type { SportsFacilityType } from '../model/SportsFacilityType';

export const SportsFacilityApiClient = {
    getAll: async (): Promise<AxiosResponse<SportsFacilityType[]>> => {
        return await restApiInstance.get<SportsFacilityType[]>('/facilities');
    },
    getById: async (id: string): Promise<AxiosResponse<SportsFacilityType>> => {
        return await restApiInstance.get<SportsFacilityType>(`/facilities/${id}`);
    }
};