package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    List<Transfer> getHistory(long id);

    List<Transfer> viewPendingRequests(long id);

//    void sendBucks();
//
//    void requestBucks();

    Transfer createTransfer(Transfer transfer);

    Transfer updatePending(Transfer transfer, Long id);

    Transfer getTransferByID(Long id);


}
