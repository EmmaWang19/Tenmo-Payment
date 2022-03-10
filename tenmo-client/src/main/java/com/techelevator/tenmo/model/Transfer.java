package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.security.Principal;

public class Transfer {

    private Long id;
    private String transferType;
    private String transferStatus;
    private String userFrom;
    private Long accountFrom;
    private String userTo;
    private Long accountTo;
    private BigDecimal amount;

    @Override
    public String toString() {
        return "\n--------------------------------------------" +
                "\n Transfer Details" +
                "\n--------------------------------------------" +
                "\n Id: " + id +
                "\n Type: " + transferType +
                "\n Status: " + transferStatus +
                "\n From: " + userFrom +
                "\n To: " + userTo +
                "\n Amount: " + amount;
    }

    public String toTransferFormat() {
        String Id = "          ";
        String from = "           ";
        String send = transferType.equals("Send") ? "To:   " + userTo : "From: " + userFrom;
        String output = ""+id;
        for(int i=output.length(); i<12; i++){
            output += " ";
        }
        String output1 = send;
        for(int i=output1.length(); i<20; i++){
            output1 += " ";
        }
        return
               output + output1 + "$ "+amount;

    }

    public String toPendingFormat(){
        String Id = "          ";
        String from = "           ";
        String output = ""+id;
        for(int i=output.length(); i<12; i++){
            output += " ";
        }
        String output1 = userTo;
        for(int i=output1.length(); i<20; i++){
            output1 += " ";
        }
        return output + output1 + "$ "+amount;
    }

    public Long getId() {
        return id;
    }

    public String getTransferType() {
        return transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
