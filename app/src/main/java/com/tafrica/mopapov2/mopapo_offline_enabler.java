package com.tafrica.mopapov2;
import com.google.firebase.database.FirebaseDatabase;
import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;

public class mopapo_offline_enabler extends GlobalDeviceDetails {

    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
