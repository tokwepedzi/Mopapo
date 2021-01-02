package com.tafrica.mopapov2;

public class Disbursee {
    private String balance,clientname,disbursementdate, duedate,amount,interest,amountdue;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getDisbursementdate() {
        return disbursementdate;
    }

    public void setDisbursementdate(String disbursementdate) {
        this.disbursementdate = disbursementdate;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getAmountdue() {
        return amountdue;
    }

    public void setAmountdue(String amountdue) {
        this.amountdue = amountdue;
    }

    public Disbursee(String clientname, String disbursementdate, String amount, String amountdue) {
        this.clientname = clientname;
        this.disbursementdate = disbursementdate;
        this.amount = amount;
        this.amountdue = amountdue;
    }

    public Disbursee(String balance, String clientname, String disbursementdate, String duedate, String amount, String interest, String amountdue) {
        this.balance = balance;
        this.clientname = clientname;
        this.disbursementdate = disbursementdate;
        this.duedate = duedate;
        this.amount = amount;
        this.interest = interest;
        this.amountdue = amountdue;
    }

    public Disbursee() {
    }
}
