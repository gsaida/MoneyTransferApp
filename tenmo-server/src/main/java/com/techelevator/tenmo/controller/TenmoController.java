package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    private JdbcUserDao jdbcUserDao;
    private UserDao userDao;
    private TransferDao transferDAO;

    public TenmoController(JdbcUserDao jdbcUserDao, UserDao userDao) {
        this.userDao = userDao;
        this.jdbcUserDao = jdbcUserDao;
    }


    @RequestMapping(path = "accounts/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable("id") int accountId, Principal principal) {
        BigDecimal balance = userDao.getBalance(accountId);
        if (userDao.findAccountIdByUsername(principal.getName()) == accountId) {
            if (balance == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
            } else {
                return balance;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error. You are not authorized.");
        }
    }

    @RequestMapping(path = "users", method = RequestMethod.GET)
    public List<String> getAllUsernamesAndIds(Principal principal) {
        if(userDao.findUsernameAndId(userDao.findIdByUsername(principal.getName())) != null) {
            return userDao.findUsernameAndId(userDao.findIdByUsername(principal.getName()));
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found");
        }
    }



    @RequestMapping(path = "/whoAmI")
    public String whoAmI(Principal principal) {
        return principal.getName();

    }

}
