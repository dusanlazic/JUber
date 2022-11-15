import { Component, Input, OnInit } from '@angular/core';
import { Place, Ride } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';

@Component({
  selector: 'app-places',
  templateUrl: './places.component.html',
  styleUrls: ['./places.component.sass']
})
export class PlacesComponent implements OnInit {
  isModalActive: boolean = false;
  @Input() ride: Ride;

  constructor(private mapService: MapService) { 
    this.ride = new Ride();
  }

  ngOnInit(): void {
  }

  toggleModal(): void {
    this.isModalActive = !this.isModalActive;
  }

  updateEmptyPlace(evt: any) {
    if(evt.confirmed) {
      let newPlace = new Place(evt.name, "");
      newPlace.editing = true;
      this.ride.places.push(newPlace);
      this.mapService.setConfirmedLocation(newPlace.name);
    }
    this.toggleModal();
    this.mapService.setRedraw(true);
  }

}
