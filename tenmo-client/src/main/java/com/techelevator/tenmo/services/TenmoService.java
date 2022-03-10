package com.techelevator.tenmo.services;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class TenmoService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken){this.authToken = authToken;}

    public BigDecimal getBalance(){
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Transfer[] listHistory() {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "history", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer[] listPending() {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer sendBucks(long accountToId, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setAccountTo(accountToId);
        transfer.setAmount(amount);
        Transfer returnTransfer = restTemplate.postForObject(API_BASE_URL + "send", makeTransferEntity(transfer), Transfer.class);

        return returnTransfer;
    }

    public Transfer requestBucks(long accountFromId, BigDecimal amount){
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(accountFromId);
        transfer.setAmount(amount);
        Transfer returnTransfer = restTemplate.postForObject(API_BASE_URL + "request", makeTransferEntity(transfer), Transfer.class);
        return returnTransfer;
    }

    public User[] getUsers(){
        User[] users = null;
        try{
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "user", HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public boolean updatePending(long id, int approve){
        boolean success = false;
        if(approve == 0){
            return false;
        }
        String status = approve == 1 ? "Approved" : "Rejected";
        try{
            restTemplate.put(API_BASE_URL + "request/" + id + "/" + status, makeAuthEntity());
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
