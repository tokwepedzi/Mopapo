package com.tafrica.mopapov2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tafrica.mopapov2.DeviceConfig.GlobalDeviceDetails;
import com.tafrica.mopapov2.Login1;
import com.tafrica.mopapov2.LogoutListener;

public class BaseActivity extends AppCompatActivity implements LogoutListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((GlobalDeviceDetails) getApplication()).registerSessionListener(this);

        ((GlobalDeviceDetails) getApplication()).startUserSession();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        ((GlobalDeviceDetails) getApplication()).onUserInteracted();
    }

    @Override
    public void onSessionLogout(){
        finish();

        startActivity(new Intent(this, Login1.class));
    }
}
