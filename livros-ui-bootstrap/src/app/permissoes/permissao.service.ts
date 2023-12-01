import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { map, take } from 'rxjs';
import { Permissao } from '../core/models/permissao/permissao';
import { PermissaoPage } from '../core/models/permissao/permissao-page';

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

    return this.http.get<PermissaoPage>(this.API, { params }).pipe(
      map((response: any) => {
        const resultado: PermissaoPage = {
          permissoes: response['content'],
          totalElements: response['totalElements'],
          totalPages: response['totalPages']
        }
        return resultado;
      }),
      take(1)
    );
  }


}
