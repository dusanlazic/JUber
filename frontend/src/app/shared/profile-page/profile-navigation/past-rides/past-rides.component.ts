import { Component, OnInit, ViewChild } from '@angular/core';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import { RideService } from 'src/services/ride/ride.service';
import { Router } from '@angular/router';

export interface PastRidesResponse {
	id: string;
	startPlaceName: string;
	endPlaceName: string;
	date: string;
	startTime: string;
	endTime: string;
	fare: number;
}

@Component({
  selector: 'app-past-rides',
  templateUrl: './past-rides.component.html',
  styleUrls: ['./past-rides.component.sass']
})
export class PastRidesComponent implements  OnInit {

  displayedColumns: string[] = ['startPlaceName', 'date', 'startTime', 'endTime', 'fare'];
  dataSource: any;
  
  constructor(
    private _liveAnnouncer: LiveAnnouncer,
    private rideService: RideService,
    private router: Router
  ) {}

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  ngOnInit() {
    this.rideService.getPastRides().subscribe({
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

  clickedRow(row: PastRidesResponse) : void{
    console.log(row)
    //this.router.navigate([`/ridePreview/${row.id}`])
  }
}
