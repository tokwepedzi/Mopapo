package com.tafrica.mopapov2.PaymentModel;

public class Payer {
    private String balance,clientname,duedate,paid,disbursementdate,paymentdate;

    public Payer(String balance, String clientname, String paid, String paymentdate) {
        this.balance = balance;
        this.clientname = clientname;
        this.paid = paid;
        this.paymentdate = paymentdate;
    }

    public Payer(String balance, String clientname, String duedate, String paid, String disbursementdate, String paymentdate) {
        this.balance = balance;
        this.clientname = clientname;
        this.duedate = duedate;
        this.paid = paid;
        this.disbursementdate = disbursementdate;
        this.paymentdate= paymentdate;
    }

    public String getBalance() {
        return balance;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
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

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getDisbursementdate() {
        return disbursementdate;
    }

    public void setDisbursementdate(String disbursementdate) {
        this.disbursementdate = disbursementdate;
    }

    public Payer() {
    }
}
