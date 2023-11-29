import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Grupo } from '../core/models/model';
import { map, take } from 'rxjs';


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

    return this.http.get<Grupo>(this.API, { params }).pipe(
      map((response: any) => {
        const grupos = response['content'];
        const resultado = {
          grupos,
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

  remove(codigo: number) {
    return this.http.delete<void>(`${this.API}/${codigo}`).pipe(take(1));
  }

  private create(record: GrupoInput) {
    return this.http.post<Grupo>(`${this.API}`, record).pipe(take(1));
  }

  private update(codigo: number, record: GrupoInput) {
    return this.http.put<Grupo>(`${this.API}/${codigo}`, record).pipe(take(1));
  }



}