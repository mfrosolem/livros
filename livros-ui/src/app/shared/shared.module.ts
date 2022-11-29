import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';

import { ConfirmModalComponent } from './confirm-modal/confirm-modal.component';
import { ToastComponent } from './toast/toast.component';
import { ToasterComponent } from './toaster/toaster.component';



@NgModule({
  declarations: [
    ConfirmModalComponent,
    ToastComponent,
    ToasterComponent
  ],
  imports: [
    CommonModule,
    NgbModalModule
  ],
  exports: [
    ConfirmModalComponent,
    ToasterComponent
  ]
})
export class SharedModule { }
