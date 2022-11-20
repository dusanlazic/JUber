import { state } from '@angular/animations';
import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { MoveToPreviewAction } from 'src/app/store/ride.actions';
import { AppState } from 'src/app/store/ride.reducer';
import { Place } from 'src/models/ride';
import { MapService } from 'src/services/map/map.service';

@Component({
  selector: 'app-place',
  templateUrl: './place.component.html',
  styleUrls: ['./place.component.sass']
})
export class PlaceComponent implements OnInit {

  place: Place | undefined;
  @Input() index: number;

  constructor(private mapService: MapService, private store: Store<{state: AppState}>) {
    this.index = -1;
   }

  ngOnInit(): void {
    this.store.select('state').subscribe(res => {
      this.place = res.ride.places.at(this.index)
    })
  }

  editPlace() {
    if(!this.place) return;
    this.store.dispatch(MoveToPreviewAction({place: this.place}))
    this.mapService.setEditing(this.index);
  }

}
