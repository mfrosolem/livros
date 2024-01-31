import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { ToastService } from './../../shared/toast.service';
import { GeneroService } from './../genero.service';
import { Genero } from '../../core/models/genero/genero';
import { Observable, catchError, first, of, take, tap } from 'rxjs';



@Component({
  selector: 'app-genero-cadastro',
  templateUrl: './genero-cadastro.component.html',
  styleUrls: ['./genero-cadastro.component.css'],
  preserveWhitespaces: true
})
export class GeneroCadastroComponent implements OnInit {

  genero$: Observable<Genero|any> | null = null;

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
  ) {
    const idGenero = this.route.snapshot.params['codigo'];
    this.carregarRegistro(idGenero);   
   }

  ngOnInit(): void {
    this.title.setTitle('Novo Gênero');
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
      .pipe(
        take(1)
      )
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

  private carregarRegistro(codigo?: number) {
    if (!codigo) {
      this.genero$ = of({});
    } else {
      this.genero$ = this.generoService.findById(codigo)
        .pipe(
          first(),
          tap(resultado => {
            this.form.patchValue(resultado);
            this.atualizarTituloEdicao();
          }),
          catchError(falha => {
            this.errorHandler.handle(falha);
            this.onCancel();
            return of({});
          })
        );
    }
  }

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição gênero: ${this.form.value.descricao}`);
  }

}
