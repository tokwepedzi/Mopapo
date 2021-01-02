package com.tafrica.mopapov2;

public class Licensing {
    private String  expirydate,licensekey,versionname,trialperiod;

    public String getExpirydate() {
        return expirydate;
    }

    public String getTrialperiod() {
        return trialperiod;
    }

    public void setTrialperiod(String trialperiod) {
        this.trialperiod = trialperiod;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getLicensekey() {
        return licensekey;
    }

    public void setLicensekey(String licensekey) {
        this.licensekey = licensekey;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public Licensing(String expirydate, String licensekey, String versionname,String trialperiod) {
        this.expirydate = expirydate;
        this.licensekey = licensekey;
        this.versionname = versionname;
        this.trialperiod = trialperiod;
    }

    public Licensing() {
    }
}
