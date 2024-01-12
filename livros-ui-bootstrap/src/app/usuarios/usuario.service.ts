import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { UsuarioPage } from '../core/models/usuario/usuario-page';
import { catchError, delay, first, map, take } from 'rxjs';
import { Usuario } from '../core/models/usuario/usuario';
import { AlterarSenha } from '../core/models/usuario/alterar-senha';


export class UsuarioFilter {
  nome?: string;
  page: number;
  itensPorPagina: number;

  constructor(page: number, itensPorPagina: number, nome?: string) {
    this.page = page;
    this.itensPorPagina = itensPorPagina;
    this.nome = nome;
  }
}

class GrupoIdInput {
  id?: number;

  constructor(id?: number) {
    this.id = id;
  }
}

class UsuarioInput {
  nome?: string;
  email?: string;
  grupo?: GrupoIdInput;

  constructor(nome?: string, email?: string,
    grupo?: GrupoIdInput) {
    this.nome = nome;
    this.email = email;
    this.grupo = grupo;
  }

}

class UsuarioInputComSenha extends UsuarioInput {
  senha?: string;

  constructor(nome?: string, email?: string,
    grupo?: GrupoIdInput, senha?: string) {
    super(nome, email, grupo);
    this.senha = senha;
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

  save(record: Partial<Usuario>) {
    if (record.id) {
      const input = this.parseToInput(record);
      return this.update(record.id, input);
    } else {
      const input = this.parseToInputComSenha(record);
      return this.create(input);
    }
  }

  findById(id: number) {
    return this.http.get<Usuario>(`${this.API}/${id}`).pipe(first());
  }

  remove(id: number) {
    return this.http.delete<void>(`${this.API}/${id}`).pipe(first());
  }

  alterarSenhaUsuario(idUsuario: number, record: AlterarSenha) {
    const senhaInput = {
      senhaAtual: record.senhaAtual,
      novaSenha: record.novaSenha
    }
    return this.http.put<void>(`${this.API}/${idUsuario}/senha`, senhaInput).pipe(take(1));
  }

  private create(record: Partial<UsuarioInputComSenha>) {
    return this.http.post(this.API, record).pipe(take(1));
  }

  private update(id: number, record: Partial<UsuarioInput>) {
    return this.http.put<Usuario>(`${this.API}/${id}`, record).pipe(take(1));
  }

  private parseToInputComSenha(record: Partial<Usuario>): UsuarioInputComSenha {
    return new UsuarioInputComSenha(record.nome, record.email, 
      new GrupoIdInput(record.grupo?.id), record.senha);
  }

  private parseToInput(record: Partial<Usuario>): UsuarioInput {
    return new UsuarioInput(record.nome, record.email, 
      new GrupoIdInput(record.grupo?.id));
  }

}
