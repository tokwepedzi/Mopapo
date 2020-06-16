package com.example.mopapov2;
import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class mopapo_offline_enabler extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
