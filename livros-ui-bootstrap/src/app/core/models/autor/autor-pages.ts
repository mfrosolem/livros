import { Autor } from "./autor";

export interface AutorPage {
    autores: Autor[];
    totalElements: number;
    totalPages: number;
}