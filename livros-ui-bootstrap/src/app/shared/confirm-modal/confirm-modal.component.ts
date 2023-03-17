import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrls: ['./confirm-modal.component.css']
})
export class ConfirmModalComponent implements OnInit {

  @Input() title!: string;
  @Input() message!: string;
  @Input() cancelTxt: string = 'Cancelar';
  @Input() okTxt: string = 'Sim';

  confirmResult!: Subject<boolean>;

  constructor(
    public modal: BsModalRef

  ) { }

  ngOnInit(): void {
    this.confirmResult = new Subject();
  }

  onCancel() {
    this.confirmAndClose(false);
  }

  onConfirm() {
    this.confirmAndClose(true);
  }

   confirmAndClose(value: boolean) {
    this.confirmResult.next(value);
    this.modal.hide();
  }

}
