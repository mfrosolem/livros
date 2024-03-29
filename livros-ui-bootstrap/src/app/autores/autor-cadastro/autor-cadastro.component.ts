import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { ToastService } from './../../shared/toast.service';
import { AutorService } from './../autor.service';
import { Autor } from '../../core/models/autor/autor';
import { Observable, catchError, first, of, tap } from 'rxjs';

@Component({
  selector: 'app-autor-cadastro',
  templateUrl: './autor-cadastro.component.html',
  styleUrls: ['./autor-cadastro.component.css'],
  preserveWhitespaces: true
})
export class AutorCadastroComponent implements OnInit {

  autor$: Observable<Autor|any> | null = null;

  form: FormGroup = this.formBuilder.group({
    id: [],
    nome: [null, [Validators.required, Validators.maxLength(30)]],
    sobrenome: [null, [Validators.required, Validators.maxLength(40)]],
    nomeConhecido: [null, [Validators.maxLength(60)]],
    sexo: ['M'],
    dataNascimento: [null],
    dataFalecimento: [null],
    paisNascimento: [null],
    estadoNascimento: [null],
    cidadeNascimento: [null],
    biografia: [null],
    urlSiteOficial: [null, [Validators.maxLength(100)]],
    urlFacebook: [null, [Validators.maxLength(100)]],
    urlTwitter: [null, [Validators.maxLength(100)]],
    urlWikipedia: [null, [Validators.maxLength(100)]]
  });

  constructor(
    private formBuilder: FormBuilder,
    private autorService: AutorService,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private location: Location,
    private route: ActivatedRoute,
    private title: Title
  ) {
    const codigoAutor = this.route.snapshot.params['codigo'];
    this.carregarRegistro(codigoAutor);
  }

  ngOnInit(): void {
    this.title.setTitle('Novo Autor');
  }

  get editando() {
    return Boolean(this.form.value.id);
  }


  onSubmit() {
    const descricaoOpercao = this.editando ? 'atualizado(a)' : 'cadastrado(a)';
    this.autorService.save(this.form.value)
      .subscribe({
        next: (result) => {
          this.toastService.showSuccessToast(`Autor(a) ${descricaoOpercao} com sucesso!`);
          this.onCancel();
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        }
      });
  }

  onCancel() {
    this.location.back();
  }

  private carregarRegistro(codigo?: number) {
    if (!codigo) {
      this.autor$ = of({});
    } else {
      this.autor$ = this.autorService.findById(codigo)
        .pipe(
          first(),
          tap(resultado => {
            this.form.patchValue(resultado);
            this.atualizarTituloEdicao();
          }),
          catchError(falha => {
            this.errorHandlerService.handle(falha);
            this.onCancel();
            return of({});
          })
        );
    }
  }

  private popularForm(autor: Autor) {
    this.form.setValue({
      id: autor.id,
      nome: autor.nome,
      sobrenome: autor.sobrenome,
      nomeConhecido: autor.nomeConhecido,
      sexo: autor.sexo,
      dataNascimento: autor.dataNascimento,
      dataFalecimento: autor.dataFalecimento,
      paisNascimento: autor.paisNascimento,
      estadoNascimento: autor.estadoNascimento,
      cidadeNascimento: autor.cidadeNascimento,
      biografia: autor.biografia,
      urlSiteOficial: autor.urlSiteOficial,
      urlFacebook: autor.urlFacebook,
      urlTwitter: autor.urlTwitter,
      urlWikipedia: autor.urlWikipedia
    });
  }

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição autor(a): ${this.form?.value?.nome}`);
  }


}
