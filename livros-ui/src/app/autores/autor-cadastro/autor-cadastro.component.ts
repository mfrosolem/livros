import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { Autor } from './../../core/models/model';
import { ToastService } from './../../shared/toast.service';
import { AutorService } from './../autor.service';

@Component({
  selector: 'app-autor-cadastro',
  templateUrl: './autor-cadastro.component.html',
  styleUrls: ['./autor-cadastro.component.css'],
  preserveWhitespaces: true
})
export class AutorCadastroComponent implements OnInit {

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
    ultTwitter: [null, [Validators.maxLength(100)]],
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

  }

  ngOnInit(): void {
    const codigoAutor = this.route.snapshot.params['codigo'];

    this.title.setTitle('Novo Autor');

    if (codigoAutor) {
      this.carregarRegistro(codigoAutor);
    }

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

  private carregarRegistro(codigo: number) {
    this.autorService.findById(codigo)
      .subscribe({
        next: (autorRetornado: Autor) => {
          this.form.patchValue(autorRetornado);
          this.atualizarTituloEdicao();
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        }
      });
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
      ultTwitter: autor.ultTwitter,
      urlWikipedia: autor.urlWikipedia
    });
  }

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição autor(a): ${this.form?.value?.nome}`);
  }


}
