package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferReceiveAccount {

    @JsonProperty("account_id_to")
    private int accountTo;
    private BigDecimal amount;

    public TransferReceiveAccount() {
    }

    public void setAccountTo(int accountId) { this.accountTo = accountId; }
    public int getAccountTo() { return accountTo; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getAmount() { return amount; }

}



