import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AddPalEvent, Pal } from 'src/models/user';
import { ValidationConstants } from 'src/services/util/custom-validators';

@Component({
  selector: 'app-add-pals-dialog',
  templateUrl: './add-pals-dialog.component.html',
  styleUrls: ['./add-pals-dialog.component.sass']
})
export class AddPalsDialogComponent implements OnInit {

  @Output('addPalEvent')
  addPal: EventEmitter<AddPalEvent> = new EventEmitter<AddPalEvent>();

  palForm!: FormGroup

  constructor(
    private builder: FormBuilder
  ){ 
    this.createForm();
  }

  private createForm() : void {
    this.palForm = this.builder.group({
      email: new FormControl('', [Validators.required, Validators.email]),
      firstName: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
      lastName: new FormControl('', [Validators.required, Validators.pattern(ValidationConstants.Name), Validators.maxLength(40)]),
    })
  }

  ngOnInit(): void {
  }

  add() {
    this.addPal.emit({confirmed: true, newPal: this.palForm.value})
  }

  cancel() {
    this.addPal.emit({confirmed: false, newPal: undefined})
  }


  get firstName() { return this.palForm.get('firstName'); }
  get lastName() { return this.palForm.get('lastName'); }
  get email() { return this.palForm.get('email'); }

}
