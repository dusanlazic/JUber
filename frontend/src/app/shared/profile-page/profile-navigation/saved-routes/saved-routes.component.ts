import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { FullRide } from 'src/models/ride';
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
    private router: Router
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
    console.log(row)
    // Do the rest
  }

}
