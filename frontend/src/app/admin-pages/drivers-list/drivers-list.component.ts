import { Component, OnInit, ViewChild } from '@angular/core';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import { DriverService } from 'src/services/driver/driver.service';
import { Router } from '@angular/router';

export interface DriverStatusResponse {
	driverId: string,
  fullName: string,
  status: string,
  startPlaceName: string,
  endPlaceName: string,
}

@Component({
  selector: 'app-drivers-list',
  templateUrl: './drivers-list.component.html',
  styleUrls: ['./drivers-list.component.sass']
})
export class DriversListComponent implements  OnInit {

  displayedColumns: string[] = ['fullName', 'status', 'ride'];
  dataSource: any;
  
  constructor(
    private _liveAnnouncer: LiveAnnouncer,
    private driverService: DriverService,
    private router: Router
  ) {}

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  ngOnInit() {
    this.driverService.getStatuses().subscribe({
      next: (res: Array<DriverStatusResponse>) => {
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

  formatRide(element: DriverStatusResponse) {
    if (element.startPlaceName != '') {
      return element.startPlaceName + " → " + element.endPlaceName;
    } else {
      return "";
    }
  }

  clickedRow(row: DriverStatusResponse) : void{
    this.router.navigate([`/admin/drivers/${row.driverId}`])
  }
}
