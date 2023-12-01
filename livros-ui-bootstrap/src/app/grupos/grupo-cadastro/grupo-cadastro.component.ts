import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GrupoService } from '../grupo.service';
import { ErrorHandlerService } from '../../core/error-handler.service';
import { ToastService } from '../../shared/toast.service';
import { ActivatedRoute } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Location } from '@angular/common';
import { Grupo } from '../../core/models/model';

@Component({
  selector: 'app-grupo-cadastro',
  templateUrl: './grupo-cadastro.component.html',
  styleUrls: ['./grupo-cadastro.component.css'],
  preserveWhitespaces: true
})
export class GrupoCadastroComponent implements OnInit{

  form: FormGroup = this.formBuilder.group({
    id: [],
    nome: [null, [Validators.required, Validators.maxLength(60) ] ]
  });

  constructor(
    private grupoService: GrupoService,
    private formBuilder: FormBuilder,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private route: ActivatedRoute,
    private title: Title,
    private location: Location
  ) {
    const idGrupo = this.route.snapshot.params['codigo'];

    if (idGrupo) {
      console.log(idGrupo);
      this.carregarRegistro(idGrupo);
    }
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
          this.location.back();
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        },
      });
  }

  private carregarRegistro(codigo: number) {
    this.grupoService.findById(codigo)
      .subscribe({
        next: (grupoRetornado: Grupo) => {
          this.form.patchValue(grupoRetornado);
          this.atualizarTituloEdicao();
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        },
      });
  }

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição grupo: ${this.form.value.nome}`);
  }

}
