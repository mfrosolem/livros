import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, take, tap, firstValueFrom } from 'rxjs';

import { environment } from './../../environments/environment';
import { Genero } from './../core/models/model';

export class GeneroFilter {
  descricao?: string;
  pagina = 0;
  itensPorPagina = 5;
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
        //delay(2000),
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

    return this.http.get<Genero>(this.API, { params }).pipe(
      map((response: any) => {
        const generos = response['content'];
        const resultado = {
          generos,
          totalElements: response['totalElements'],
          totalPages: response['totalPages']
        }
        return resultado;
      }),
      take(1)
    );
  }

  findById(id: number) {
    return this.http.get<Genero>(`${this.API}/${id}`).pipe(take(1));
  }

  save(record: Partial<Genero>) {
    if (record.id) {
      return this.update(record);
    } else {
      return this.create(record);
    }
  }

  remove(codigo: number) {
    return this.http.delete<void>(`${this.API}/${codigo}`).pipe(take(1));
  }

  private create(record: Partial<Genero>) {
    return this.http.post<Genero>(this.API, record).pipe(take(1));
  }

  private update(record: Partial<Genero>) {
    return this.http.put<Genero>(`${this.API}/${record.id}`, record).pipe(take(1));
  }

}