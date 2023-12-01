import { Autor } from "../autor/autor";
import { Editora } from '../editora/editora';
import { Genero } from '../genero/genero';

export interface Livro {
    id: number;
    isbn: string;
    titulo: string;
    subtitulo: string;
    idioma: string;
    serieColecao: string;
    volume: number;
    tradutor: string;
    ano: number;
    edicao: number;
    paginas: number;
    sinopse: string;
    autor: Autor;
    editora: Editora;
    genero: Genero;
  }