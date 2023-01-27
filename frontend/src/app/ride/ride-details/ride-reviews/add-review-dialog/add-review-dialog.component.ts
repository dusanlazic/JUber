import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { RideReviewInputEvent } from 'src/models/rideReview';
import { defineComponents, IgcRatingComponent } from 'igniteui-webcomponents';

defineComponents(IgcRatingComponent);

@Component({
  selector: 'app-add-review-dialog',
  templateUrl: './add-review-dialog.component.html',
  styleUrls: ['./add-review-dialog.component.sass']
})
export class AddReviewDialogComponent implements OnInit {

  @Output('addReviewEvent')
  addReview: EventEmitter<RideReviewInputEvent> = new EventEmitter<RideReviewInputEvent>();

  reviewForm!: FormGroup

  constructor(
    private builder: FormBuilder,
  ){ 
    this.createForm();
  }

  private createForm() : void {
    this.reviewForm = this.builder.group({
      comment: new FormControl('', [Validators.required]),
      driverRating: new FormControl(5, [Validators.required, Validators.min(1), Validators.max(5)]),
      vehicleRating: new FormControl(5, [Validators.required, Validators.min(1), Validators.max(5)]),
    })
  }

  ngOnInit(): void {
  }

  add() {
    this.addReview.emit({confirmed: true, rideReviewInput: this.reviewForm.value})
  }

  cancel() {
    this.addReview.emit({confirmed: false, rideReviewInput: undefined})
  }

  changeDriverRating(event: any) : void {
    this.reviewForm.get('driverRating')?.setValue(event.detail);
    if(this.driverRating?.value > 5){
      this.reviewForm.get('driverRating')?.setValue(5);
    }
    if(this.driverRating?.value < 1){
      this.reviewForm.get('driverRating')?.setValue(1);
    }
  }

  changeVehicleRating(event: any) : void {
    this.reviewForm.get('vehicleRating')?.setValue(event.detail);

    if(this.vehicleRating?.value > 5){
      this.reviewForm.get('vehicleRating')?.setValue(5);
    }
    if(this.vehicleRating?.value < 1){
      this.reviewForm.get('vehicleRating')?.setValue(1);
    }

  }

  get comment() { return this.reviewForm.get('comment'); }
  get driverRating() { return this.reviewForm.get('driverRating'); }
  get vehicleRating() { return this.reviewForm.get('vehicleRating'); }
  
}
