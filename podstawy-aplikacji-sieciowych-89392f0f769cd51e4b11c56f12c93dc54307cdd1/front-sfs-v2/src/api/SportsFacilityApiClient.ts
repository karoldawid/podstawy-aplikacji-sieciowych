import type {SportsFacilityType} from "./SportsFacilityType.ts";
import {restApiInstance} from "../api/api.config.ts";
import type {AxiosResponse} from "axios";

export const SportsFacilityApiClient = {
    // pobierz obiekt po ID
    getById: async (id: string): Promise<AxiosResponse<SportsFacilityType>> => {
        return await restApiInstance.get<SportsFacilityType>(`/facilities/${id}`);
    },

    // pobierz wszystkie
    getAll: async (): Promise<AxiosResponse<SportsFacilityType[]>> => {
        return await restApiInstance.get<SportsFacilityType[]>('/facilities');
    }
};