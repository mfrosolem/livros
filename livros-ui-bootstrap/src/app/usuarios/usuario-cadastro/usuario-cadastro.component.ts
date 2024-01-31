import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Title } from '@angular/platform-browser';
import { Observable, catchError, first, map, of, take, tap } from 'rxjs';

import { UsuarioService } from '../usuario.service';
import { GrupoService } from '../../grupos/grupo.service';
import { ToastService } from '../../shared/toast.service';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { Usuario } from '../../core/models/usuario/usuario';
import { Grupo } from '../../core/models/grupo/grupo';


@Component({
  selector: 'app-usuario-cadastro',
  templateUrl: './usuario-cadastro.component.html',
  styleUrls: ['./usuario-cadastro.component.css'],
  preserveWhitespaces: true
})
export class UsuarioCadastroComponent implements OnInit {

  usuario$: Observable<Usuario|any> | null = null;
  grupos$: Observable<Grupo[]|any> | null = null;

  form: FormGroup = this.formBuilder.group({
    id: [],
    nome: [null, [Validators.required, Validators.maxLength(80)]],
    email: [null, [Validators.required, Validators.email]], //colocar pattern

    grupo: this.formBuilder.group({
      id: [null, Validators.required],
      nome: []
    })
  });

  constructor(
    private usuarioService: UsuarioService,
    private grupoService: GrupoService,
    private toastService: ToastService,
    private errorHandlerService: ErrorHandlerService,
    private formBuilder: FormBuilder,
    private title: Title,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {
    const idUsuario = this.route.snapshot.params['codigo'];
    this.carregarRegistro(idUsuario);

    this.carregarGrupos();

  }

  ngOnInit(): void {
    this.title.setTitle('Novo Usuário');
  }

  get editando() {
    return Boolean(this.form.value.id);
  }

  onCancel() {
    this.location.back();
  }

  onSubmit() {
    const descricaoOperacao = this.editando ? 'atualizado' : 'cadastrado';
    this.usuarioService.save(this.form.value)
    .pipe(
      take(1)
    )
    .subscribe({
      next: (result) => {
        this.toastService.showSuccessToast(`Usuário ${descricaoOperacao} com sucesso!`);
        this.location.back();
      },
      error: (e) => {
        this.errorHandlerService.handle(e);
      }
    });
  }

  private carregarRegistro(codigo?: number) {
    if (!codigo) {
      this.usuario$ = of({});
    } else {
      this.usuario$ = this.usuarioService.findById(codigo)
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
      )
    }
  }

  private carregarGrupos() {
    this.grupos$ = this.grupoService.listAll()
      .pipe(
        first(),
        catchError(falha => {
          this.errorHandlerService.handle(falha);
          return of([]);
        })
      );
  }

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição usuário: `);
  }

}
