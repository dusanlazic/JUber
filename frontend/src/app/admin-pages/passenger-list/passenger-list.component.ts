import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { PersonDTO } from 'src/models/user';
import { DriverService } from 'src/services/driver/driver.service';
import { PassengerService } from 'src/services/passenger/passenger.service';

@Component({
  selector: 'app-passenger-list',
  templateUrl: './passenger-list.component.html',
  styleUrls: ['./passenger-list.component.sass']
})
export class PassengerListComponent implements OnInit {

  displayedColumns: string[] = ['fullName', 'city', 'email'];
  dataSource: any;
  
  constructor(
    private _liveAnnouncer: LiveAnnouncer,
    private passengerService: PassengerService,
    private router: Router,
  ) {}

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  ngOnInit() {
    this.passengerService.findAll().subscribe({
      next: (res: Array<PersonDTO>) => {
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


  clickedRow(row: PersonDTO) : void{
    this.router.navigate([`/admin/passengers/${row.id}`])
  }
}
