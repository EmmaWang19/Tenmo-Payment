package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.NotAuthorizedException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.NotActiveException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
public class TenmoController {
    @Autowired
    private TransferDao transferDao;
    @Autowired
    private UserDao userDao;

public TenmoController(TransferDao transferDao) {
    this.transferDao = transferDao;
}

@RequestMapping(path = "/history", method = RequestMethod.GET)
    public List<Transfer> getHistory (Principal principal) {
    long userId = userDao.findIdByUsername(principal.getName());
    return transferDao.getHistory(userId);
}

@RequestMapping(path = "/pending", method = RequestMethod.GET)
    public List<Transfer> viewPending (Principal principal) {
    long userId = userDao.findIdByUsername(principal.getName());
    return transferDao.viewPendingRequests(userId);
}

@Transactional
@RequestMapping(path = "/send", method = RequestMethod.POST)
    public Transfer sendBucks (@RequestBody Transfer transfer, Principal principal) throws NotAuthorizedException {
    long userId = userDao.findIdByUsername(principal.getName());
    transfer.setAccountFrom(userId);
    if (transfer.getAccountFrom() == transfer.getAccountTo()) {
        throw new NotAuthorizedException();
    }
    else if (transfer.getAmount().compareTo(userDao.getBalance(transfer.getAccountFrom())) == 1){
        throw new NotAuthorizedException();
    } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) != 1){
        throw new NotAuthorizedException();
    }
    transfer.setTransferType("Send");
    transfer.setTransferStatus("Approved");
    userDao.updateBalance(transfer.getAmount().negate(), transfer.getAccountFrom());
    userDao.updateBalance(transfer.getAmount(), transfer.getAccountTo());
    return transferDao.createTransfer(transfer);
}

@RequestMapping(path = "/request", method = RequestMethod.POST)
    public Transfer requestBucks (@RequestBody Transfer transfer, Principal principal) throws NotAuthorizedException {
    long userId = userDao.findIdByUsername(principal.getName());
    transfer.setAccountTo(userId);
    if (!(transfer.getAccountTo()==userId)){
        throw new NotAuthorizedException();
    } else if(transfer.getAccountFrom() == transfer.getAccountTo()){
        throw new NotAuthorizedException();
    } else if(transfer.getAmount().compareTo(BigDecimal.ZERO) != 1){
        throw new NotAuthorizedException();
    }
    transfer.setTransferType("Request");
    transfer.setTransferStatus("Pending");
    return transferDao.createTransfer(transfer);
}

@Transactional
@RequestMapping(path="/request/{id}/{status}", method = RequestMethod.PUT)
    public void updatePendingRequest (@PathVariable long id, @PathVariable String status) throws NotAuthorizedException {

    if (status.equals("Approved")) {
        Transfer transfer = transferDao.getTransferByID(id);
        transfer.setAccountFrom(new Long(userDao.findIdByUsername(transfer.getUserFrom())));
        transfer.setAccountTo(new Long(userDao.findIdByUsername(transfer.getUserTo())));
        if (transfer.getAmount().compareTo(userDao.getBalance(transfer.getAccountFrom()))==1) {
            throw new NotAuthorizedException();
        }
        userDao.updateBalance(transfer.getAmount().negate(), transfer.getAccountFrom());
        userDao.updateBalance(transfer.getAmount(), transfer.getAccountTo());
    }
    transferDao.updatePending(status,id);
}

@RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal){
    return userDao.getBalance(userDao.findIdByUsername(principal.getName()));
}

@RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<User> listOfUsers () {
    return userDao.findAll();
}

@RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer transferDetailById (@PathVariable long id) {
    return transferDao.getTransferByID(id);
}


}
