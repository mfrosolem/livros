import { Grupo } from "../grupo/grupo";

export interface Usuario {
    id: number;
    nome: string;
    email: string;
    grupo: Grupo
}