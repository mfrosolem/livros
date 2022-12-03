import { Component, Input, OnInit } from '@angular/core';


import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrls: ['./confirm-modal.component.css']
})
export class ConfirmModalComponent implements OnInit {

  @Input() title!: string;
  @Input() message!: string;
  @Input() cancelTxt = 'Cancelar';
  @Input() okTxt = 'Sim';

  confirmResult = new Subject<boolean>();

  constructor(
    public modal: NgbActiveModal

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
    if (value) {
      this.modal.close(value);
    } else {
      this.modal.dismiss(value);
    }
  }

}
