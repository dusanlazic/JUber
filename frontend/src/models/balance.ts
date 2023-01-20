export interface BalanceResponse {
    balance: number;
}

export interface DepositAddressResponse {
    ethAddress: string;
}

export interface BalanceUpdatedMessage {
    currentBalance: number;
    increase: number;
}