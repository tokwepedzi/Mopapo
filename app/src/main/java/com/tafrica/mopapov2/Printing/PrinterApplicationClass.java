package com.tafrica.mopapov2.Printing;

import android.app.Application;

import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.google.firebase.database.FirebaseDatabase;
import com.mazenrashed.printooth.Printooth;

public class PrinterApplicationClass extends GlobalDeviceDetails {

    @Override
    public void onCreate() {
        super.onCreate();
        Printooth.INSTANCE.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
