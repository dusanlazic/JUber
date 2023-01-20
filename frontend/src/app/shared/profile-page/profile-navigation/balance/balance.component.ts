import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-balance',
  templateUrl: './balance.component.html',
  styleUrls: ['./balance.component.sass']
})
export class BalanceComponent implements OnInit {

  balance: number;
  ethAddress: string;

  constructor() {
    this.balance = 0;
    this.ethAddress = '';
  }

  ngOnInit(): void {
    this.balance = 420
    this.ethAddress = "0x01d3aa54b06728c5002e22d1558cd6d0a2c04c92";
  }

}
