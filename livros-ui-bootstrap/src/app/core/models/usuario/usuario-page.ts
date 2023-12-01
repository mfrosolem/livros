import { Usuario } from "./usuario";

export interface UsuarioPage {
    usuarios: Usuario[];
    totalElements: number;
    totalPages: number;
}