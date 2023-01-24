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
	formattedDate: string;
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

  constructor(
    private rideService: RideService,
    private router: Router
  ) {}

  rides!: Array<PastRidesResponse>

  ngOnInit() {
    this.rideService.getPastRides().subscribe({
      next: (res: Array<PastRidesResponse>) => {
        this.rides = res;
      }
    })
  }

  clickedRow(row: PastRidesResponse) : void{
    console.log(row)
    //this.router.navigate([`/ridePreview/${row.id}`])
  }
}
