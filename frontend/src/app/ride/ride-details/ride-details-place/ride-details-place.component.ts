import { Component, Input, OnInit } from '@angular/core';
import { Place } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';

@Component({
  selector: 'app-ride-details-place',
  templateUrl: './ride-details-place.component.html',
  styleUrls: ['./ride-details-place.component.sass']
})
export class RideDetailsPlaceComponent implements OnInit {

  @Input() place: Place | undefined;
  @Input() index: number = -1;

  color: string = 'gray';

  constructor(private mapService: MapService,) { }

  ngOnInit(): void {
    this.color = this.mapService.colors.at(this.index) || 'gray';
  }

}
