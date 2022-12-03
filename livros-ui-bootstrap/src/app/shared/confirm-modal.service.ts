import { tap, map } from 'rxjs';
import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ConfirmModalComponent } from './confirm-modal/confirm-modal.component';

@Injectable({
  providedIn: 'root'
})
export class ConfirmModalService {

  constructor(
    private modalService: NgbModal
  ) { }

  showConfirm(title: string, message: string, okTxt?: string, cancelTxt?: string) {

    const modalRef = this.modalService.open(ConfirmModalComponent);

    modalRef.componentInstance.title = title;
    modalRef.componentInstance.message = message;

    if (okTxt) {
      modalRef.componentInstance.okTxt = okTxt;
    }

    if (cancelTxt) {
      modalRef.componentInstance.cancelTxt = cancelTxt;
    }

    const resultado = modalRef.result;

    return resultado;

  }
}
