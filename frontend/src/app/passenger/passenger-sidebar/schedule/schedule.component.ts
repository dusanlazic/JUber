import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Store } from '@ngrx/store';
import { IRideRequest } from 'src/app/store/rideRequest/rideRequest';
import { UpdateScheduleTime } from 'src/app/store/rideRequest/rideRequest.actions';
import { CustomValidators } from 'src/services/util/custom-validators';

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.sass']
})
export class ScheduleComponent implements OnInit {

  scheduleForm!: FormGroup

  constructor (
    private builder: FormBuilder,
    private store: Store<{rideRequest: IRideRequest}>
  ) {
    this.createForm();
  }

  ngOnInit(): void {
  }


  private createForm() : void{
    this.scheduleForm = this.builder.group({
      hours: new FormControl(),
      minutes: new FormControl(),
    }, {
      validators: [CustomValidators.Schedule5HoursValidator('hours', 'minutes')]
    });
  }

  clearFields(): void {
    this.scheduleForm.reset();
  }


  changeHours() : void {
    if(!this.minutes?.value){
      this.scheduleForm.get('minutes')?.setValue('00');
    }

    if(this.hours?.value > 23){
      this.scheduleForm.get('hours')?.setValue(23);
    }
    if(this.hours?.value < 0){
      this.scheduleForm.get('hours')?.setValue(0);
    }
    if(this.scheduleForm.valid){
      this.store.dispatch(UpdateScheduleTime({time: `${this.hours?.value}:${this.minutes?.value}`}))
    }
  }

  changeMinutes() : void {
    if(!this.hours?.value){
      this.scheduleForm.get('hours')?.setValue('00');
    }

    if(this.minutes?.value > 59){
      this.scheduleForm.get('minutes')?.setValue(59);
    }
    if(this.minutes?.value < 0){
      this.scheduleForm.get('minutes')?.setValue(0);
    }

    if(this.scheduleForm.valid){
      this.store.dispatch(UpdateScheduleTime({time: `${this.hours?.value}:${this.minutes?.value}`}))
    }
  }

  get hours() { return this.scheduleForm.get('hours'); }
  get minutes() { return this.scheduleForm.get('minutes'); }

}
