import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { decode } from '@googlemaps/polyline-codec';
import { Store } from '@ngrx/store';
import { SetRideAction } from 'src/app/store/ride.actions';
import { AppState } from 'src/app/store/ride.reducer';
import { Point } from 'src/models/map';
import { FullRide, Place, Ride } from 'src/models/ride';
import { RideService } from 'src/services/ride/ride.service';

@Component({
  selector: 'app-saved-routes',
  templateUrl: './saved-routes.component.html',
  styleUrls: ['./saved-routes.component.sass']
})
export class SavedRoutesComponent implements OnInit {

  displayedColumns: string[] = ['route', 'fare']
  dataSource: any;

  constructor(
    private _liveAnnouncer: LiveAnnouncer,
    private rideService: RideService,
    private router: Router,
    private store: Store<{state: AppState}>
  ) {}

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  ngOnInit() {
    this.rideService.getSavedRoutes().subscribe({
      next: (res: Array<FullRide>) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.sort = this.sort;
      }
    })
  }

  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  getFormatedRouteString(ride: FullRide) : string {
    return ride.places.map(p => p.name).join(" → ");
  }

  clickedRow(row: FullRide) : void {
    let ride = this.convertFullRideToRide(row);
    this.store.dispatch(SetRideAction({ride: ride})) 
    this.router.navigate(['/home'])
  }

  convertFullRideToRide(fullRide: FullRide): Ride {
    let ride = new Ride();
    ride.places = fullRide.places.map((p, ind) => {
      let place = new Place();
      place.name = p.name;
      place.option = p.option;
      place.id = ind;
      let a = p as any;
      place.editing = false;
      place.point = new Point(a.latitude, a.longitude);;
      place.routes = p.routes;
      return place;
    });
    ride.places.forEach(p => p.routes.forEach(r => r.coordinates = decode(r.coordinatesEncoded).map(c => new Point(c[0], c[1]))));
    ride.places.forEach(p => p.routes.forEach(r => r.coordinates = decode(r.coordinatesEncoded).map(c => new Point(c[0], c[1]))));
    ride.fare = fullRide.fare;
    return ride;
  }
  

}
