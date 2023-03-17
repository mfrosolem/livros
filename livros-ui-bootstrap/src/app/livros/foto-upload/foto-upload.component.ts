import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { EMPTY, switchMap, take } from 'rxjs';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { Livro } from 'src/app/core/models/model';
import { ToastService } from 'src/app/shared/toast.service';

import { LivroService } from '../livro.service';
import { ConfirmModalService } from './../../shared/confirm-modal.service';

@Component({
  selector: 'app-foto-upload',
  templateUrl: './foto-upload.component.html',
  styleUrls: ['./foto-upload.component.css']
})
export class FotoUploadComponent implements OnInit {

  URL_SEM_IMAGEM = '/assets/images/sem_imagem.jpg';
  imagemUrl = this.URL_SEM_IMAGEM;
  livro: Livro = new Livro();
  fileFoto!: File;
  habilitaExcluir: boolean = false;

  form: FormGroup = this.formBuilder.group({
    arquivo: [null, [Validators.required, this.requiredFileType('jpg', 'jpeg', 'png')]]
  });

  constructor(
    private formBuilder: FormBuilder,
    private livroService: LivroService,
    private location: Location,
    private router: Router,
    private route: ActivatedRoute,
    private errorHandlerService: ErrorHandlerService,
    private title: Title,
    private toastService: ToastService,
    private confirmModalService: ConfirmModalService
  ) { }

  ngOnInit(): void {
    const codigoLivro = this.route.snapshot.params['codigo'];
    this.carregarRegistroLivro(codigoLivro);
    this.carregaFotoLivro(codigoLivro);
  }

  inputFotoChange(event: any) {

    if (event.target.files && event.target.files[0]) {
      const foto = event.target.files[0];

      const extensõesPermitidas = ['jpg', 'jpeg', 'png'];

      if (this.fileTypeAllowed(foto.name, ...extensõesPermitidas)) {

        this.fileFoto = foto;
        console.log(this.fileFoto);

        this.readFile(this.fileFoto);
      } else {
        this.toastService.showWarningToast(`Arquivo escolhido não é uma imagem do tipo ${extensõesPermitidas}`);
      }
    }
  }

  onSubmit() {
    this.livroService.uploadFoto(this.livro, this.fileFoto)
      .subscribe({
        next: (response) => {
          this.form.reset();
          this.toastService.showSuccessToast(`Foto para o livro ${this.livro.titulo} salva com sucesso!`);
          this.habilitaExcluir = true;
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        }
      })
  }

  onCancel() {
    this.location.back();
  }

  onRemoveFoto() {
    this.confirmDelete(Number(this.livro.id));
  }

  private confirmDelete(codigo: number) {

    const resultado$ =
      this.confirmModalService.showConfirm('Confirmação', 'Deseja excluir a foto?');

    resultado$.asObservable()
      .pipe(
        take(1),
        switchMap(resultConfirm => resultConfirm ? this.livroService.removeFoto(codigo) : EMPTY)
      )
      .subscribe(
        {
          next: () => {
            this.toastService.showSuccessToast('Foto excluída com sucesso');
            this.imagemUrl = this.URL_SEM_IMAGEM;
            this.form.patchValue({ arquivo: null });
            this.fileFoto = new File([], '');
            this.habilitaExcluir = false;
            this.form.reset();
          },
          error: (erro) => {
            this.errorHandlerService.handle(erro);
          }
        }
      );
  }

  private carregarRegistroLivro(codigo: number) {
    this.livroService.findById(codigo)
      .subscribe({
        next: (livroRetornado: Livro) => {
          this.livro = livroRetornado;
        },
        error: (falha) => {
          this.errorHandlerService.handle(falha);
        }
      })
  }

  private carregaFotoLivro(codigo: number) {
    this.livroService.getFoto(codigo)
      .subscribe({
        next: (fotoRecebida: any) => {
          this.fileFoto = fotoRecebida;
          this.readFile(fotoRecebida);
          this.habilitaExcluir = true;
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

  private requiredFileType(...types: string[]) {
    return function (control: FormControl) {
      const file = control.value;
      if (file) {

        const extension = file.split('.')[1].toLowerCase();

        const typesLowerCase = types.map(t => t.toLowerCase());

        if (!typesLowerCase.includes(extension)) {

          return {
            requiredFileType: true
          };
        }

        return null;
      }

      return null;
    };
  }

  private fileTypeAllowed(fileName: string, ...types: string[]): boolean {
    if (fileName) {
      const extension = fileName.split('.')[1].toLowerCase();

      const typesLowerCase = types.map(t => t.toLowerCase());

      if (typesLowerCase.includes(extension)) {
        return true;
      }
    }

    return false;
  }


}
