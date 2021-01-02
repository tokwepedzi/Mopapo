package com.tafrica.mopapov2.DeviceConfig;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class GlobalDeviceDetails extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
    /*
    private LogoutListener listener;
    Timer timer;

    public void startUserSession(){
        cancelTimer();
         timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //listener.onSessionLogout();


            }
        }, 30000000);//to change back 30 000
    }

    private void cancelTimer(){
        if(timer!=null) timer.cancel();
    }


    public void registerSessionListener(LogoutListener listener){
        this.listener = listener;

    }

    public void onUserInteracted(){
        startUserSession();

    }








    /* private String industryname;
    private String companyname;
    private String branchname;




    public String getIndustryname() {
        return industryname;
    }

    public void setIndustryname(String industryname) {
        this.industryname = industryname;
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
    }*/
}
