import { Component, OnInit, ViewChild } from '@angular/core';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import { AccountManagementService } from 'src/services/accountManagement/account-management.service';
import { Toastr } from 'src/services/util/toastr.service';

export interface ChangeRequestsResponse {
	requestId: string,
  userId: string,
  userFullName: string,
  changes: Map<String, String>,
  requestedAt: Date
}

@Component({
  selector: 'app-change-requests',
  templateUrl: './change-requests.component.html',
  styleUrls: ['./change-requests.component.sass']
})
export class ChangeRequestsComponent implements  OnInit {

  displayedColumns: string[] = ['userFullName', 'firstName', 'lastName', 'city', 'phoneNumber', 'requestedAt', 'actions'];
  dataSource: any;
  
  constructor(
    private _liveAnnouncer: LiveAnnouncer,
    private accountManagementService: AccountManagementService,
    private toastr: Toastr,
  ) {}

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  ngOnInit() {
    this.accountManagementService.getChangeRequests().subscribe({
      next: (res: Array<ChangeRequestsResponse>) => {
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

  approveChange(requestId: string) : void {
    this.removeFromTable(requestId)

    this.accountManagementService.resolveChangeRequest(requestId, 'APPROVED').subscribe({
      next: (res: any) => {
        this.toastr.success(`Request approved!`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  denyChange(requestId: string) : void {
    this.removeFromTable(requestId)

    this.accountManagementService.resolveChangeRequest(requestId, 'DENIED').subscribe({
      next: (res: any) => {
        this.toastr.success(`Request denied!`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  removeFromTable(requestId: string) : void{
    this.dataSource.data = this.dataSource.data.filter((elem: { requestId: string; }) => elem.requestId !== requestId);
    this.dataSource.data = [...this.dataSource.data];
  }
}
