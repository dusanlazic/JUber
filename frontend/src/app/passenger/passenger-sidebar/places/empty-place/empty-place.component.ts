import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MapService } from 'src/services/map/map.service';

@Component({
  selector: 'app-empty-place',
  templateUrl: './empty-place.component.html',
  styleUrls: ['./empty-place.component.sass']
})
export class EmptyPlaceComponent implements OnInit {

  name: string = '';
  
  @Output('updateEvent')
  change: EventEmitter<any> = new EventEmitter<any>();

  constructor(private map: MapService) { }

  ngOnInit(): void {
  }

  confirm() {
    this.change.emit({confirmed: true, name: this.name})
  }

  cancel() {
    this.change.emit({confirmed: false, name: this.name})
  }

  preview() {
    this.map.setPreviewLocation(this.name);
  }

}
