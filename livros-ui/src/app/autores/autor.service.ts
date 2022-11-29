import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, take, tap } from 'rxjs';

import { environment } from './../../environments/environment';
import { Autor } from './../core/models/model';

export class AutorFilter {
  nome?: string;
  pagina = 0;
  itensPorPagina = 5;
}

@Injectable({
  providedIn: 'root'
})
export class AutorService {

  private readonly API = `${environment.API}autores`;

  constructor(
    private http: HttpClient
  ) { }

  listAll() {
    return this.http.get<Autor[]>(this.API)
      .pipe(
        //delay(2000),
        take(1)
      );
  }

  findByFilter(filterAutor: AutorFilter) {
    let params = new HttpParams()
      .set('page', filterAutor.pagina)
      .set('size', filterAutor.itensPorPagina);

    if (filterAutor.nome) {
      params = params.set('nome', filterAutor.nome);
    }

    return this.http.get<Autor[]>(this.API, { params })
      .pipe(
        map((response: any) => {
          const autores = response['content'];
          const resultado = {
            autores,
            totalElements: response['totalElements'],
            totalPages: response['totalPages']
          }
          return resultado;
        }),
        take(1)
      );
  }

  findById(id: number) {
    return this.http.get<Autor>(`${this.API}/${id}`).pipe(take(1));
  }

  save(record: Partial<Autor>) {
    if (record.id) {
      return this.update(record);
    } else {
      return this.create(record);
    }
  }


  remove(codigo: number) {
    return this.http.delete(`${this.API}/${codigo}`).pipe(tap(console.log), take(1));
  }


  private create(record: Partial<Autor>) {
    return this.http.post<Autor>(this.API, record).pipe(tap(console.log), take(1));
  }

  private update(record: Partial<Autor>) {
    return this.http.put<Autor>(`${this.API}/${record.id}`, record).pipe(tap(console.log), take(1));
  }


}
