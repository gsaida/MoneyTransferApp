package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


public class TenmoService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) { this.authToken = authToken; }

    public BigDecimal getBalance(int accountId) {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "accounts/" + accountId + "/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
//            balance = restTemplate.getForObject(API_BASE_URL + "accounts/" + accountId + "/balance", BigDecimal.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public List<String> getTransfers(int accountId) {
        List<String> transfers = null;
        try {
            transfers = restTemplate.getForObject(API_BASE_URL + "accounts/" + accountId + "/transfers", List.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public List<String> getAllUsers() {
        List<String> allUsers = null;
        try {
            allUsers = restTemplate.getForObject(API_BASE_URL + "users", List.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return allUsers;
    }

//    public User sendMoney() {
//        HttpEntity<User> transfer =
//
//    }

    public List<String> getPendingRequests() {
        List<String> requests = null;
        return requests;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
