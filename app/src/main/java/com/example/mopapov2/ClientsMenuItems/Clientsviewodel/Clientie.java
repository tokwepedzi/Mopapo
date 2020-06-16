package com.example.mopapov2.ClientsMenuItems.Clientsviewodel;

public class Clientie {
    private String name,loanamount; //name = name, description= address, image = loanamount,address

    public Clientie() {
    }

    public Clientie(String name, String loanamount) {
        this.name = name;
       // this.address = address;
        this.loanamount = loanamount;
    }

    public String getName() {
        return "Client name: "+name;
    }

    public void setName(String name) {
        this.name = name;
    }

   // public String getAddress() {
      //  return "Address:"+ " "+address;
   // }

    //public void setAddress(String address) {
     //   this.address = address;
   // }

    public String getLoanamount() {
        return "Amount owing = "+loanamount;
    }

    public void setLoanamount(String loanamount) {
        this.loanamount = loanamount;
    }
}
