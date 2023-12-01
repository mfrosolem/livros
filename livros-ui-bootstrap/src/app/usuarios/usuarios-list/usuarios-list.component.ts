import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Usuario } from '../../core/models/usuario/usuario';

@Component({
  selector: 'app-usuarios-list',
  templateUrl: './usuarios-list.component.html',
  styleUrls: ['./usuarios-list.component.css'],
  preserveWhitespaces: true
})
export class UsuariosListComponent {

  @Input() usuarios: Usuario[] = [];
  @Output() edit = new EventEmitter(false);
  @Output() remove = new EventEmitter(false);
  @Output() naoTemPermissao = new EventEmitter(false);

  onEdit(usuario: Usuario) {
    this.edit.emit(usuario);
  }

  onRemove(usuario: Usuario) {
    this.remove.emit(usuario);
  }

  onNaoTemPermissao(nomePermissao: string) {
    this.naoTemPermissao.emit(nomePermissao);
  }

}
