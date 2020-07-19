package com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel;

public class DebtorsAccountsInfo {
    private String name,loanamount,paidamount,dailypaidamount,amountdue,principal,duedate,disbursementdate;

    public DebtorsAccountsInfo() {
    }

    public String getDailypaidamount() {
        return dailypaidamount;
    }

    public void setDailypaidamount(String dailypaidamount) {
        this.dailypaidamount = dailypaidamount;
    }

    public String getAmountdue() {
        return amountdue;
    }

    public void setAmountdue(String amountdue) {
        this.amountdue = amountdue;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getDisbursementdate() {
        return disbursementdate;
    }

    public void setDisbursementdate(String disbursementdate) {
        this.disbursementdate = disbursementdate;
    }

    public DebtorsAccountsInfo(String name, String loanamount, String paidamount, String dailypaidamount, String amountdue, String principal, String duedate, String disbursementdate) {
        this.name = name;
        this.loanamount = loanamount;
        this.paidamount = paidamount;
        this.dailypaidamount = dailypaidamount;
        this.amountdue = amountdue;
        this.principal = principal;
        this.duedate = duedate;
        this.disbursementdate = disbursementdate;
    }

    public DebtorsAccountsInfo(String name, String loanamount, String paidamount) {
        this.name = name;
        this.loanamount = loanamount;
        this.paidamount = paidamount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoanamount() {
        return loanamount;
    }

    public void setLoanamount(String loanamount) {
        this.loanamount = loanamount;
    }

    public String getPaidamount() {
        return paidamount;
    }

    public void setPaidamount(String paidamount) {
        this.paidamount = paidamount;
    }


}
