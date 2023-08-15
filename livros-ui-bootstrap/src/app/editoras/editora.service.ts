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

class EditoraInput {

  nome?: string;
  urlSiteOficial?: string;
  urlFacebook?: string;
  urlTwitter?: string;
  urlWikipedia?: string;
  
  constructor(nome?: string, urlSiteOficial?: string, urlFacebook?: string,
    urlTwitter?: string, urlWikipedia?: string) {
      this.nome = nome;
      this.urlSiteOficial = urlSiteOficial;
      this.urlFacebook = urlFacebook;
      this.urlTwitter = urlTwitter;
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

  private create(record: EditoraInput) {
    return this.http.post(this.API, record).pipe(take(1));
  }

  private update(id: number, record: EditoraInput) {
    return this.http.put<Editora>(`${this.API}/${id}`, record).pipe(take(1));
  }

  private parseToInput(editora: Editora) : EditoraInput {
    const input = new EditoraInput(editora.nome, editora.urlSiteOficial, editora.urlFacebook,
      editora.urlTwitter, editora.urlWikipedia);
    return input;
  }

}
