package com.tafrica.mopapov2.ReportsMenuItems;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.tafrica.mopapov2.R;
import com.tafrica.mopapov2.RecepitMenuItems.receipt;
import com.tafrica.mopapov2.main;

public class reports extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        AlertDialog.Builder builder = new AlertDialog.Builder(reports.this);
        builder.setMessage("This function is still under development):");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gobacktomain();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void gobacktomain() {
        Intent intent = new Intent(this,main.class);
        startActivity(intent);
    }
}
