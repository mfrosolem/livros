import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

import { AutorService } from './../../autores/autor.service';
import { ErrorHandlerService } from './../../core/error-handler.service';
import { EditoraService } from './../../editoras/editora.service';
import { GeneroService } from './../../generos/genero.service';
import { ToastService } from './../../shared/toast.service';
import { LivroService } from './../livro.service';
import { Genero } from '../../core/models/genero/genero';
import { Autor } from '../../core/models/autor/autor';
import { Editora } from '../../core/models/editora/editora';
import { Observable } from 'rxjs';
import { Livro } from '../../core/models/livro/livro';

@Component({
  selector: 'app-livro-cadastro',
  templateUrl: './livro-cadastro.component.html',
  styleUrls: ['./livro-cadastro.component.css'],
  preserveWhitespaces: true
})
export class LivroCadastroComponent implements OnInit {

  livro$: Observable<Livro|any> | null = null;

  autores: Autor[] = [];
  generos: Genero[] = [];
  editoras: Editora[] = [];

  URL_SEM_IMAGEM = '/assets/images/sem_imagem.jpg';
  imagemUrl = this.URL_SEM_IMAGEM;

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
  ) { 
    const codigoLivro = this.route.snapshot.params['codigo'];
    if (codigoLivro) {
      this.carregarRegistro(codigoLivro);
      this.carregaFotoLivro(codigoLivro);
    }
  }

  ngOnInit(): void {
    this.carregarAutores();
    this.carregarGeneros();
    this.carregarEditoras();

    this.title.setTitle('Novo Livro');

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

  onChangeFoto() {
    this.router.navigate(['foto'], {  relativeTo: this.route });
  }

  private carregarRegistro(codigo: number) {
    this.livroService.findById(codigo)
      .subscribe({
        next: (livroRetornado: any) => {
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

  private carregaFotoLivro(codigo: number) {
    this.livroService.getFoto(codigo)
      .subscribe({
        next: (fotoRecebida: any) => {
          this.readFile(fotoRecebida);
        },
        error: (falha) => {

        }
      });

  }

  private readFile(blob: Blob) {

    const reader = new FileReader();
    reader.onload = () => {
      this.imagemUrl = reader.result as string;
    }
    reader.readAsDataURL(blob);
  }

}
