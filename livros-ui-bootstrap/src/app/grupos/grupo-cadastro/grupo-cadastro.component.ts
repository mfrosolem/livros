import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Location } from '@angular/common';
import { Observable, catchError, first, of, tap } from 'rxjs';

import { GrupoService } from '../grupo.service';
import { PermissaoService } from '../../permissoes/permissao.service';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { ToastService } from '../../shared/toast.service';
import { Grupo } from '../../core/models/grupo/grupo';
import { Permissao } from '../../core/models/permissao/permissao';

@Component({
  selector: 'app-grupo-cadastro',
  templateUrl: './grupo-cadastro.component.html',
  styleUrls: ['./grupo-cadastro.component.css'],
  preserveWhitespaces: true
})
export class GrupoCadastroComponent implements OnInit{

  grupo$: Observable<Grupo|any> | null = null;

  form: FormGroup = this.formBuilder.group({
    id: [],
    nome: [null, [Validators.required, Validators.maxLength(60) ] ]
  });

  permissoesGrupo$: Observable<Permissao[]|any> | null = null;
  permissoes$: Observable<Permissao[]|any> | null = null;

  constructor(
    private grupoService: GrupoService,
    private permissaoService: PermissaoService,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private route: ActivatedRoute,
    private title: Title,
    private location: Location
  ) {
    const idGrupo = this.route.snapshot.params['codigo'];
    this.carregarRegistro(idGrupo);  
    this.getGrupoPermissoes(idGrupo);
    this.getPermissoes();
    
  }


  ngOnInit(): void {
   this.title.setTitle('Novo Grupo');
  }

  get editando() {
    return Boolean(this.form.value.id);
  }

  onCancel() {
    this.location.back();
  }

  onSubmit() {
    const descricaoOperacao = this.editando ? 'atualizado' : 'cadastrado';
    this.grupoService.save(this.form.value)
      .subscribe({
        next: (grupoAdicionado) => {
          this.toastService.showSuccessToast(`Grupo ${descricaoOperacao} com sucesso!`);
          //this.location.back();
          this.form.patchValue(grupoAdicionado);
          this.atualizarTituloEdicao();
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        },
      });
  }

  onAttach(permissao: Permissao) {
    if (this.editando) {
      this.grupoService.attach(this.form.value.id, permissao.id)
        .subscribe({
          next: () => {
            this.toastService.showSuccessToast(`Permissão ${permissao.nome} vinculada com sucesso!`);
            this.getGrupoPermissoes(this.form.value.id);
          },
          error: (falha) => {
            this.errorHandlerService.handle(falha);
          }
        });
    }
  }

  onDetach(permissao: Permissao) {
    if (this.editando) {
      this.grupoService.detach(this.form.value.id, permissao.id)
        .subscribe({
          next: () => {
            this.toastService.showSuccessToast(`Permissão ${permissao.nome} desvinculada com sucesso!`);
            this.getGrupoPermissoes(this.form.value.id);
          },
          error: (falha) => {
            this.errorHandlerService.handle(falha);
          }
        });
    }
  }

  private carregarRegistro(codigo?: number) {
    if (!codigo) {
      this.grupo$ = of({});
    } else {
      this.grupo$ = this.grupoService.findById(codigo)
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

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição grupo: ${this.form.value.nome}`);
  }

  private getGrupoPermissoes(codigo?: number) {
    if (!codigo) {
      this.permissoesGrupo$ = of([]);
    } else {
      this.permissoesGrupo$ = this.grupoService.getPermissoes(codigo)
        .pipe(
          first(),
          catchError(falha => {
            this.errorHandlerService.handle(falha);            
            return of([]);
          })
        );
    }
  }

  private getPermissoes() {
      this.permissoes$ = this.permissaoService.listAll()
        .pipe(
          first(),
          catchError(falha => {
            this.errorHandlerService.handle(falha);            
            return of([]);
          })
        );
  }


}
