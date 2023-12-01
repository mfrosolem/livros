import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, take } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LivroPage } from '../core/models/livro/livro-page';
import { Livro } from '../core/models/livro/livro';


export class LivroFilter {
  titulo?: string;
  pagina: number;
  itensPorPagina: number;

  constructor(pagina: number, itensPorPagina: number, titulo?: string) {
    this.pagina = pagina;
    this.itensPorPagina = itensPorPagina;
    this.titulo = titulo;
  }
}

class GeneroIdInput {
  id?: number;

  constructor(id?: number) {
    this.id = id;
  }
}

class EditoraIdInput {
  id?: number;

  constructor(id?: number) {
    this.id = id;
  }
}

class AutorIdInput {
  id?: number;

  constructor(id?: number) {
    this.id = id;
  }
}

class LivroInput {
  isbn?: string;
  titulo?: string;
  subtitulo?: string;
  idioma?: string;
  serieColecao?: string;
  volume?: number;
  tradutor?: string;
  ano?: number;
  edicao?: number;
  paginas?: number;
  sinopse?: string;
  editora?: EditoraIdInput;
  genero?: GeneroIdInput;
  autor?: AutorIdInput;

  constructor(isbn?: string,
    titulo?: string,
    subtitulo?: string,
    idioma?: string,
    serieColecao?: string,
    volume?: number,
    tradutor?: string,
    ano?: number,
    edicao?: number,
    paginas?: number,
    sinopse?: string,
    editora?: EditoraIdInput,
    genero?: GeneroIdInput,
    autor?: AutorIdInput) {

    this.isbn = isbn;
    this.titulo = titulo
    this.subtitulo = subtitulo;
    this.idioma = idioma;
    this.serieColecao = serieColecao;
    this.volume = volume;
    this.tradutor = tradutor;
    this.ano = ano;
    this.edicao = edicao;
    this.paginas = paginas;
    this.sinopse = sinopse
    this.editora = editora;
    this.genero = genero;
    this.autor = autor;
  }
}

@Injectable({
  providedIn: 'root'
})
export class LivroService {

  private readonly API = `${environment.API}livros`;

  constructor(
    private http: HttpClient
  ) { }


  listAll() {
    return this.http.get<Livro[]>(this.API)
      .pipe(
        //delay(2000),
        map((response: any) => {
          const livros = response['content'];
          return livros;
        }),
        take(1)
      );
  }

  findByFilter(filter: LivroFilter) {
    let params = new HttpParams()
      .set('page', filter.pagina)
      .set('size', filter.itensPorPagina);

    if (filter.titulo) {
      params = params.set('titulo', filter.titulo);
    }

    return this.http.get<LivroPage>(this.API, { params })
      .pipe(
        map((response: any) => {
          const resultado: LivroPage = {
            livros: response['content'],
            totalElements: response['totalElements'],
            totalPages: response['totalPages']
          }
          return resultado;
        }),
        take(1)
      );
  }

  findById(id: number) {
    return this.http.get<Livro>(`${this.API}/${id}`).pipe(take(1));
  }

  save(record: Partial<Livro>) {
    const input = this.parseToInput(record);
    if (record.id) {
      return this.update(record.id, input);
    } else {
      return this.create(input);
    }

  }

  remove(codigo: number) {
    return this.http.delete(`${this.API}/${codigo}`).pipe(take(1));
  }

  getFoto(codigo: number) {

    let headers = new HttpHeaders().set('Accept', "image/jpeg");

    return this.http.get(`${this.API}/${codigo}/foto`, { headers, responseType: 'blob' }).pipe(take(1));

  }

  uploadFoto(livroId: number, foto: File) {
    const url = `${this.API}/${livroId}/foto`;

    const formData = new FormData();
    formData.append('arquivo', foto);
    formData.append('descricao', `Capa_${livroId}`);

    return this.http.put(url, formData).pipe(take(1));

  }

  removeFoto(id: number) {
    const url = `${this.API}/${id}/foto`;

    return this.http.delete(url).pipe(take(1));
  }

  private create(record: Partial<LivroInput>) {
    return this.http.post(this.API, record).pipe(take(1));
  }

  private update(id: number, record: Partial<LivroInput>) {
    return this.http.put<Livro>(`${this.API}/${id}`, record).pipe(take(1));
  }

  private parseToInput(record: Partial<Livro>) : LivroInput {
    const input = new LivroInput(record.isbn, record.titulo, record.subtitulo, record.idioma, record.serieColecao,
      record.volume, record.tradutor, record.ano, record.edicao, record.paginas, record.sinopse,
      new EditoraIdInput(record.editora?.id), new GeneroIdInput(record.genero?.id), new AutorIdInput(record.autor?.id));

      return input;
  }


}
