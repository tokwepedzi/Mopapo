package com.tafrica.mopapov2.AccountsMenuItems.CashupModel;

public class CahsupModelclass {
    public CahsupModelclass() {
    }

    private String totalcollection;

    public CahsupModelclass(String totalcollection, String cashinhand, String cashupdate,String shortfall) {
        this.totalcollection = totalcollection;
        this.cashinhand = cashinhand;
        this.cashupdate = cashupdate;
        this.shortfall = shortfall;
    }

    public CahsupModelclass(String totalcollection, String trnasport, String lunch, String airtime, String disbursements,
                            String other1, String other2, String cashinhand, String cashsubmitted, String shortfall, String totalcashcogn, String cashupdate) {
        this.totalcollection = totalcollection;
        this.trnasport = trnasport;
        this.lunch = lunch;
        this.airtime = airtime;
        this.disbursements = disbursements;
        this.other1 = other1;
        this.other2 = other2;
        this.cashinhand = cashinhand;
        this.cashsubmitted = cashsubmitted;
        this.shortfall = shortfall;
        this.totalcashcogn = totalcashcogn;
        this.cashupdate=cashupdate;
    }

    private String trnasport;

    public String getTotalcollection() {
        return totalcollection;
    }

    public void setTotalcollection(String totalcollection) {
        this.totalcollection = totalcollection;
    }

    public String getTrnasport() {
        return trnasport;
    }

    public void setTrnasport(String trnasport) {
        this.trnasport = trnasport;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getCashupdate() {
        return cashupdate;
    }

    public void setCashupdate(String cashupdate) {
        this.cashupdate = cashupdate;
    }

    public String getAirtime() {
        return airtime;
    }

    public void setAirtime(String airtime) {
        this.airtime = airtime;
    }

    public String getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(String disbursements) {
        this.disbursements = disbursements;
    }

    public String getOther1() {
        return other1;
    }

    public void setOther1(String other1) {
        this.other1 = other1;
    }

    public String getOther2() {
        return other2;
    }

    public void setOther2(String other2) {
        this.other2 = other2;
    }

    public String getCashinhand() {
        return cashinhand;
    }

    public void setCashinhand(String cashinhand) {
        this.cashinhand = cashinhand;
    }

    public String getCashsubmitted() {
        return cashsubmitted;
    }

    public void setCashsubmitted(String cashsubmitted) {
        this.cashsubmitted = cashsubmitted;
    }

    public String getShortfall() {
        return shortfall;
    }

    public void setShortfall(String shortfall) {
        this.shortfall = shortfall;
    }

    public String getTotalcashcogn() {
        return totalcashcogn;
    }

    public void setTotalcashcogn(String totalcashcogn) {
        this.totalcashcogn = totalcashcogn;
    }

    private String lunch;
    private String airtime;
    private String disbursements;
    private String other1;
    private String other2;
    private String cashinhand;
    private String cashsubmitted;
    private String shortfall;
    private String totalcashcogn;
    private  String cashupdate;
}
