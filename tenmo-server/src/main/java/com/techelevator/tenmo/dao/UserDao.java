package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    List<String> findUsernameAndId(int userId);

    User getUserById(int id);

    User findByUsername(String username);

    BigDecimal getBalance(int accountId);

    int findAccountIdByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
