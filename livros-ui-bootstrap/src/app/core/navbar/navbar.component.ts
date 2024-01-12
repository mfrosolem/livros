import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { AuthService } from 'src/app/seguranca/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(private auth: AuthService) { }

  temPermissao(permissao: string) {
    return this.auth.hasPermission(permissao);
  }

  logout() {
    this.auth.logout();
  }

  get nomeUsuarioLogado() {
    return this.auth.jwtPayload?.nome;
  }


}