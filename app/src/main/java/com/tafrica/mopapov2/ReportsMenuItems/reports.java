package com.tafrica.mopapov2.ReportsMenuItems;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tafrica.mopapov2.DatedPickerFragment;
import com.tafrica.mopapov2.PaymentModel.Payer;
import com.tafrica.mopapov2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class reports extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout mselectDate, mSelectDate2;
    TextView mReportsDate,mReportsDate2,mTotalCollections,mTotalcashedup;
    Button mLoadReports;
    ListView mReportList;
    DatabaseReference PaymentsRef;
    String companyname,branchname;
    private boolean startDateOrEndDAte = true;
    private ProgressDialog mLoadingBar;
    ArrayList<Payer> payerlist,payerlist1;
    Date startdate, enddate;
    String startstring ;
    String endstring ;
    Payer payer;
    Calendar start = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);


        mselectDate = (LinearLayout) findViewById(R.id.select_date_linear_btn);
        mSelectDate2 = (LinearLayout) findViewById(R.id.select_date2_linear_btn);
        mReportsDate = (TextView) findViewById(R.id.date_txtvw);
        mReportsDate2 = (TextView) findViewById(R.id.date2_txtvw);
        mLoadingBar = new ProgressDialog(reports.this);
        mLoadReports = (Button) findViewById(R.id.load_reports_btn);
        mReportList= (ListView) findViewById(R.id.report_listvw);
        mTotalCollections = (TextView) findViewById(R.id.total_amnt_displaytxtvw);
        mLoadReports.setVisibility(View.INVISIBLE);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");

        PaymentsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " payments").child(branchname);
        PaymentsRef.keepSynced(true);


        DialogFragment datepicker = new DatedPickerFragment();
        payerlist = new ArrayList<>();
        payerlist1 = new ArrayList<>();


       mselectDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               datepicker.show(getSupportFragmentManager(), "datepicker");
               startDateOrEndDAte = true;


           }
       });

       mSelectDate2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               datepicker.show(getSupportFragmentManager(), "datepicker");
               startDateOrEndDAte = false;
           }
       });

       CollectionsListAdapter adapter = new CollectionsListAdapter(this,R.layout.collectio_report_listview,payerlist);


        mLoadReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLoadReports.setVisibility(View.INVISIBLE);
                mLoadingBar.setTitle("Collections Report");
                mLoadingBar.setMessage("...fetching data.");
                mLoadingBar.setCanceledOnTouchOutside(true);
                mLoadingBar.show();

                try { startdate = simpleDateFormat.parse(startstring);
                    enddate = simpleDateFormat.parse(endstring);
                    start.setTime(startdate);
                    end.setTime(enddate);}
                catch (Exception e) { }

                payer = new Payer();
                PaymentsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> dateskeys = new ArrayList<>();
                        List<String> nameskeys = new ArrayList<>();
                        List<Payer> wholelist = new ArrayList<>();
                        Date datefromwholelist= new Date();
                        Calendar calendar= Calendar.getInstance();

                        for (DataSnapshot dateskeysnode : dataSnapshot.getChildren()) {
                            dateskeys.clear();
                            dateskeys.add(dateskeysnode.getKey());

                            for(int i=0;i<dateskeys.size();i++){
                                for(DataSnapshot nameskeynode:dateskeysnode.getChildren()){
                                    nameskeys.clear();
                                    nameskeys.add(nameskeynode.getKey());
                                    //list.add(dateskeysnode.getKey()+"  "+nameskeynode.getKey());
                                    for(int i1=0;i1<nameskeys.size();i1++){

                                        Payer payer = nameskeynode.getValue(Payer.class);
                                        wholelist.add(payer);
                                    }
                                }
                            }
                        }

                        payerlist.clear();
                        double paid1 = 0;
                        double totalcollected=0;
                        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()){

                                 /*starting with the start date get all objects from the wholelist ( for(int i=0;i<wholelist.size();i++))and
                                    check which one item has its date value equal to date and add that item to list1, add 1 day to date and repeat
                                    the above now using date+1 in place of date....do this while date value is less than end date*/

                            for(int i=0;i<wholelist.size();i++){
                                Payer payer1 = wholelist.get(i);
                                String datestringfrmwholelist = payer1.getPaymentdate();
                                try{ datefromwholelist= simpleDateFormat.parse(datestringfrmwholelist);
                                    calendar.setTime(datefromwholelist);
                                }

                                catch (Exception e){ }


                                if (date.equals(datefromwholelist)){
                                    payerlist.add(payer1);


                                }


                            }
                        }

                        //add total payments and set the total as text in total amount collected
                        for(int j=0;j<payerlist.size();j++){
                            Payer payer2 = payerlist.get(j);
                            String total = payer2.getPaid();
                            //String totalpaid = disbursee2.getAmountdue();
                            double paiddb = Double.parseDouble(String.valueOf(total));
                            //double disbursedaccrual = Double.parseDouble(String.valueOf(totalaccrual));
                            paid1+=paiddb;
                            //disbursed1accrual+=disbursedaccrual;
                            mTotalCollections.setText("$"+paid1+"");
                            //mTotalplusAccrual.setText(disbursed1accrual+"");
                        }

                        // Originally here mReportList.setAdapter(adapter);
                        mLoadingBar.hide();
                        /*if(payerlist.isEmpty()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(reports.this);
                            builder.setMessage("No data was found for the selected period!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }*/

                        if(payerlist.isEmpty()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(reports.this);
                            builder.setMessage("No records found for the selected period!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }









                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                mReportList.setAdapter(adapter);
                //mLoadingBar.hide();


            }


        });


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,i);
        cal.set(Calendar.MONTH,i1);
        cal.set(Calendar.DAY_OF_MONTH,i2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = simpleDateFormat.format(cal.getTime());

        //set start date
        if(startDateOrEndDAte){
            mReportsDate.setText(currentDateString);
            mTotalCollections.setText("0.00");
            startstring = mReportsDate.getText().toString();
            mLoadReports.setVisibility(View.VISIBLE);}

        //set end date
        else {
            mReportsDate2.setText(currentDateString);
            mTotalCollections.setText("0.00");
            endstring = mReportsDate2.getText().toString();
            mLoadReports.setVisibility(View.VISIBLE);

        }



    }

}
//https://www.youtube.com/watch?v=QZPzWNbjAV0 (OLD CODE THAT WAS HERE)