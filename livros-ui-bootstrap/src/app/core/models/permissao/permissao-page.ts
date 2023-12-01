import { Permissao } from './permissao';

export interface PermissaoPage {
    permissoes: Permissao[];
    totalElements: number;
    totalPages: number;
}