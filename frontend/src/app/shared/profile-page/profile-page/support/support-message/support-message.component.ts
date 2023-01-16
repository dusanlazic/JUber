import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-support-message',
  templateUrl: './support-message.component.html',
  styleUrls: ['./support-message.component.sass']
})
export class SupportMessageComponent implements OnInit {

  @Input()
  message!: any

  constructor() { }

  ngOnInit(): void {
  }

}
