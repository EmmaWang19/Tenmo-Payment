package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getHistory();

    List<Transfer> viewPendingRequests();

//    void sendBucks();
//
//    void requestBucks();

    Transfer createTransfer(boolean request);

    Transfer updatePending(Transfer transfer, Long id);



}
