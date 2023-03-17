import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, take } from 'rxjs';

import { environment } from './../../environments/environment';
import { Editora } from './../core/models/model';

export class EditoraFilter {
  nome?: string;
  pagina: number;
  itensPorPagina: number;

  constructor(pagina: number, itensPorPagina: number, nome?: string) {
    this.pagina = pagina;
    this.itensPorPagina = itensPorPagina;
    this.nome = nome;
  }
}

@Injectable({
  providedIn: 'root'
})
export class EditoraService {

  private readonly API = `${environment.API}editoras`;

  constructor(
    private http: HttpClient
  ) { }

  listAll() {
    return this.http.get<Editora[]>(this.API)
      .pipe(
        take(1),
        map((response: any) => {
          const editoras = response['content'];
          return editoras;
        })
      );
  }

  findByFilter(filterEditora: EditoraFilter) {
    let params = new HttpParams()
      .set('page', filterEditora.pagina)
      .set('size', filterEditora.itensPorPagina);

    if (filterEditora.nome) {
      params = params.set('nome', filterEditora.nome);
    }

    return this.http.get<Editora[]>(this.API, { params })
      .pipe(
        take(1),
        map((response: any) => {
          const editoras = response['content'];
          const resultado = {
            editoras,
            totalElements: response['totalElements'],
            totalPages: response['totalPages']
          }
          return resultado;
        })
      );
  }

  findById(id: number) {
    return this.http.get<Editora>(`${this.API}/${id}`).pipe(take(1));
  }

  save(record: Partial<Editora>) {
    if (record.id) {
      return this.update(record);
    } else {
      return this.create(record);
    }
  }

  remove(codigo: number) {
    return this.http.delete(`${this.API}/${codigo}`).pipe(take(1));
  }

  private create(record: Partial<Editora>) {
    return this.http.post(this.API, record).pipe(take(1));
  }

  private update(record: Partial<Editora>) {
    return this.http.put<Editora>(`${this.API}/${record.id}`, record).pipe(take(1));
  }

}
