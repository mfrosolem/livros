import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, take } from 'rxjs';
import { environment } from 'src/environments/environment';

import { Livro } from './../core/models/model';

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

    return this.http.get<Livro[]>(this.API, { params })
      .pipe(
        map((response: any) => {
          const livros = response['content'];
          const resultado = {
            livros,
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

    if (record.id) {
      return this.update(record);
    } else {
      return this.create(record);
    }

  }

  remove(codigo: number) {
    return this.http.delete(`${this.API}/${codigo}`).pipe(take(1));
  }

  getFoto(codigo: number) {

    let headers = new HttpHeaders().set('Accept', "image/jpeg");

    return this.http.get(`${this.API}/${codigo}/foto`, { headers, responseType: 'blob' }).pipe(take(1));

  }

  uploadFoto(livro: Livro, foto: File) {
    const url = `${this.API}/${livro.id}/foto`;

    const formData = new FormData();
    formData.append('arquivo', foto);
    formData.append('descricao', `Capa_${livro.id}`);

    return this.http.put(url, formData).pipe(take(1));

  }

  removeFoto(codigo: number) {
    const url = `${this.API}/${codigo}/foto`;

    return this.http.delete(url).pipe(take(1));
  }

  private create(record: Partial<Livro>) {
    return this.http.post(this.API, record).pipe(take(1));
  }

  private update(record: Partial<Livro>) {
    return this.http.put<Livro>(`${this.API}/${record.id}`, record).pipe(take(1));
  }


}
