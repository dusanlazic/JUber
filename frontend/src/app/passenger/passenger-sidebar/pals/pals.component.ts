import { Component, Input, OnInit } from '@angular/core';
import { Ride } from 'src/models/ride';

@Component({
  selector: 'app-pals',
  templateUrl: './pals.component.html',
  styleUrls: ['./pals.component.sass']
})
export class PalsComponent implements OnInit {

  @Input() ride: Ride;

  constructor() { 
    this.ride = new Ride();
  }

  ngOnInit(): void {
  }

}
