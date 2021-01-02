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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CurrentListview extends AppCompatActivity {

    DatabaseReference DebtorsRef;
    FirebaseListAdapter currentadapter,thirtyplusadapter,sixtyplusadapter,ninetyplusadapter;
    ListView mCurrent,mThirtyplus,mSixtyplus,mNinetyplus;
    TextView mCurrenttot;

    String companyname,branchname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_listview);

        mCurrent = (ListView) findViewById(R.id.currentreceivableslistview);
       // mCurrenttot = (TextView) findViewById(R.id.currenttotalttxtvw);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");

        DebtorsRef =FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " debtors accounts").child(branchname);
        // DebtorsRef.keepSynced(true);
        DebtorsRef.keepSynced(true);

        DebtorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float currentsum = 0;
                float totalsum = 0;
                try{
                    Date date1;
                    Date date2;
                    Calendar calendar = Calendar.getInstance();
                    String datetoday = DateFormat.getInstance().format(calendar.getTime());
                    SimpleDateFormat  simpleDateFormat= new SimpleDateFormat("dd-M-YYYY");
                    datetoday = simpleDateFormat.format(calendar.getTime());

                    //set dates
                    date1 = simpleDateFormat.parse(datetoday);
                    for(DataSnapshot ds: dataSnapshot.getChildren()){

                        Map<String,Object> map = (Map<String,Object>) ds.getValue();
                        Object disburseddate = map.get("disbursementdate");

                        date2 = simpleDateFormat.parse(disburseddate.toString());

                        //Comparing dates
                        long difference = Math.abs(date1.getTime() - date2.getTime()) ;
                        long differenceinDates = difference/(24*60*60*1000);

                        if(differenceinDates<31){
                            Object currentTotal = map.get("amountdue");
                            float currentvalues = Float.parseFloat(String.valueOf(currentTotal));
                            currentsum += currentvalues;
                            mCurrenttot.setText("$"+currentsum+"0");

                            //repeat for the other receivables' periods


                        }

                    }
                }

                catch (Exception e){}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Query query = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " debtors accounts").child(branchname);


        FirebaseListOptions<DebtorsAccountsInfo> currentlistoption = new FirebaseListOptions.Builder<DebtorsAccountsInfo>()
                .setLayout(R.layout.collectio_report_listview)
                //.setLifecycleOwner(CurrentListview.this)
                .setQuery(query,DebtorsAccountsInfo.class)
                .build();


        currentadapter = new FirebaseListAdapter(currentlistoption) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView mClientname= v.findViewById(R.id.client_name_lstvw_item);
                //TextView mPaidamount = v.findViewById(R.id.paid_amount_lstvw_item);
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
                    if(differenceinDates<31 && differenceinDates>0){
                      mClientname.setTextColor(Color.rgb(0,118,0));
                      mBalance.setTextColor(Color.rgb(0,118,0));
                    }






                }

                catch (Exception e){

                }

            }

        };



        mCurrent.setAdapter(currentadapter);


    }

    @Override
    protected void onStart(){
        super.onStart();
        currentadapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        currentadapter.stopListening();
    }
}






/*##########################################FALLBACK###################################

{

    DatabaseReference DebtorsRef;
    FirebaseListAdapter currentadapter,thirtyplusadapter,sixtyplusadapter,ninetyplusadapter;
    ListView mCurrent,mThirtyplus,mSixtyplus,mNinetyplus;

    String companyname,branchname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_listview);

        mCurrent = (ListView) findViewById(R.id.currentreceivableslistview);
       // mThirtyplus = (ListView) findViewById(R.id.thirty_plus_listview);
       // mSixtyplus = (ListView) findViewById(R.id.sixty_plus_listview);
       // mNinetyplus = (ListView) findViewById(R.id.ninety_plus_listview);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");


        Query query = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " debtors accounts").child(branchname);


        FirebaseListOptions<DebtorsAccountsInfo> currentlistoption = new FirebaseListOptions.Builder<DebtorsAccountsInfo>()
                .setLayout(R.layout.current_listview_layout_template)
                //.setLifecycleOwner(CurrentListview.this)
                .setQuery(query,DebtorsAccountsInfo.class)
                .build();


        currentadapter = new FirebaseListAdapter(currentlistoption) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView mClientname= v.findViewById(R.id.current_client_name_lstvw_item);
                //TextView mPaidamount = v.findViewById(R.id.paid_amount_lstvw_item);
                //TextView mDuedate = v.findViewById(R.id.due_date_lstvw_item);
                TextView mBalance = v.findViewById(R.id.current_balance_lstvw_item);
                //TextView mDisbursementdate = v.findViewById(R.id.disbursement_date_lstvw_item);
                try{
                Date date1;@
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

                if(differenceinDates<31){
                    mClientname.setText(currentdebtor.getName().toString());
                    //mPaidamount.setText(currentdebtor.getAmountdue().toString());
                    //mDuedate.setText(currentdebtor.getDuedate().toString());
                    mBalance.setText(currentdebtor.getAmountdue().toString());

                }






                }

                catch (Exception e){

                }

            }

        };

        mCurrent.setAdapter(currentadapter);

    }

    @Override
    protected void onStart(){
        super.onStart();
        currentadapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        currentadapter.stopListening();
    }
}


 */
