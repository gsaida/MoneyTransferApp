package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferOriginAccount {

    @JsonProperty("account_id_from")
    private int accountFrom;
    private BigDecimal amount;

    public TransferOriginAccount() {

    }

    public void setAccountFrom(int accountId) { this.accountFrom = accountId; }
    public int getAccountFrom() { return accountFrom; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getAmount() { return amount; }



}
