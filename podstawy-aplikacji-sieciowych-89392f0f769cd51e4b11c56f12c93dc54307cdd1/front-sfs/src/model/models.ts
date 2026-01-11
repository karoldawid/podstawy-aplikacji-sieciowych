export interface User {
    id: string;
    login: string;
    firstName: string;
    lastName: string;
    isActive: boolean;
    phoneNumber?: string;
    accessLevel?: 'ADMIN' | 'CLIENT' | 'RESOURCE_MANAGER';
}

export interface CreateUserDto {
    login: string;
    password?: string;
    firstName: string;
    lastName: string;
    phoneNumber?: string;
    accessLevel: string;
}

export interface SportsFacility {
    id: string;
    name: string;
}

export interface Rental {
    id: string;
    beginTime: string;
    endTime: string;
    sportsFacility: SportsFacility;
    client: User;
}

export interface CreateRentalDto {
    clientId: string;
    sportsFacilityId: string;
    beginTime: string;
    endTime: string;
}