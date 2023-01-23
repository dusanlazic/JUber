import { Component, OnInit, ViewChild } from '@angular/core';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import { DriverService } from 'src/services/driver/driver.service';
import { RideReview } from 'src/models/rideReview';
import { PastRidesResponse } from 'src/app/shared/profile-page/profile-navigation/past-rides/past-rides.component';
import { IPerson } from 'src/models/ride';

export interface DriverInfo {
  profile: IPerson,
  status: string
}

@Component({
  selector: 'app-driver-info',
  templateUrl: './driver-info.component.html',
  styleUrls: ['./driver-info.component.sass']
})
export class DriverInfoComponent implements  OnInit {

  displayedColumns: string[] = ['startPlaceName', 'formattedDate', 'startTime', 'endTime', 'fare'];
  dataSource: any;
  
  constructor(
    private _liveAnnouncer: LiveAnnouncer,
    private driverService: DriverService,
  ) {}

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  ngOnInit() {
    this.driverService.getDriversPastRides('should be uuid from url').subscribe({
      next: (res: Array<PastRidesResponse>) => {
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

  clickedRideRow(row: PastRidesResponse) : void{
    console.log(row)
  }

  clickedReviewRow(row: RideReview) : void{
    console.log(row)
  }
}
