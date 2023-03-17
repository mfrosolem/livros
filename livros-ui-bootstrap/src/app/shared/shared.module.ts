import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ModalModule } from 'ngx-bootstrap/modal';

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
    ModalModule.forRoot()
  ],
  exports: [
    ConfirmModalComponent,
    ToasterComponent
  ]
})
export class SharedModule { }
