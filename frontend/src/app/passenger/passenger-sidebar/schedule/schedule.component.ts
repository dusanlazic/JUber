import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { RideRequest } from 'src/models/ride';


@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.sass']
})
export class ScheduleComponent implements OnInit {

  // @Input() rideRequest: RideRequest;

  scheduleForm!: FormGroup

  constructor(private builder: FormBuilder) {
    // this.rideRequest = new RideRequest();
    this.createForm();
   }

  ngOnInit(): void {
  }


  private createForm() : void{
    this.scheduleForm = this.builder.group({
      hours: new FormControl(),
      minutes: new FormControl(),
    });
  }

  clearFields(): void {
    this.scheduleForm.reset();
  }

  get hours() { return this.scheduleForm.get('hours'); }
  get minutes() { return this.scheduleForm.get('minutes'); }
}
