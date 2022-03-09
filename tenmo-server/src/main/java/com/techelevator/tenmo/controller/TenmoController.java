package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.NotAuthorizedException;
import com.techelevator.tenmo.model.Transfer;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

@RequestMapping(path = "/send", method = RequestMethod.POST)
    public Transfer sendBucks (@RequestBody Transfer transfer, Principal principal) throws NotAuthorizedException {
    long userId = userDao.findIdByUsername(principal.getName());
    if (!(transfer.getAccountFrom()==userId)) {
        throw new NotAuthorizedException();
    }
    else if (transfer.getAccountFrom() == transfer.getAccountTo()) {
        throw new NotAuthorizedException();
    }
    else if (transfer.getAmount().compareTo(userDao.getBalance(transfer.getAccountFrom())) == 1){
        throw new NotAuthorizedException();
    } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) != 1){
        throw new NotAuthorizedException();
    }
    transfer.setTransferType("Send");
    transfer.setTransferStatus("Approved");
    BigDecimal newBalanceFrom = userDao.getBalance(transfer.getAccountFrom()).subtract(transfer.getAmount());
    BigDecimal newBalanceTo = userDao.getBalance(transfer.getAccountTo()).add(transfer.getAmount());
    userDao.updateBalance(newBalanceFrom, transfer.getAccountFrom());
    userDao.updateBalance(newBalanceTo, transfer.getAccountTo());
    return transferDao.createTransfer(transfer);
}

@RequestMapping(path = "/request", method = RequestMethod.POST)
    public Transfer requestBucks (@RequestBody Transfer transfer, Principal principal) throws NotAuthorizedException {
    long userId = userDao.findIdByUsername(principal.getName());
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

@RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal){
    return userDao.getBalance(userDao.findIdByUsername(principal.getName()));
}


}
