import { Component, OnInit, ViewChild } from '@angular/core';
import {MatSort, Sort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import { AccountManagementService } from 'src/services/accountManagement/account-management.service';
import { Router } from '@angular/router';
import { Toastr } from 'src/services/util/toastr.service';
import { FormControl, Validators } from '@angular/forms';
import { ApiResponse } from 'src/models/responses';
import { HttpErrorResponse } from '@angular/common/http';

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

  emailInput: FormControl
  blockedUsers: BlockedUserResponse[] = [];

  isEditNoteActive: boolean[] = [];
  
  constructor(
    private _liveAnnouncer: LiveAnnouncer,
    private accountManagementService: AccountManagementService,
    private toastr: Toastr,
    private router: Router
  ) {
    this.emailInput = new FormControl('', [Validators.required, Validators.email]);
  }

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  ngOnInit() {
    this.accountManagementService.getBlockedUsers().subscribe({
      next: (res: Array<BlockedUserResponse>) => {
        this.blockedUsers = res;
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.sort = this.sort;
        this.isEditNoteActive = Array(res.length).fill(false)
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
    // this.accountManagementService.blockUser(email).subscribe({
    //   next: (res: BlockedUserResponse) => {
    //     this.dataSource.data.splice(0, 0, res);
    //     this.dataSource.data = [...this.dataSource.data];
    //     this.toastr.success(`User with the address "${email}" has been blocked.`, 'Success');
    //   },
    //   error: (res: HttpErrorResponse) => {
    //     this.toastr.error(res.error.message, 'Error');
    //   },
    // })
  }

  unblockUser(userId: string, fullName: string) : void {
    this.accountManagementService.unblockUser(userId).subscribe({
      next: (res: any) => {
        this.dataSource.data = this.dataSource.data.filter((elem: { userId: string; }) => elem.userId !== userId);
        this.dataSource.data = [...this.dataSource.data];
        this.toastr.success(`${fullName} has been unblocked.`, 'Success');
      },
      error: (res: any) => {
        this.toastr.success(res, 'Error');
        console.log(res);
      },
    })
  }

  editNote(index: number) : void{
    this.isEditNoteActive[index] = true;
  }

  save(event: any, index: number) : void {
    const newNote = event.target.value;
    this.dataSource.data[index].note = newNote;
    this.dataSource.data = [...this.dataSource.data]
    this.isEditNoteActive[index] = false;
    this.saveNewNote(this.dataSource.data[index].userId, event.target.value)
  }

  private saveNewNote(userId: string, newNote: string): void {
    this.accountManagementService.updateNote(userId, newNote).subscribe({
      next: (res: any) => {
        this.toastr.success(`Note has been updated.`, 'Success');
      },
      error: (err: any) => {
        console.log(err);
      }
    })
  }
}
