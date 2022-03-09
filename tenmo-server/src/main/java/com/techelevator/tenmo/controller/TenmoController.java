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
    if (!(userDao.findIdByUsername(transfer.getAccountFrom())==userId)) {
        throw new NotAuthorizedException();
    }
    else if (transfer.getAccountFrom().equalsIgnoreCase(transfer.getAccountTo())) {
        throw new NotAuthorizedException();
    }
//    else if (transfer.getAmount().compareTo(userDao.findByUsername(principal.getName()).g))
    return null;
}

}
