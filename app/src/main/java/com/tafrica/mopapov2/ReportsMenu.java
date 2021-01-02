package com.tafrica.mopapov2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.tafrica.mopapov2.ReportsMenuItems.Cashup_report;
import com.tafrica.mopapov2.ReportsMenuItems.reports;

public class ReportsMenu extends AppCompatActivity {

    RelativeLayout mCollectionreports, mDocuments,mAgingreports,mDisbursmentreports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_menu);

        mCollectionreports = (RelativeLayout) findViewById(R.id.collection_reports_btn);
        mDocuments = (RelativeLayout) findViewById(R.id.documents_btn);
        mAgingreports = (RelativeLayout) findViewById(R.id.aging_btn);
        mDisbursmentreports = (RelativeLayout) findViewById(R.id.disbursements_reports_btn);


        mCollectionreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCollectioReport();
            }
        });

        mAgingreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAgingReports();
                //finish();
            }
        });


        mDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCashupReports();
            }
        });

        mDisbursmentreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDisbursmentReports();
            }
        });

    }

    private void openCashupReports() {

        Intent intent = new Intent(this, Cashup_report.class);
        startActivity(intent);
        //finish();
    }

    private void openDisbursmentReports() {
        Intent intent = new Intent(this, DisbursementsReports.class);
        startActivity(intent);
        //finish();

    }

    private void openAgingReports() {

        Intent intent = new Intent(this, AgingReports.class);
        startActivity(intent);
        //finish();
    }

    private void openCollectioReport() {
        Intent intent = new Intent(this, reports.class);
        startActivity(intent);
        //finish();
    }
}
