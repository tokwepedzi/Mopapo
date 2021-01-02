package com.tafrica.mopapov2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NinetyPluslistview extends AppCompatActivity {

    FirebaseListAdapter ninetyplusadapter;
    ListView mNinetyplus;

    String companyname,branchname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninety_pluslistview);

        mNinetyplus = (ListView) findViewById(R.id.ninetyplusreceivableslistview);



        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");


        Query query = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " debtors accounts").child(branchname);
        query.keepSynced(true);


        FirebaseListOptions<DebtorsAccountsInfo> ninetypluslistoption = new FirebaseListOptions.Builder<DebtorsAccountsInfo>()
                .setLayout(R.layout.collectio_report_listview)
                .setLifecycleOwner(NinetyPluslistview.this)
                .setQuery(query,DebtorsAccountsInfo.class)
                .build();

        ninetyplusadapter = new FirebaseListAdapter(ninetypluslistoption) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView mClientname= v.findViewById(R.id.client_name_lstvw_item);
                TextView mPaidamount = v.findViewById(R.id.paid_amount_lstvw_item);
                //TextView mDuedate = v.findViewById(R.id.due_date_lstvw_item);
                TextView mBalance = v.findViewById(R.id.balance_lstvw_item);
                //TextView mDisbursementdate = v.findViewById(R.id.disbursement_date_lstvw_item);
                try{
                    Date date1;
                    Date date2;
                    Calendar calendar = Calendar.getInstance();
                    String datetoday = DateFormat.getDateInstance().format(calendar.getTime());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    datetoday = simpleDateFormat.format(calendar.getTime());

                    //setting dates
                    date1 = simpleDateFormat.parse(datetoday);
                    DebtorsAccountsInfo currentdebtor = (DebtorsAccountsInfo) model;
                    String disbursementdate= currentdebtor.getDisbursementdate();
                    date2 = simpleDateFormat.parse(disbursementdate);

                    //Comparing dates
                    long difference = Math.abs(date1.getTime() - date2.getTime()) ;
                    long differenceinDates = difference/(24*60*60*1000);

                    mClientname.setText(currentdebtor.getName().toString());
                    //mPaidamount.setText(currentdebtor.getAmountdue().toString());
                    //mDuedate.setText(currentdebtor.getDuedate().toString());
                    mBalance.setText(currentdebtor.getAmountdue().toString());
                    mClientname.setTextColor(Color.rgb(220,220,220));
                    mBalance.setTextColor(Color.rgb(220,220,220));
                    if(differenceinDates>90){
                        mClientname.setTextColor(Color.rgb(255,50,50));
                        mBalance.setTextColor(Color.rgb(255,50,50));
                    }



                }

                catch (Exception e){

                }

            }

        };
        mNinetyplus.setAdapter(ninetyplusadapter);
    }
}
