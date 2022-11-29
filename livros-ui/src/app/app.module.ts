import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AutoresModule } from './autores/autores.module';
import { CoreModule } from './core/core.module';
import { EditorasModule } from './editoras/editoras.module';
import { GenerosModule } from './generos/generos.module';
import { SharedModule } from './shared/shared.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,

    CoreModule,
    SharedModule,
    GenerosModule,
    EditorasModule,
    AutoresModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
