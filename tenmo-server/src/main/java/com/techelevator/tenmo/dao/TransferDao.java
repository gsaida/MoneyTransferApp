package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    boolean transferAllowed(BigDecimal transfer, int accountId);

    boolean createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount);

    BigDecimal subtractTransferAmount(BigDecimal transferAmount, int accountId);

    BigDecimal addTransferAmount(BigDecimal transferAmount, int accountId);

    void fullTransfer(int accountFrom, int accountTo, BigDecimal amount);

    List<String>seeMyTransfers(int accountId);

    String getTransferById(int transferId);


}
