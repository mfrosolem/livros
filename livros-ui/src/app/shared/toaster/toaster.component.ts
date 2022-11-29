import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';

import { ToastEvent } from '../models-toast/toast-event';
import { ToastService } from '../toast.service';

@Component({
  selector: 'app-toaster',
  templateUrl: './toaster.component.html',
  styleUrls: ['./toaster.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ToasterComponent implements OnInit {

  currentToasts: ToastEvent[] = [];

  constructor(
    private toastService: ToastService,
    private crd: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.subscribeToToast();
  }

  subscribeToToast() {
    this.toastService.toastEvents.subscribe((toasts) => {
      const currentToast: ToastEvent = {
        type: toasts.type,
        title: toasts.title,
        message: toasts.message
      };
      this.currentToasts.push(currentToast);
      this.crd.detectChanges();
    });
  }

  dispose(index: number) {
    this.currentToasts.splice(index, 1);
    this.crd.detectChanges();
  }

}
