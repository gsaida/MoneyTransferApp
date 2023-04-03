package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private UserDao userDao;
    private Principal principal;
    private final JdbcTemplate jdbcTemplate;

//    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }
    @Override
    public boolean transferAllowed(BigDecimal transfer, int accountId) {
        BigDecimal currentBalance = userDao.getBalance(accountId);

        if (transfer.compareTo(currentBalance) != 1 && (transfer.signum() > 0)) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You do not have enough money in your account.");

        }
    }


    @Override
    public boolean createTransfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount){
        String sql = "INSERT into transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer newTransferId;
        if(accountFrom == accountTo) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "You cannot send money to yourself");
        }
        newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusId, accountFrom, accountTo, amount);

        if (newTransferId == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public BigDecimal subtractTransferAmount(BigDecimal transferAmount, int accountId){
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, transferAmount, accountId);

        BigDecimal newBalance = userDao.getBalance(accountId);

        return newBalance;
    }

    @Override
    public BigDecimal addTransferAmount(BigDecimal transferAmount, int accountId){
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";

        jdbcTemplate.update(sql, transferAmount, accountId);

        BigDecimal newBalance = userDao.getBalance(accountId);

        return newBalance;
    }

    @Override
    public void fullTransfer(int accountFrom, int accountTo, BigDecimal amount){
        subtractTransferAmount(amount, accountFrom);
        addTransferAmount(amount, accountTo);
    }



    public List<String> seeMyTransfers(int accountId){
        List<String> myTransfers = new ArrayList<>();
        String sql = "select * from transfer WHERE account_from = ? OR account_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while(results.next()) {
            Transfer transferResult = mapRowToTransfer(results);
            String transferString = transferResult.toString();
            myTransfers.add(transferString);
        }
        if(myTransfers.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You do not have any transfers to display");
        }
        else {
            return myTransfers;
        }
    }

    @Override
    public String getTransferById(int transferId) {
        String transfer = null;
        String sql = "SELECT * from transfer WHERE transfer_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if (results.next()) {
            transfer = "Transfer ID: " + results.getInt("transfer_id") + ", Transfer Type ID: " + results.getInt("transfer_type_id") +
                    ", Transfer status ID: " + results.getInt("transfer_status_id") + ", Account from: " + results.getInt("account_from") +
                    ", Account to: " + results.getInt("account_to") + ", Transfer amount: " + results.getBigDecimal("amount").toString();
        }
        if(transfer == null) {
            return "No transfers found with that ID. Please try another Transfer ID";
        }
        else {
            return transfer;
        }
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }

}

