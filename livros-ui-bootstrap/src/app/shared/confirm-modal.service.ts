import { Injectable } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

import { ConfirmModalComponent } from './confirm-modal/confirm-modal.component';


@Injectable({
  providedIn: 'root'
})
export class ConfirmModalService {

  constructor(
    private modalService: BsModalService
  ) { }

  showConfirm(title: string, message: string, okTxt?: string, cancelTxt?: string) {

    const modalRef: BsModalRef = this.modalService.show(ConfirmModalComponent);

    modalRef.content.title = title;
    modalRef.content.message = message;

    if (okTxt) {
      modalRef.content.okTxt = okTxt;
    }

    if (cancelTxt) {
      modalRef.content.cancelTxt = cancelTxt;
    }


    return (<ConfirmModalComponent> modalRef.content).confirmResult;

  }
}
