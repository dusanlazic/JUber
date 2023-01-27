import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { BalanceResponse, BalanceUpdatedMessage, DepositAddressResponse } from 'src/models/balance';
import { PaymentWebsocketshareService } from 'src/services/payment/payment-websocketshare.service';
import { PaymentService } from 'src/services/payment/payment.service';
import { Toastr } from 'src/services/util/toastr.service';
import { Clipboard } from '@angular/cdk/clipboard';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-balance',
  templateUrl: './balance.component.html',
  styleUrls: ['./balance.component.sass']
})
export class BalanceComponent implements OnInit, OnDestroy {

  balance: number;
  ethAddress: string;
  subscription: Subscription | undefined = undefined;

  constructor(
    private paymentService: PaymentService,
    private websocketService: PaymentWebsocketshareService,
    private toastr: Toastr,
    private clipboard: Clipboard
  ) {
    this.balance = 0.00
    this.ethAddress = "";
  }

  ngOnInit(): void {
    this.getBalance();
    this.getDepositAddress();
    this.subscribeToBalanceChanges();
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
    this.websocketService.onNewValueReceive('');
  }

  private getBalance() : void {
    this.paymentService.getBalance().subscribe({
      next: (balanceRes: BalanceResponse) => {
        this.balance = balanceRes.balance;
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  private getDepositAddress() : void {
    this.paymentService.getDepositAddress().subscribe({
      next: (depositAddr: DepositAddressResponse) => {
        this.ethAddress = depositAddr.ethAddress;
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  private subscribeToBalanceChanges() : void {
    this.subscription = this.websocketService.getNewValue().subscribe({
      next: (res: string) => {
        if (res) {
          const balanceChange = JSON.parse(res) as BalanceUpdatedMessage;
          this.balance = balanceChange.currentBalance;
          this.getDepositAddress();
          this.toastr.success(`Successfully deposited ${balanceChange.increase} RSD!`, 'Success');
          console.log(balanceChange);
        }
      },
      error: (res: HttpErrorResponse) => {
        console.log(res)
      },
    })
  }

  public copyAddress() {
    this.clipboard.copy(this.ethAddress);
    this.toastr.success('Deposit address copied!');
  }

  public expectBalanceChange() {
    this.getDepositAddress();
    this.toastr.info('Deposit address is now being monitored for transactions. If you made a deposit, it will show up at any moment.');
  }

}
