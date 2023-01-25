import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Component, OnInit,Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { RideReview } from 'src/models/rideReview';

@Component({
  selector: 'app-reviews-table',
  templateUrl: './reviews-table.component.html',
  styleUrls: ['./reviews-table.component.sass']
})
export class ReviewsTableComponent implements OnInit {


  @Input() 
  reviews: Array<RideReview> = new Array<RideReview>()
  @Output('onRowClicked')rowClickedEvent = new EventEmitter<RideReview>();


  displayedColumns: string[] = ['reviewerFullName', 'comment', 'driverRating', 'vehicleRating'];
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
    this.reviews = changes.reviews.currentValue;
    this.setupDataSource();
  }

  private setupDataSource(): void{
    this.dataSource = new MatTableDataSource(this.reviews);
    this.dataSource.sort = this.sort;
  }

  clickedRow(elem: RideReview): void{
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
