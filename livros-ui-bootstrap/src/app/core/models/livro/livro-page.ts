import { Livro } from './livro';

export interface LivroPage {
    livros: Livro[];
    totalElements: number;
    totalPages: number;
}