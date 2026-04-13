// interface a nie klasa bo jest nowszy i lzejszy i prostszy

export interface UserType {
    id: string;
    login: string;
    firstName: string;
    lastName: string;
    active: boolean; // w javie isActive
    role: string;
}

export interface ClientType extends UserType {
    // gdyby Client czy Admin mieli dodatkowe pola to można tu dodać
}