import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, first, map, take } from 'rxjs';

import { environment } from './../../environments/environment';
import { Genero } from '../core/models/genero/genero';
import { GeneroPage } from '../core/models/genero/genero-page';


export class GeneroFilter {
  descricao?: string;
  pagina: number;
  itensPorPagina: number;

  constructor(pagina: number, itensPorPagina: number, descricao?: string) {
    this.pagina = pagina;
    this.itensPorPagina = itensPorPagina;
    this.descricao = descricao;
  }
}

class GeneroInput {
  descricao?: string;
  constructor(descricao?: string) {
    this.descricao = descricao;
  }
}

@Injectable({
  providedIn: 'root'
})
export class GeneroService {

  private readonly API = `${environment.API}generos`;

  constructor(
    private http: HttpClient
  ) { }

  listAll() {
    return this.http.get<Genero[]>(this.API)
      .pipe(
        map((response: any) => {
          const generos = response['content'];
          return generos;
        }),
        // delay(2000),
        take(1)
      );
  }

  findByFilter(filterGenero: GeneroFilter) {
    let params = new HttpParams()
      .set('page', filterGenero.pagina)
      .set('size', filterGenero.itensPorPagina);

    if (filterGenero.descricao) {
      params = params.set('descricao', filterGenero.descricao);
    }

    return this.http.get<GeneroPage>(this.API, { params }).pipe(
      map((response: any) => {
        const resultado: GeneroPage = {
          generos: response['content'],
          totalElements: response['totalElements'],
          totalPages: response['totalPages']
        }
        return resultado;
      }),      
      first()
      // ,delay(5000)
    );

  }

  findById(id: number) {
    return this.http.get<Genero>(`${this.API}/${id}`).pipe(take(1));
  }

  save(record: Partial<Genero>) {
    const generoSalvar = new GeneroInput(record.descricao);
    if (record.id) {
      return this.update(record.id, generoSalvar);
    } else {
      return this.create(generoSalvar);
    }
  }

  remove(id: number) {
    return this.http.delete<void>(`${this.API}/${id}`).pipe(take(1));
  }

  private create(record: GeneroInput) {
    return this.http.post<Genero>(this.API, record).pipe(take(1));
  }

  private update(id: number, record: GeneroInput) {
    return this.http.put<Genero>(`${this.API}/${id}`, record).pipe(take(1));
  }

}
