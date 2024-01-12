import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../usuario.service';
import { ToastService } from 'src/app/shared/toast.service';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/seguranca/auth.service';
import { Location } from '@angular/common';
import { UsuarioValidacao } from '../../shared/form-validacao';

@Component({
  selector: 'app-alterar-senha',
  templateUrl: './alterar-senha.component.html',
  styleUrls: ['./alterar-senha.component.css'],
  preserveWhitespaces: true
})
export class AlterarSenhaComponent implements OnInit {

  idUsuarioLogado: number;
  nomeUsuarioLogado: string = "";
  readonly patternSenha = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,8}$/

  form: FormGroup = this.formBuilder.group({
    id: [],
    senhaAtual: [null,
      [Validators.required]
    ],
    novaSenha: [null,
      [
        Validators.required,
        Validators.pattern(this.patternSenha)
      ]
    ],
    confirmaNovaSenha: [null,
      [
        Validators.required,
        Validators.pattern(this.patternSenha),
        UsuarioValidacao.equalsTo('novaSenha')
      ]
    ]
  });

  

  constructor(
    private auth: AuthService,
    private usuarioService: UsuarioService,
    private toastService: ToastService,
    private errorHandlerService: ErrorHandlerService,
    private formBuilder: FormBuilder,
    private title: Title,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {
    this.idUsuarioLogado = this.auth.jwtPayload.usuario_id as number;
    this.nomeUsuarioLogado = this.auth.jwtPayload.nome;
  }

  ngOnInit(): void {
    this.title.setTitle('Alterar Senha do Usuário');
  }

  onSubmit() {
    this.usuarioService.alterarSenhaUsuario(this.idUsuarioLogado, this.form.value).subscribe({
      next: (result) => {
        this.toastService.showSuccessToast("Senha alterada com sucesso!");
        this.location.back();
      },
      error: (e) => {
        this.errorHandlerService.handle(e);
      }
    });
  }

  onCancel(){
    this.location.back();
  }

  get senhaAtual() {
    return this.form.get('senhaAtual');
  }

  get novaSenha() {
    return this.form.get('novaSenha');
  }

  get confirmaNovaSenha() {
    return this.form.get('confirmaNovaSenha');
  }
}
