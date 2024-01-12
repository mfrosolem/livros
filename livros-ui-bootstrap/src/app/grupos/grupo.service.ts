import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, take, first } from 'rxjs';

import { Grupo } from '../core/models/grupo/grupo';
import { GrupoPage } from '../core/models/grupo/grupo-page';
import { Permissao } from '../core/models/permissao/permissao';
import { environment } from 'src/environments/environment';


export class GrupoFilter{
  nome?: string;
  pagina: number;
  itensPorPagina: number;

  constructor(pagina: number, itensPorPagina: number, nome?: string) {
    this.pagina = pagina;
    this.itensPorPagina = itensPorPagina;
    this.nome = nome;
  }
}

class GrupoInput {
  nome?: string;
  constructor(nome?: string) {
    this.nome = nome;
  }
}

@Injectable({
  providedIn: 'root'
})
export class GrupoService {

  private readonly API = `${environment.API}grupos`;

  constructor(private http: HttpClient) { }

  listAll() {
    return this.http.get<Grupo[]>(this.API).pipe(
      map((response: any) => {
        const grupos = response['content'];
        return grupos;
      }),
      take(1)
    );
  }

  findByFilter(filterGrupo: GrupoFilter) {
    let params = new HttpParams()
      .set('page', filterGrupo.pagina)
      .set('size', filterGrupo.itensPorPagina);

    if (filterGrupo.nome) {
      params = params.set('nome', filterGrupo.nome);
    }

    return this.http.get<GrupoPage>(this.API, { params }).pipe(
      map((response: any) => {
        const resultado: GrupoPage = {
          grupos: response['content'],
          totalElements: response['totalElements'],
          totalPages: response['totalPages']
        }
        return resultado;
      }),
      take(1)
    );
  }

  findById(id: number) {
    return this.http.get<Grupo>(`${this.API}/${id}`).pipe(take(1));
  }

  save(record: Partial<Grupo>) {
    const grupoSalvar = new GrupoInput(record.nome);
    if (record.id) {
      return this.update(record.id, grupoSalvar);
    } else {
      return this.create(grupoSalvar);
    }
  }

  remove(idGrupo: number) {
    return this.http.delete<void>(`${this.API}/${idGrupo}`).pipe(take(1));
  }

  getPermissoes(idGrupo: number) {
    return this.http.get<Permissao[]>(`${this.API}/${idGrupo}/permissoes`).pipe(first());
  }

  attach(idGrupo: number, idPermissao: number) {
    return this.http.put<void>(`${this.API}/${idGrupo}/permissoes/${idPermissao}`, null)
      .pipe(first());
  }

  detach(idGrupo: number, idPermissao: number) {
    return this.http.delete<void>(`${this.API}/${idGrupo}/permissoes/${idPermissao}`)
      .pipe(first());
  }

  private create(record: GrupoInput) {
    return this.http.post<Grupo>(`${this.API}`, record).pipe(take(1));
  }

  private update(codigo: number, record: GrupoInput) {
    return this.http.put<Grupo>(`${this.API}/${codigo}`, record).pipe(take(1));
  }

}
