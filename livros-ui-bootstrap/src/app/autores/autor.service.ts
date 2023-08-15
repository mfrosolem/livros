import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, take, Observable } from 'rxjs';

import { environment } from './../../environments/environment';
import { Autor } from './../core/models/model';

export class AutorFilter {
  nome?: string;
  pagina: number;
  itensPorPagina: number;

  constructor(pagina: number, itensPorPagina: number, nome?: string) {
    this.pagina = pagina;
    this.itensPorPagina = itensPorPagina;
    this.nome = nome;
  }
}

class AutorInput {
  nome?: string;
  sobrenome?: string;
  nomeConhecido?: string;
  sexo?: string;
  dataNascimento?: Date;
  dataFalecimento?: Date;
  paisNascimento?: string;
  estadoNascimento?: string;
  cidadeNascimento?: string;
  biografia?: string;
  urlSiteOficial?: string;
  urlFacebook?: string;
  urlTwitter?: string;
  urlWikipedia?: string;

  constructor(nome?: string,
    sobrenome?: string,
    nomeConhecido?: string,
    sexo?: string,
    dataNascimento?: Date,
    dataFalecimento?: Date,
    paisNascimento?: string,
    estadoNascimento?: string,
    cidadeNascimento?: string,
    biografia?: string,
    urlSiteOficial?: string,
    urlFacebook?: string,
    urlTwitter?: string,
    urlWikipedia?: string) {

    this.nome = nome;  
    this.sobrenome = sobrenome;
    this.nomeConhecido = nomeConhecido;
    this.sexo = sexo;
    this.dataNascimento = dataNascimento;
    this.dataFalecimento = dataFalecimento;
    this.paisNascimento = paisNascimento;
    this.estadoNascimento = estadoNascimento;
    this.cidadeNascimento = cidadeNascimento;
    this.biografia = biografia;
    this.urlSiteOficial = urlSiteOficial;
    this.urlFacebook = urlFacebook;
    this.urlTwitter = urlTwitter;
    this.urlWikipedia = urlWikipedia;

  }
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
        take(1),
        map((response: any) => {
          const autores = response['content'];
          return autores;
        }),

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
        take(1),
        map((response: any) => {
          const autores = response['content'];
          const resultado = {
            autores,
            totalElements: response['totalElements'],
            totalPages: response['totalPages']
          }
          return resultado;
        })
      );
  }

  findById(id: number) {
    return this.http.get<Autor>(`${this.API}/${id}`).pipe(take(1));
  }

  save(record: Partial<Autor>) {
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


  private create(record: AutorInput) {
    return this.http.post<Autor>(this.API, record).pipe(take(1));
  }

  private update(id: number, record: AutorInput) {
    return this.http.put<Autor>(`${this.API}/${id}`, record).pipe(take(1));
  }

  private parseToInput(record: Partial<Autor>) : AutorInput {
    const input = new AutorInput(record.nome, record.sobrenome, record.nomeConhecido, record.sexo, record.dataNascimento,
      record.dataFalecimento, record.paisNascimento, record.estadoNascimento, record.cidadeNascimento, record.biografia,
      record.urlSiteOficial, record.urlFacebook, record.urlTwitter, record.urlWikipedia);

    return input;
  }


}
