export interface CreateClientType {
    firstName: string;
    lastName: string;
    login: string;
    // haslo, nie ma w REST, TODO: dodac
    password: string; // optional pole?: typ
}