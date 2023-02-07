export interface IGenero {
  id: number;
  descricao: string;
}

export interface IEditora {
  id: number;
  nome: string;
  urlSiteOficial: string;
  urlFacebook: string;
  ultTwitter: string;
  urlWikipedia: string;
}

export interface IAutor {
  id: number;
  nome: string;
  sobrenome: string;
  nomeConhecido: string;
  sexo: string;
  dataNascimento: Date;
  dataFalecimento: Date;
  paisNascimento: string;
  estadoNascimento: string;
  cidadeNascimento: string;
  biografia: string;
  urlSiteOficial: string;
  urlFacebook: string;
  ultTwitter: string;
  urlWikipedia: string;
}

export interface ILivro {
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

// CLASSES

export class Genero {
  id?: number;
  descricao?: string;
}

export class Editora {
  id?: number;
  nome?: string;
  urlSiteOficial?: string;
  urlFacebook?: string;
  ultTwitter?: string;
  urlWikipedia?: string;
}

export class Autor {
  id?: number;
  nome?: string;
  sobrenome?: string;
  nomeConhecido?: string;
  sexo = 'M';
  dataNascimento: Date = new Date();
  dataFalecimento: Date = new Date();
  paisNascimento?: string;
  estadoNascimento?: string;
  cidadeNascimento?: string;
  biografia?: string;
  urlSiteOficial?: string;
  urlFacebook?: string;
  ultTwitter?: string;
  urlWikipedia?: string;
}


export class Livro {
  id?: number;
  isbn?: string;
  titulo?: string;
  subtitulo?: string;
  idioma = 'Português';
  serieColecao?: string;
  volume?: number;
  tradutor?: string;
  ano?: number;
  edicao?: number;
  paginas?: number;
  sinopse?: string;
  editora = new Editora();
  genero = new Genero();
  autor = new Autor();
}