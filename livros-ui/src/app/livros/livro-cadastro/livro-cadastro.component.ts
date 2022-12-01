import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { AutorService } from './../../autores/autor.service';
import { ErrorHandlerService } from './../../core/error-handler.service';
import { Autor, Editora, Genero, Livro } from './../../core/models/model';
import { EditoraService } from './../../editoras/editora.service';
import { GeneroService } from './../../generos/genero.service';
import { ToastService } from './../../shared/toast.service';
import { LivroService } from './../livro.service';

@Component({
  selector: 'app-livro-cadastro',
  templateUrl: './livro-cadastro.component.html',
  styleUrls: ['./livro-cadastro.component.css'],
  preserveWhitespaces: true
})
export class LivroCadastroComponent implements OnInit {

  autores: Autor[] = [];
  generos: Genero[] = [];
  editoras: Editora[] = [];

  form: FormGroup = this.formBuilder.group({
    id: [],
    isbn: [null, [Validators.required, Validators.minLength(13), Validators.maxLength(13)]],
    titulo: [null, [Validators.required, Validators.maxLength(50)]],
    subtitulo: [null, [Validators.maxLength(50)]],
    idioma: ['Português', [Validators.required, Validators.maxLength(15)]],
    serieColecao: [null, [Validators.maxLength(20)]],
    volume: [null],
    tradutor: [null, [Validators.maxLength(60)]],
    ano: [null],
    edicao: [null],
    paginas: [null, [Validators.required]],
    sinopse: [null, [Validators.maxLength(500)]],
    genero: this.formBuilder.group({
      id: [null, Validators.required],
      descricao: []
    }),
    editora: this.formBuilder.group({
      id: [null, Validators.required],
      nome: []
    }),
    autor: this.formBuilder.group({
      id: [null, Validators.required],
      nome: []
    })
  });

  constructor(
    private formBuilder: FormBuilder,
    private livroService: LivroService,
    private toastService: ToastService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private title: Title,
    private autorService: AutorService,
    private generoService: GeneroService,
    private editoraService: EditoraService
  ) { }

  ngOnInit(): void {
    const codigoLivro = this.route.snapshot.params['codigo'];

    this.carregarAutores();
    this.carregarGeneros();
    this.carregarEditoras();

    this.title.setTitle('Novo Livro');

    if (codigoLivro) {
      this.carregarRegistro(codigoLivro);
    }
  }

  get editando() {
    return Boolean(this.form.value.id);
  }

  onSubmit() {
    const descricaoOpercao = this.editando ? 'atualizado' : 'cadastrado';
    this.livroService.save(this.form.value).subscribe({
      next: (result) => {
        this.toastService.showSuccessToast(`Livro ${descricaoOpercao} com sucesso!`);
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
    this.livroService.findById(codigo)
      .subscribe({
        next: (livroRetornado: Livro) => {
          this.form.patchValue(livroRetornado);
          this.atualizarTituloEdicao();
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        }
      });
  }


  private atualizarTituloEdicao() {
    this.title.setTitle(`Edição livro: ${this.form.value.titulo}`);
  }

  private carregarAutores() {
    this.autorService.listAll()
      .subscribe({
        next: (result: Autor[]) => { this.autores = result; },
        error: (falha) => { this.errorHandlerService.handle(falha); }
      });
  }

  private carregarGeneros() {
    this.generoService.listAll()
      .subscribe({
        next: (result: Genero[]) => { this.generos = result; },
        error: (falha) => { this.errorHandlerService.handle(falha); }
      });
  }

  private carregarEditoras() {
    this.editoraService.listAll()
      .subscribe({
        next: (result: Editora[]) => { this.editoras = result; },
        error: (falha) => { this.errorHandlerService.handle(falha); }
      });
  }

}
