package com.tafrica.mopapov2;

public class Companycontact {
    private  String companyname,branchname,address, contactname,cellnumber,emailaddress;

    public Companycontact(String companyname, String branchname, String address, String contactname, String cellnumber, String emailaddress) {
        this.companyname = companyname;
        this.branchname = branchname;
        this.address = address;
        this.contactname = contactname;
        this.cellnumber = cellnumber;
        this.emailaddress = emailaddress;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getCellnumber() {
        return cellnumber;
    }

    public void setCellnumber(String cellnumber) {
        this.cellnumber = cellnumber;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public Companycontact() {
    }
}
