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
import com.tafrica.mopapov2.AccountsMenuItems.CashupModel.CahsupModelclass;
import com.tafrica.mopapov2.CashupModelClassListAdpater;
import com.tafrica.mopapov2.DatedPickerFragment;
import com.tafrica.mopapov2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Cashup_report extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    LinearLayout mselectDate, mselectDate2;
    TextView mCashupReportsDate1, mCashupReportsDate2, mTotalCashCollected,mTotalCashSubmitted,mTotalShortfalls;
    Button mLoadCashupReports;
    ListView mCashupReportList;
    DatabaseReference CashupRef;
    String companyname, branchname;
    private boolean startDateOrEndDAte = true;
    private ProgressDialog mLoadingBar;
    ArrayList<CahsupModelclass> list,list1;
    CahsupModelclass cahsupModelclass;
    Date startdate, enddate;
    String startstring ;
    String endstring ;
    Calendar start = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashup_report);

        mselectDate = (LinearLayout) findViewById(R.id.select_date_linear_btn);
        mselectDate2 = (LinearLayout) findViewById(R.id.select_date2_linear_btn);
        mCashupReportsDate1 = (TextView) findViewById(R.id.date_txtvw);
        mCashupReportsDate2 = (TextView) findViewById(R.id.date2_txtvw);
        mLoadingBar = new ProgressDialog(Cashup_report.this);
        mLoadCashupReports = (Button) findViewById(R.id.load_cashup_reports_btn);
        mCashupReportList = (ListView) findViewById(R.id.cashup_report_listvw);
        mTotalCashCollected = (TextView) findViewById(R.id.total_cashamount_collected_displaytxtvw);
        mTotalCashSubmitted = (TextView) findViewById(R.id.total_cash_submitted_txtvw);
        mTotalShortfalls = (TextView) findViewById(R.id.shortfall_view);
        mLoadCashupReports.setVisibility(View.INVISIBLE);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        companyname = sp.getString("companynam", "");
        branchname = sp.getString("branchnam", "");

        CashupRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname + " cashups").child(branchname);
        CashupRef.keepSynced(true);

        DialogFragment datepicker = new DatedPickerFragment();
        list = new ArrayList<>();
        list1 = new ArrayList<>();

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


        CashupModelClassListAdpater adapter = new CashupModelClassListAdpater(this,R.layout.cashup_report_layouttemp,list);

        mLoadCashupReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadCashupReports.setVisibility(View.INVISIBLE);
                mLoadingBar.setTitle("Cashup Report");
                mLoadingBar.setMessage("...fetching data.");
                mLoadingBar.setCanceledOnTouchOutside(true);
                mLoadingBar.show();
                try { startdate = simpleDateFormat.parse(startstring);
                    enddate = simpleDateFormat.parse(endstring);
                    start.setTime(startdate);
                    end.setTime(enddate);}
                catch (Exception e) { }

                 cahsupModelclass = new CahsupModelclass();
                CashupRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> dateskeys = new ArrayList<>();
                        List<String> nameskeys = new ArrayList<>();
                        List<CahsupModelclass> wholelist = new ArrayList<>();
                        Date datefromwholelist= new Date();
                        Calendar calendar= Calendar.getInstance();
                        for(DataSnapshot dateskeynode:dataSnapshot.getChildren()){
                            dateskeys.clear();
                            dateskeys.add(dateskeynode.getKey());

                            for(int i=0;i<dateskeys.size();i++){

                                CahsupModelclass modelclass = dateskeynode.getValue(CahsupModelclass.class);
                                wholelist.add(modelclass);
                            }
                        }

                        list.clear();
                        double totalcollect =0;
                        double totalsubmmitt = 0;
                        double totalshortfalls =0;
                        for(Date date = start.getTime();start.before(end);start.add(Calendar.DATE,1),date = start.getTime()){

                             /*starting with the start date get all objects from the wholelist ( for(int i=0;i<wholelist.size();i++))and
                                    check which one item has its date value equal to date and add that item to list1, add 1 day to date and repeat
                                    the above now using date+1 in place of date....do this while date value is less than end date*/

                            for(int i=0;i<wholelist.size();i++){
                                CahsupModelclass modelclss = wholelist.get(i);
                                String datestringfrmwholelist = modelclss.getCashupdate();
                                try{ datefromwholelist= simpleDateFormat.parse(datestringfrmwholelist);
                                    calendar.setTime(datefromwholelist);
                                }

                                catch (Exception e){ }


                                if (date.equals(datefromwholelist)){
                                    list.add(modelclss);


                                }


                            }

                        }

                        //add total disbursements and set the total as text in total amount distbursed
                        // and total amount disbursed+ interest fields
                        for(int j=0;j<list.size();j++){
                            CahsupModelclass modelclass2 = list.get(j);
                            String total = modelclass2.getTotalcollection();
                            String totalsubmit = modelclass2.getCashinhand();
                            String totalshort = modelclass2.getShortfall();
                            double totacollectdob = Double.parseDouble(String.valueOf(total));
                            double totalsubmitdob = Double.parseDouble(String.valueOf(totalsubmit));
                            double totalshortfalldob = Double.parseDouble(String.valueOf(totalshort));
                            totalcollect+=totacollectdob;
                            totalsubmmitt+=totalsubmitdob;
                            totalshortfalls+=totalshortfalldob;
                            mTotalCashCollected.setText("$"+totalcollect+"");
                            mTotalCashSubmitted.setText("$"+totalsubmmitt+"");
                            mTotalShortfalls.setText("$"+totalshortfalls);
                        }

                        mCashupReportList.setAdapter(adapter);

                        mLoadingBar.hide();
                        if(list.isEmpty()){
                            try{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Cashup_report.this);
                            builder.setMessage("No records found for the selected period!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();}
                            catch (Exception e){}
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
            mCashupReportsDate1.setText(currentDateString);
            mTotalCashCollected.setText("0.00");
            mTotalCashSubmitted.setText("0.00");
            startstring = mCashupReportsDate1.getText().toString();
            mLoadCashupReports.setVisibility(View.VISIBLE);}

        //set end date
        else {
            mCashupReportsDate2.setText(currentDateString);
            mTotalCashCollected.setText("0.00");
            mTotalCashSubmitted.setText("0.00");
            endstring = mCashupReportsDate2.getText().toString();
            mLoadCashupReports.setVisibility(View.VISIBLE);

        }


    }
}
