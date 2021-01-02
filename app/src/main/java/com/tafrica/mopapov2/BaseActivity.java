package com.tafrica.mopapov2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity implements LogoutListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // ((GlobalDeviceDetails) getApplication()).registerSessionListener(this);

       // ((GlobalDeviceDetails) getApplication()).startUserSession();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

       // ((GlobalDeviceDetails) getApplication()).onUserInteracted();
    }

   /* @Override
    public void onSessionLogout(){
        finish();

        startActivity(new Intent(this, Login1.class));
    }*/
}
