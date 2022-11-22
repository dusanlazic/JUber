import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-add-pals-dialog',
  templateUrl: './add-pals-dialog.component.html',
  styleUrls: ['./add-pals-dialog.component.sass']
})
export class AddPalsDialogComponent implements OnInit {

  @Output('addPalEvent')
  change: EventEmitter<any> = new EventEmitter<any>();


  constructor() { }

  ngOnInit(): void {
  }

  add() {
    this.change.emit({confirmed: true, newPal: {firstName: 'Pera', lastName: 'Peric', email: 'pera.peric@gmail.com'}})
  }

  cancel() {
    this.change.emit({confirmed: false})
  }

}
