import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, OnInit, Input, Output, EventEmitter, ViewChild, OnChanges } from '@angular/core';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { PastRidesResponse } from 'src/app/profile/profile-navigation/past-rides/past-rides.component';

@Component({
  selector: 'app-past-rides-table',
  templateUrl: './past-rides-table.component.html',
  styleUrls: ['./past-rides-table.component.sass']
})
export class PastRidesTableComponent implements OnInit, OnChanges {

  @Input() 
  rides: Array<PastRidesResponse> = new Array<PastRidesResponse>()
  @Output('onRowClicked')rowClickedEvent = new EventEmitter<PastRidesResponse>();


  displayedColumns: string[] = ['startPlaceName', 'formattedDate', 'startTime', 'endTime', 'fare'];
  dataSource: any;

  @ViewChild(MatSort)
  sort: MatSort = new MatSort;
  
  constructor(
    private _liveAnnouncer: LiveAnnouncer
  ) { }

  ngOnInit(): void {
    this.setupDataSource();
  }
  ngOnChanges(changes: any) {
    this.rides = changes.rides.currentValue;
    this.setupDataSource();
  }

  private setupDataSource(): void{
    this.dataSource = new MatTableDataSource(this.rides);
    this.dataSource.sort = this.sort;
  }

  clickedRow(elem: PastRidesResponse): void{
    this.rowClickedEvent.emit(elem)
  }

  
  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }


}
