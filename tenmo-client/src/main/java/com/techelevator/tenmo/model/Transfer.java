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

    public String viewTransfers () {
        String Id = "          ";
        String from = "           ";
        String to = "            ";
        return "\n --------------------------------------------"+
                "\n Transfers"+
                "\n ID"+Id.substring(2)+"From/To"+from.substring(7)+"Amount"+
                "\n --------------------------------------------"+
                "\n "+Id.substring(4)+transferType+": "+userFrom+from.substring(userFrom.length()+transferType.length()+2)+"$ "+amount+
                "\n ---------------"+
                "\n Please enter transfer ID to view details (0 to cancel): ";
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
