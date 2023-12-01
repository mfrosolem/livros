import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { UsuarioPage } from '../core/models/usuario/usuario-page';
import { catchError, delay, first, map } from 'rxjs';
import { Usuario } from '../core/models/usuario/usuario';


export class UsuarioFilter{
  nome?: string;
  page: number;
  itensPorPagina: number;

  constructor(page: number, itensPorPagina: number, nome?: string) {
    this.page = page;
    this.itensPorPagina = itensPorPagina;
    this.nome = nome;
  }
}


@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private readonly API = `${environment.API}usuarios`;

  constructor(private http: HttpClient) { }


  findByFilter(filterUsuario: UsuarioFilter) {
    let params = new HttpParams()
    .set('page', filterUsuario.page)
    .set('size', filterUsuario.itensPorPagina);

    if (filterUsuario.nome) {
      params = params.set('nome', filterUsuario.nome);
    }

    return this.http.get<UsuarioPage>(this.API, { params }).pipe(
      map((response: any) => {
        const resultado: UsuarioPage = {
          usuarios: response['content'],
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
    return this.http.get<Usuario>(`${this.API}/${id}`).pipe(first());
  }

  remove(id: number) {
    return this.http.delete<void>(`${this.API}/${id}`).pipe(first());
  }

}
