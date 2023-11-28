import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Permissao } from '../core/models/model';
import { map, take } from 'rxjs';

export class PermissaoFilter {
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
export class PermissaoService {

  private readonly API = `${environment.API}permissoes`;

  constructor(private http: HttpClient) { }

  listAll() {
    return this.http.get<Permissao>(this.API).pipe(
      map((response: any) => {
        const permissoes = response['content'];
        return permissoes;
      }),
      take(1)
    );
  }

  findByFilter(filterPermissao: PermissaoFilter) {
    let params = new HttpParams()
    .set('page', filterPermissao.pagina)
    .set('size', filterPermissao.itensPorPagina);

    if (filterPermissao.nome) {
      params = params.set('nome', filterPermissao.nome);
    }

    return this.http.get<Permissao>(this.API, { params }).pipe(
      map((response: any) => {
        const permissoes = response['content'];
        const resultado = {
          permissoes,
          totalElements: response['totalElements'],
          totalPages: response['totalPages']
        }
        return resultado;
      }),
      take(1)
    );
  }


}
