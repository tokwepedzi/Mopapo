package com.example.mopapov2.AccountsMenuItems.DebtorsAccountsModel;

public class DebtorsAccountsInfo {
    private String name,loanamount,paidamount;

    public DebtorsAccountsInfo() {
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
