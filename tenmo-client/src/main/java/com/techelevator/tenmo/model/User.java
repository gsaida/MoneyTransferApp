package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class User {

    private int id;
    private String username;
    private BigDecimal balance;
    private int accountId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public int getAccountId(){
        return accountId;
    }
    public void setAccountId(int accountId){
        this.accountId = accountId;
    }




    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            User otherUser = (User) other;
            return otherUser.getId() == id
                    && otherUser.getUsername().equals(username);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
