package com.tafrica.mopapov2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class AgingReports extends AppCompatActivity {

    private static  String TAG = "AgingReports";

    CardView mCurrentbtn,mThirtyplusbtn,mSixtyplusbtn,mNinetyplusbtn;

    PieChart pieChart;
    String companyname,branchname;
    DatabaseReference DebtorsRef;
    TextView mCurrenttextvw,mThirtyplustextvw,mSixtyplustextvw,mNinetyplustextvw,mTotatalreceivable;
    //colors for different aging periods on piechart
    int [] colorClassArray = new int[]{Color.rgb(0,118,0),Color.rgb(193,205,50),Color.rgb(255,149,0),Color.rgb(255,50,50)};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aging_reports);
        Log.d(TAG, "onCreate: Setting layouts and creating piechart started");
        
        pieChart =(PieChart) findViewById(R.id.aging_pie_chart) ;
       mCurrentbtn = (CardView) findViewById(R.id.current_cdvw);
        mThirtyplusbtn = (CardView) findViewById(R.id.thirty_plus_cdvw);
        mSixtyplusbtn = (CardView) findViewById(R.id.sixty_plus_cdvw);
        mNinetyplusbtn = (CardView) findViewById(R.id.ninety_plus_cdvw);
        mCurrenttextvw = (TextView) findViewById(R.id.current_txtvw);
        mThirtyplustextvw =(TextView) findViewById(R.id.thirtyplus_txtvw);
        mSixtyplustextvw =(TextView) findViewById(R.id.sixtyplus_txtvw);
        mNinetyplustextvw =(TextView) findViewById(R.id.ninetyplus_txtvw);
        mTotatalreceivable = (TextView) findViewById(R.id.total_receivabletxtvw);


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
                float currentsum =0;
                float thirtyplususm = 0;
                float sixtyplussum =0;
                float ninetyplussum =0;
                float totalsum =0;
                try{
                Date date1;
                Date date2;
                Calendar calendar = Calendar.getInstance();
                String datetoday = DateFormat.getDateInstance().format(calendar.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                datetoday = simpleDateFormat.format(calendar.getTime());

                //setting dates
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
                        mCurrenttextvw.setText("$"+currentsum+"0");

                        //repeat for the other receivables' periods


                    }

                    if(differenceinDates>30 && differenceinDates<61){
                        Object thirtyplusTotal = map.get("amountdue");
                        float thirtyplusvalues = Float.parseFloat(String.valueOf(thirtyplusTotal));
                        thirtyplususm += thirtyplusvalues;
                        mThirtyplustextvw.setText("$"+thirtyplususm+"0");
                    }

                    if(differenceinDates>60 && differenceinDates<90){
                        Object sixtyplusTotal = map.get("amountdue");
                        float sixtyplusvalues = Float.parseFloat(String.valueOf(sixtyplusTotal));
                        sixtyplussum += sixtyplusvalues;
                        mSixtyplustextvw.setText("$"+sixtyplussum+"0");

                    }

                    if(differenceinDates>90){
                        Object ninetyplusTotal = map.get("amountdue");
                        float ninetyplusvalues = Float.parseFloat(String.valueOf(ninetyplusTotal));
                        ninetyplussum += ninetyplusvalues;
                        mNinetyplustextvw.setText("$"+ninetyplussum+"0");

                    }



                    totalsum = (currentsum+thirtyplususm+sixtyplussum+ninetyplussum);
                    mTotatalreceivable.setText("$"+totalsum+"0");
                    pieChart.setUsePercentValues(true);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setExtraOffsets(5,10,5,5);
                    pieChart.setDragDecelerationFrictionCoef(0.95f);
                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setHoleRadius(45f);
                    pieChart.setHoleColor(Color.WHITE);
                    pieChart.setTransparentCircleRadius(50f);
                    pieChart.setDrawEntryLabels(false);//hides thirty+ labels ...etc

                    ArrayList<PieEntry> yValues = new ArrayList<>();
                    yValues.add(new PieEntry(currentsum,"Current"));
                    yValues.add(new PieEntry(thirtyplususm,"Thirty+"));
                    yValues.add(new PieEntry(sixtyplussum,"Sixty+"));
                    yValues.add(new PieEntry(ninetyplussum,"Ninety+"));

                    pieChart.animateY(1000, Easing.EaseInOutCubic);

                    PieDataSet dataSet = new PieDataSet(yValues,"");
                    //Set piechart colors
                    dataSet.setColors(colorClassArray);
                    dataSet.setSliceSpace(2f);
                    dataSet.setSelectionShift(1f);
                   //dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(18f);
                    data.setValueTextColor(Color.BLACK);
                    data.setValueFormatter(new PercentFormatter(pieChart));
                    pieChart.setData(data);
                    pieChart.invalidate();



                }

                }

                catch (Exception e){}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






       /*ITERATION1*********************** pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(1);
        pieChart.setCenterText("AR Aging Report");
        pieChart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);*/

       // addDataSet();



        mCurrentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCurrentListView();
            }
        });
        mThirtyplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openThirtyPlusListView();
            }
        });
        mSixtyplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSixtyPlusListView();
            }
        });
        mNinetyplusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNinetyPlusListView();
            }
        });




    }

   /*ITERATION!*************************************** private void addDataSet() {
        Log.d(TAG, "addDataSet started ");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        for(int i=0;i<yData.length;i++){
            yEntrys.add(new PieEntry(yData[i],i));
        }

        for(int i=1;i<xData.length;i++){
            xEntrys.add(xData[i]);
        }

        //Create the dataset
        PieDataSet pieDataSet = new PieDataSet(yEntrys,"Employee Sales");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setValueTextSize(12);

        //Add colors to dataset
        //
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        pieDataSet.setColors(colors);

        //add legend to chart

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //legend.setPosition(Legend.Po)

        //create pieData object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();


    }*/

    private void openNinetyPlusListView() {
        Intent intent = new Intent(this, NinetyPluslistview.class);
        startActivity(intent);
    }

    private void openSixtyPlusListView() {
        Intent intent = new Intent(this, SixtyPluslistview.class);
        startActivity(intent);
    }

    private void openThirtyPlusListView() {
        Intent intent = new Intent(this, ThirtyPluslistview.class);
        startActivity(intent);
    }

    private void openCurrentListView() {
        Intent intent = new Intent(this, CurrentListview.class);
        startActivity(intent);
    }


}
