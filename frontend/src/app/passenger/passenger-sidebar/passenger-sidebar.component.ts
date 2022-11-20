import { Component, Input, OnInit } from '@angular/core';
import { Ride } from 'src/models/ride';

@Component({
  selector: 'passenger-sidebar',
  templateUrl: './passenger-sidebar.component.html',
  styleUrls: ['./passenger-sidebar.component.sass']
})
export class PassengerSidebarComponent implements OnInit {

  @Input() ride: Ride;
  
  constructor() { 
    this.ride = new Ride();
  }

  ngOnInit(): void {
  }

}
