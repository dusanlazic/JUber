import { Component, Input, OnInit } from '@angular/core';
import { FullRide } from 'src/models/ride';

@Component({
  selector: 'app-ride-details-sidebar',
  templateUrl: './ride-details-sidebar.component.html',
  styleUrls: ['./ride-details-sidebar.component.sass']
})
export class RideDetailsSidebarComponent implements OnInit {

  @Input() ride: FullRide | undefined;

  constructor() { }

  ngOnInit(): void {

  }

}
