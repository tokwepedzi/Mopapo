package com.tafrica.mopapov2.ClientsMenuItems;

public class Client {
   private String name;
   private String idnum;
   private String cellNum;
   private String address;
   private String groupName;
   private String collateral;
   private String nxtKinName;
   private String nxtKinAddrss;
   private String nxtKinCell;
   //private String loanamount;
   //private String paidamount;


    public Client() {
    }

   // public Client(String loanamount) {
    //    this.loanamount = loanamount;
   // }

    //public String getLoanamount() {
     //   return loanamount;
   // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getCellNum() {
        return cellNum;
    }

    public void setCellNum(String cellNum) {
        this.cellNum = cellNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCollateral() {
        return collateral;
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }

    public String getNxtKinName() {
        return nxtKinName;
    }

    public void setNxtKinName(String nxtKinName) {
        this.nxtKinName = nxtKinName;
    }

    public String getNxtKinAddrss() {
        return nxtKinAddrss;
    }

    public void setNxtKinAddrss(String nxtKinAddrss) {
        this.nxtKinAddrss = nxtKinAddrss;
    }

    public String getNxtKinCell() {
        return nxtKinCell;
    }

    public void setNxtKinCell(String nxtKinCell) {
        this.nxtKinCell = nxtKinCell;
    }

}
