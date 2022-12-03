import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { Genero } from './../../core/models/model';
import { ToastService } from './../../shared/toast.service';
import { GeneroService } from './../genero.service';



@Component({
  selector: 'app-genero-cadastro',
  templateUrl: './genero-cadastro.component.html',
  styleUrls: ['./genero-cadastro.component.css'],
  preserveWhitespaces: true
})
export class GeneroCadastroComponent implements OnInit {

  form: FormGroup = this.formBuilder.group({
    id: [],
    descricao: [null, [Validators.required, Validators.maxLength(60)]],
  });


  constructor(
    private formBuilder: FormBuilder,
    private generoService: GeneroService,
    private errorHandler: ErrorHandlerService,
    private toastService: ToastService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
    private location: Location
  ) { }

  ngOnInit(): void {

    const codigoGenero = this.route.snapshot.params['codigo'];

    this.title.setTitle('Novo Gênero');

    if (codigoGenero) {
      this.carregarRegistro(codigoGenero);
    }
  }

  get editando() {
    return Boolean(this.form.value.id);
  }

  onCancel() {
    this.location.back();
  }

  onSubmit() {
    const descricaoOpercao = this.editando ? 'atualizado' : 'cadastrado';
    this.generoService.save(this.form.value)
      .subscribe({
        next: (generoAdicionado) => {
          this.toastService.showSuccessToast(`Gênero ${descricaoOpercao} com sucesso!`);
          this.location.back();
        },
        error: (falha) => {
          this.errorHandler.handle(falha);
        },
      });
  }

  private carregarRegistro(codigo: number) {
    this.generoService.findById(codigo)
      .subscribe({
        next: (generoRetornado: Genero) => {
          this.form.patchValue(generoRetornado);
          this.atualizarTituloEdicao();
        },
        error: (falha) => {
          this.errorHandler.handle(falha);
        },
      });
  }

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição gênero: ${this.form.value.descricao}`);
  }



}
