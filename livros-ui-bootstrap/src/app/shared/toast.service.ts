import { Injectable, TemplateRef } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import { ToastEvent } from './models-toast/toast-event';
import { EventTypes } from './models-toast/event-types';

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  toastEvents: Observable<ToastEvent>;
  private _toastEvents = new Subject<ToastEvent>();

  constructor() {
    this.toastEvents = this._toastEvents.asObservable();
  }

  showSuccessToast(message: string, title?: string) {
    this._toastEvents.next({
      message,
      title,
      type: EventTypes.Success
    });
  }

  showInfoToast(message: string, title?: string) {
    this._toastEvents.next({
      message,
      title,
      type: EventTypes.Info
    });
  }

  showWarningToast(message: string, title?: string) {
    this._toastEvents.next({
      message,
      title,
      type: EventTypes.Warning
    });
  }

  showErrorToast(message: string, title?: string) {
    this._toastEvents.next({
      message,
      title,
      type: EventTypes.Error
    });
  }


}
