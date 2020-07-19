package com.tafrica.mopapov2;
import android.app.Application;

import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.google.firebase.database.FirebaseDatabase;

public class mopapo_offline_enabler extends GlobalDeviceDetails {

    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
