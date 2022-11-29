export class Genero {
    id = 0;
    descricao = '';
  }

  export class Editora {
    id = 0;
    nome = '';
    urlSiteOficial = '';
    urlFacebook = '';
    ultTwitter = '';
    urlWikipedia = '';
  }

  export class Autor {
    id: number = 0;
    nome: string = '';
    sobrenome: string = '';
    nomeConhecido: string = '';
    sexo: string = 'M';
    dataNascimento: Date = new Date();
    dataFalecimento: Date = new Date();
    paisNascimento: string = '';
    estadoNascimento: string = '';
    cidadeNascimento: string = '';
    biografia: string = '';
    urlSiteOficial: string = '';
    urlFacebook: string = '';
    ultTwitter: string = '';
    urlWikipedia: string = '';
  }

  // export class Livro {
  //   id?: number;
  //   isbn?: string;
  //   titulo?: string;
  //   subtitulo?: string;
  //   idioma?: string;
  //   serieColecao?: string;
  //   volume?: number;
  //   tradutor?: string;
  //   editora = new Editora();
  //   ano?: number;
  //   edicao?: number;
  //   paginas?: number;
  //   genero = new Genero();
  //   sinopse?: string;
  //   autor = new Autor();
  // }
