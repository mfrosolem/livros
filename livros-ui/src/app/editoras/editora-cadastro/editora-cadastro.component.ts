import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NgForm, NonNullableFormBuilder, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { ErrorHandlerService } from './../../core/error-handler.service';
import { Editora } from './../../core/models/model';
import { ToastService } from './../../shared/toast.service';
import { EditoraService } from './../editora.service';


@Component({
  selector: 'app-editora-cadastro',
  templateUrl: './editora-cadastro.component.html',
  styleUrls: ['./editora-cadastro.component.css'],
  preserveWhitespaces: true
})
export class EditoraCadastroComponent implements OnInit {


  form = this.formBuilder.group({
    id: [0],
    nome: ['', [Validators.required, Validators.maxLength(60)]],
    urlSiteOficial: ['', [Validators.maxLength(100)]],
    urlFacebook: ['', [Validators.maxLength(100)]],
    ultTwitter: ['', [Validators.maxLength(100)]],
    urlWikipedia: ['', [Validators.maxLength(100)]]
  });

  constructor(
    private formBuilder: NonNullableFormBuilder,
    private editoraService: EditoraService,
    private errorHandlerService: ErrorHandlerService,
    private toastService: ToastService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
    private location: Location
  ) { }

  ngOnInit(): void {

    const codigoEditora = this.route.snapshot.params['codigo'];

    this.title.setTitle('Nova Editora');

    if (codigoEditora) {
      this.carregarRegistro(codigoEditora);
    }
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

  private carregarRegistro(codigo: number) {
    this.editoraService.findById(codigo)
      .subscribe({
        next: (editoraRetornada: Editora) => {
          this.popularForm(editoraRetornada);
          this.atualizarTituloEdicao();
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        }
      });
  }

  private popularForm(editora: Editora) {
    this.form.setValue({
      id: editora.id,
      nome: editora.nome,
      urlSiteOficial: editora.urlSiteOficial,
      urlFacebook: editora.urlFacebook,
      ultTwitter: editora.ultTwitter,
      urlWikipedia: editora.urlWikipedia
    });
  }

  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição editora: ${this.form.value.nome}`);
  }




}