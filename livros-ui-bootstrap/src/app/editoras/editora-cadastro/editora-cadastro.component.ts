import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { ToastService } from './../../shared/toast.service';
import { EditoraService } from './../editora.service';
import { Editora } from '../../core/models/editora/editora';
import { Observable, catchError, first, of, tap } from 'rxjs';


@Component({
  selector: 'app-editora-cadastro',
  templateUrl: './editora-cadastro.component.html',
  styleUrls: ['./editora-cadastro.component.css'],
  preserveWhitespaces: true
})
export class EditoraCadastroComponent implements OnInit {

  editora$: Observable<Editora|any> | null = null;

  form: FormGroup = this.formBuilder.group({
    id: [],
    nome: [null, [Validators.required, Validators.maxLength(60)]],
    urlSiteOficial: [null, [Validators.maxLength(100)]],
    urlFacebook: [null, [Validators.maxLength(100)]],
    urlTwitter: [null, [Validators.maxLength(100)]],
    urlWikipedia: [null, [Validators.maxLength(100)]]
  });

  constructor(
    private formBuilder: FormBuilder,
    private editoraService: EditoraService,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
    private location: Location
  ) {
    const codigoEditora = this.route.snapshot.params['codigo'];
    this.carregarRegistro(codigoEditora);
   }

  ngOnInit(): void {
    this.title.setTitle('Nova Editora');
  }

  get editando() {
    return Boolean(this.form.value.id);
  }

  onSubmit() {
    const descricaoOpercao = this.editando ? 'atualizada' : 'cadastrada';
    this.editoraService.save(this.form.value).subscribe({
      next: (result) => {
        this.toastService.showSuccessToast(`Editora ${descricaoOpercao} com sucesso!`);
        this.location.back();
      },
      error: (e) => {
        this.errorHandlerService.handle(e);
      }
    });
  }

  onCancel() {
    this.location.back();
  }

  private carregarRegistro(codigo?: number) {
    if (!codigo) {
      this.editora$ = of({});
    } else {
      this.editora$ = this.editoraService.findById(codigo)
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

  private popularForm(editora: Editora) {
    this.form.setValue({
      id: editora.id,
      nome: editora.nome,
      urlSiteOficial: editora.urlSiteOficial,
      urlFacebook: editora.urlFacebook,
      urlTwitter: editora.urlTwitter,
      urlWikipedia: editora.urlWikipedia
    });
  }

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição editora: ${this.form.value.nome}`);
  }




}
