import { Grupo } from './grupo';

export interface GrupoPage {
    grupos: Grupo[];
    totalElements: number;
    totalPages: number;
}