import { Component, OnInit, ViewChild } from '@angular/core';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import { AccountManagementService } from 'src/services/accountManagement/account-management.service';
import { Router } from '@angular/router';
import { Toastr } from 'src/services/util/toastr.service';

export interface BlockedUserResponse {
	userId: string;
	fullName: string;
  role: string;
	note: string;
}

@Component({
  selector: 'app-blocked-users',
  templateUrl: './blocked-users.component.html',
  styleUrls: ['./blocked-users.component.sass']
})
export class BlockedUsersComponent implements  OnInit {

  displayedColumns: string[] = ['fullName', 'role', 'note', 'actions'];
  dataSource: any;
  
  constructor(
    private _liveAnnouncer: LiveAnnouncer,
    private accountManagementService: AccountManagementService,
    private toastr: Toastr,
    private router: Router
  ) {}

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  ngOnInit() {
    this.accountManagementService.getBlockedUsers().subscribe({
      next: (res: Array<BlockedUserResponse>) => {
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

  roleToHumanReadable(roleEnum: string) : string {
    if (roleEnum === "ROLE_PASSENGER") {
      return "Passenger";
    } else if (roleEnum === "ROLE_DRIVER") {
      return "Driver";
    } else {
      return "?";
    }
  }

  blockUser(email: string) : void {
    this.accountManagementService.blockUser(email).subscribe({
      next: (res: any) => {
        this.toastr.success(`User with the address "${email}" has been blocked.`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  unblockUser(userId: string, fullName: string) : void {
    this.accountManagementService.unblockUser(userId).subscribe({
      next: (res: any) => {
        this.toastr.success(`${fullName} has been unblocked.`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  clickedRow(row: BlockedUserResponse) : void{
    console.log(row)
    //this.router.navigate([`/ridePreview/${row.id}`])
  }
}
