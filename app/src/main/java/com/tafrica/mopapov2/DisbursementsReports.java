
package com.tafrica.mopapov2;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DisbursementsReports extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    LinearLayout mselectDate, mselectDate2;
    TextView mDisburseReportsDate1, mDisburseReportsDate2, mTotalDisbursed,mTotalplusAccrual;
    Button mLoadDisburseReports;
    ListView mDisburseReportList;
    DatabaseReference DisbursementsRef;
    String companyname, branchname;
    private boolean startDateOrEndDAte = true;
    private ProgressDialog mLoadingBar;
    ArrayList<Disbursee> list,list1;
    Disbursee disbursee;
    Date startdate, enddate;
    String startstring ;
    String endstring ;
    Calendar start = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursements_reports);

        mselectDate = (LinearLayout) findViewById(R.id.select_date_linear_btn);
        mselectDate2 = (LinearLayout) findViewById(R.id.select_date2_linear_btn);
        mDisburseReportsDate1 = (TextView) findViewById(R.id.date_txtvw);
        mDisburseReportsDate2 = (TextView) findViewById(R.id.date2_txtvw);
        mLoadingBar = new ProgressDialog(DisbursementsReports.this);
        mLoadDisburseReports = (Button) findViewById(R.id.load_disburse_reports_btn);
        mDisburseReportList = (ListView) findViewById(R.id.disbursements_report_listvw);
        mTotalDisbursed = (TextView) findViewById(R.id.total_amnt_displaytxtvw);
        mTotalplusAccrual = (TextView) findViewById(R.id.dis_plus_accrual);
        mLoadDisburseReports.setVisibility(View.INVISIBLE);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam", "");
        branchname = sp.getString("branchnam", "");

        DisbursementsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " disbursements").child(branchname);
        DisbursementsRef.keepSynced(true);

        DialogFragment datepicker = new DatedPickerFragment();
        list = new ArrayList<>();
        list1 = new ArrayList<>();




        //############################################################################################################
        mselectDate.setOnClickListener(new View.OnClickListener() {//choose and set the FROM date
            @Override
            public void onClick(View view) {
                datepicker.show(getSupportFragmentManager(), "datepicker");
                startDateOrEndDAte = true;

            }
        });

        mselectDate2.setOnClickListener(new View.OnClickListener() {//choose and set the date
            @Override
            public void onClick(View view) {

                datepicker.show(getSupportFragmentManager(), "datepicker");
                startDateOrEndDAte = false;
            }
        });


        DisburseeListAdapter adapter = new DisburseeListAdapter(this,R.layout.disbursements_report_layout,list);

        mLoadDisburseReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadDisburseReports.setVisibility(View.INVISIBLE);
                mLoadingBar.setTitle("Disbursements Report");
                mLoadingBar.setMessage("...fetching data.");
                mLoadingBar.setCanceledOnTouchOutside(true);
                mLoadingBar.show();
                try { startdate = simpleDateFormat.parse(startstring);
                    enddate = simpleDateFormat.parse(endstring);
                    start.setTime(startdate);
                    end.setTime(enddate);}
                catch (Exception e) { }

                disbursee = new Disbursee();
                DisbursementsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<String> dateskeys = new ArrayList<>();
                            List<String> nameskeys = new ArrayList<>();
                            List<Disbursee> wholelist = new ArrayList<>();
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

                                            Disbursee disbursee = nameskeynode.getValue(Disbursee.class);
                                            wholelist.add(disbursee);
                                        }
                                    }
                                }
                            }


                            list.clear();
                            double disbursed1=0;
                            double disbursed1accrual=0;
                            for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()){

                                 /*starting with the start date get all objects from the wholelist ( for(int i=0;i<wholelist.size();i++))and
                                    check which one item has its date value equal to date and add that item to list1, add 1 day to date and repeat
                                    the above now using date+1 in place of date....do this while date value is less than end date*/

                                for(int i=0;i<wholelist.size();i++){
                                    Disbursee disbursee1 = wholelist.get(i);
                                    String datestringfrmwholelist = disbursee1.getDisbursementdate();
                                    try{ datefromwholelist= simpleDateFormat.parse(datestringfrmwholelist);
                                        calendar.setTime(datefromwholelist);
                                    }

                                    catch (Exception e){ }


                                    if (date.equals(datefromwholelist)){
                                        list.add(disbursee1);


                                    }


                                }
                            }
                            //add total disbursements and set the total as text in total amount distbursed
                            // and total amount disbursed+ interest fields
                            for(int j=0;j<list.size();j++){
                                Disbursee disbursee2 = list.get(j);
                                String total = disbursee2.getAmount();
                                String totalaccrual = disbursee2.getAmountdue();
                                double disbursed = Double.parseDouble(String.valueOf(total));
                                double disbursedaccrual = Double.parseDouble(String.valueOf(totalaccrual));
                                disbursed1+=disbursed;
                                disbursed1accrual+=disbursedaccrual;
                                mTotalDisbursed.setText("$"+disbursed1+"");
                                mTotalplusAccrual.setText("$"+disbursed1accrual+"");
                            }

                            mDisburseReportList.setAdapter(adapter);
                            mLoadingBar.hide();
                            if(list.isEmpty()){
                                AlertDialog.Builder builder = new AlertDialog.Builder(DisbursementsReports.this);
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
            mDisburseReportsDate1.setText(currentDateString);
            mTotalDisbursed.setText("0.00");
            startstring = mDisburseReportsDate1.getText().toString();
            mLoadDisburseReports.setVisibility(View.VISIBLE);}

        //set end date
        else {
            mDisburseReportsDate2.setText(currentDateString);
            mTotalDisbursed.setText("0.00");
            endstring = mDisburseReportsDate2.getText().toString();
            mLoadDisburseReports.setVisibility(View.VISIBLE);

        }

    }




}

































/* WHOLE CODE(INCLUDING IMPORTS) COPIED FOR BACKUP FROM HERE ONWARDS###########################################################

package com.tafrica.mopapov2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DisbursementsReports extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout mselectDate,mselectDate2;
    TextView mDisburseReportsDate1,mDisburseReportsDate2,mTotalDisbursed;
    Button mLoadDisburseReports;


    FirebaseListAdapter adapter;

    ListView mDisburseReportList;
    DatabaseReference DisbursementsRef;
    String companyname,branchname,balance;
    ArrayList<Double> Tot;
    Double holder = 0.0;
    private boolean startDateOrEndDAte = true;
    ArrayList<String> list;
    ArrayAdapter<String> adapterr;
    Disbursee disbursee;

    Date startdate, enddate;
    String startstring= "24-09-2020";
    String endstring="24-09-2020";
    Calendar start = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursements_reports);

        mselectDate = (LinearLayout) findViewById(R.id.select_date_linear_btn);
        mselectDate2 = (LinearLayout) findViewById(R.id.select_date2_linear_btn);
        mDisburseReportsDate1 = (TextView) findViewById(R.id.date_txtvw);
        mDisburseReportsDate2 = (TextView) findViewById(R.id.date2_txtvw);
        mLoadDisburseReports = (Button) findViewById(R.id.load_disburse_reports_btn);
        mDisburseReportList= (ListView) findViewById(R.id.disbursements_report_listvw);
        mTotalDisbursed = (TextView) findViewById(R.id.total_amnt_displaytxtvw);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam","");
        branchname = sp.getString("branchnam","");

        DialogFragment datepicker = new DatedPickerFragment();





        //############################################################################################################
        mselectDate.setOnClickListener(new View.OnClickListener() {//choose and set the FROM date
            @Override
            public void onClick(View view) {

                datepicker.show(getSupportFragmentManager(),"datepicker");
                startDateOrEndDAte = true;
            }
        });

        mselectDate2.setOnClickListener(new View.OnClickListener() {//choose and set the date
            @Override
            public void onClick(View view) {
                datepicker.show(getSupportFragmentManager(),"datepicker");
                startDateOrEndDAte = false;
            }
        });
        try{

        startdate = simpleDateFormat.parse(startstring);
        enddate = simpleDateFormat.parse(endstring);
            start.setTime(startdate);
            end.setTime(enddate);

            disbursee = new Disbursee();
            DisbursementsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                    .child(companyname+ " disbursements").child(branchname);
            list = new ArrayList<>();
            adapterr = new ArrayAdapter<String>(this,R.layout.disbursements_report_layout,R.id.client_name_lstvw_item,list);
            DisbursementsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds:dataSnapshot.child("24-09-2020").getChildren())
                    {
                        //String root = dataSnapshot.getKey();
                            //dataSnapshot.getChildren();
                           // dataSnapshot.child("26-09-2020").getChildren();
                        // for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()){
                        disbursee = ds.getValue(Disbursee.class);
                        list.add(disbursee.getDisbursementdate()+" "+disbursee.getClientname().toString()+" "+disbursee.getAmount());


                    }

                    mDisburseReportList.setAdapter(adapterr);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });}

        catch (Exception e){}








        startstring = mDisburseReportsDate1.getText().toString();
        endstring = mDisburseReportsDate2.getText().toString();







        //#################################################################################################################
       /* mLoadDisburseReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startdate = simpleDateFormat.parse(startstring);
                    enddate = simpleDateFormat.parse(endstring);
                    start.setTime(startdate);
                    end.setTime(enddate);
                } catch (Exception e) {

                }

                //for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                    //System.out.println(date.toString());

                    //String chosendate = mDisburseReportsDate1.getText().toString();
                    DisbursementsRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                            .child(companyname + " disbursements").child(branchname);
                    DisbursementsRef.keepSynced(true);
                    //String inter = date.toString();

                   /* DisbursementsRef.addValueEventListener(new ValueEventListener() {//Sumup total disbursement for footnote textview display
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            double sum = 0;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Map<String, Object> map = (Map<String, Object>) ds.child(inter).getValue();
                                Object total = map.get("amount");
                                try {
                                    double pValue = Double.parseDouble(String.valueOf(total));
                                    sum += pValue;
                                    mTotalDisbursed.setText(sum + "");
                                } catch (Exception e) {

                                }

                            }
                            return;

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/


//################################
//query database for listview populating
                   /* Query query = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                            .child(companyname + " disbursements").child(branchname).child(mDisburseReportsDate1.getText().toString());

                    FirebaseListOptions<Disbursee> options = new FirebaseListOptions.Builder<Disbursee>()
                            .setLayout(R.layout.disbursements_report_layout)//set layout to custom layout file for disbursements reports
                            .setLifecycleOwner(DisbursementsReports.this)
                            .setQuery(query,Disbursee.class)
                            .build();

                    adapter = new FirebaseListAdapter(options) {
                        @Override
                        protected void populateView(@NonNull View v, @NonNull Object model, int position) {

                            try {
                                TextView mDisbursedate = v.findViewById(R.id.disbursementdate_lstvw_item);
                                TextView mClientname = v.findViewById(R.id.client_name_lstvw_item);
                                TextView mDisburseAmount = v.findViewById(R.id.amount_lstvw_item);
                                TextView mAmountDue = v.findViewById(R.id.amount_with_interest_lstvw_item);


                                Disbursee disbursee = (Disbursee) model;
                                mDisbursedate.setText(disbursee.getDisbursementdate());
                                mClientname.setText(disbursee.getClientname());
                                mDisburseAmount.setText(disbursee.getAmount());
                                mAmountDue.setText(disbursee.getAmountdue());
                                //mDisbursementdate.setText(payer.getDisbursementdate().toString());

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Please select date", Toast.LENGTH_SHORT).show();

                            }

                        }
                    };
                    mDisburseReportList.setAdapter(adapter);
                }

            //}
        });*/

    //}







//#################################################################################################################
/*
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
        mDisburseReportsDate1.setText(currentDateString);
        mTotalDisbursed.setText("0.00");}

        //set end date
        else {
        mDisburseReportsDate2.setText(currentDateString);
        mTotalDisbursed.setText("0.00");

        }

        }

        }




         @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, i);
        cal.set(Calendar.MONTH, i1);
        cal.set(Calendar.DAY_OF_MONTH, i2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = simpleDateFormat.format(cal.getTime());
        //set start date
        if (startDateOrEndDAte) {
            mDisburseReportsDate1.setText(currentDateString);
            mTotalDisbursed.setText("0.00");
        }

        //set end date
        else {
            mDisburseReportsDate2.setText(currentDateString);
            mTotalDisbursed.setText("0.00");

        }
    }




        */