import { Component, Input, OnInit } from '@angular/core';
import { Ride } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';

@Component({
  selector: 'passenger-sidebar',
  templateUrl: './passenger-sidebar.component.html',
  styleUrls: ['./passenger-sidebar.component.sass']
})
export class PassengerSidebarComponent implements OnInit {

  @Input() ride: Ride;

  isEditing: boolean = false;
  
  constructor(private mapService: MapService) { 
    this.ride = new Ride();
  }

  ngOnInit(): void {
    this.mapService.editing$().subscribe(placeInd => {
      if(placeInd === -1) return;
      if(this.isEditing) {
        this.isEditing = false;
      }
      else {
        this.isEditing = true;
      }

    })
  }

}
