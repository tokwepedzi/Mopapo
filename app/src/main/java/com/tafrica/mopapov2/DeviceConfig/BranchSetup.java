package com.tafrica.mopapov2.DeviceConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.DeviceConfig.BranchSetupInterface.IFFirebaseLoadDoneBranchSetup;
import com.tafrica.mopapov2.DeviceConfig.BranchSetupModel.BranchSetupModelClass;
import com.tafrica.mopapov2.Login1;
import com.tafrica.mopapov2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class BranchSetup extends AppCompatActivity implements IFFirebaseLoadDoneBranchSetup {

    TextView mCompanyname,mIndustry,mBranchname;
    EditText mGooglesheetspostlink;
    Button mSavebranchsetup;
    SearchableSpinner mBranchsearchspinner;
    DatabaseReference mBranchesRef;
    IFFirebaseLoadDoneBranchSetup ifFirebaseLoadDoneBranchSetup;
    List<BranchSetupModelClass> branchies;

    SharedPreferences sharedPreferences;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_setup);

        LinearLayout linearLayout = findViewById(R.id.branch_setup_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();

        mSavebranchsetup = (Button) findViewById(R.id.saveBranchSetupbtn) ;
        mIndustry = (TextView) findViewById(R.id.industryTxtVw);
        mCompanyname = (TextView) findViewById(R.id.companyNameTxtVw);
        mBranchname = (TextView) findViewById(R.id.branchNameTxtVw);
        mGooglesheetspostlink = (EditText) findViewById(R.id.google_sheets_post);

        sharedPreferences = getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS",Context.MODE_PRIVATE);
        String industryname = sp.getString("industrynam","");
        String companyname = sp.getString("companynam","");
        mIndustry.setText(industryname);
        mCompanyname.setText(companyname);

        mBranchsearchspinner = (SearchableSpinner) findViewById(R.id.selectBranchSearchbleSpinner);
        //Init Db
        mBranchesRef = FirebaseDatabase.getInstance().getReference(companyname+ " branches");
        mBranchesRef.keepSynced(true);
        //Init interface
        ifFirebaseLoadDoneBranchSetup = this;
        //Get data
        mBranchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BranchSetupModelClass> branches = new ArrayList<>();
                for(DataSnapshot branchesSnapshot:dataSnapshot.getChildren())
                {
                    branches.add(branchesSnapshot.getValue(BranchSetupModelClass.class));
                }
               ifFirebaseLoadDoneBranchSetup.onFirebaseLoadSuccessBranchSetup(branches);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ifFirebaseLoadDoneBranchSetup.onFirebaseLoadFailedBranchSetup(databaseError.getMessage());

            }
        });



        mBranchsearchspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String branchname = mBranchsearchspinner.getSelectedItem().toString();
                mBranchname.setText(branchname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        mSavebranchsetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String branch = mBranchname.getText().toString();
                String postmanurl = mGooglesheetspostlink.getText().toString().trim();

                if(branch.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BranchSetup.this);
                    builder.setMessage("Please select a Branch name to proceed!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("branchnam",branch);
                editor.putString("sheetspostman",postmanurl);
                editor.commit();
                Toast.makeText(BranchSetup.this,"Branch details saved successfully",Toast.LENGTH_SHORT).show();
                openUserLogin();}
            }
        });



    }

    private void openUserLogin() {

        Intent intent = new Intent(this, Login1.class);
        startActivity(intent);
    }



    @Override
    public void onFirebaseLoadSuccessBranchSetup(List<BranchSetupModelClass> branchSetupModelClassList) {
        branchies = branchSetupModelClassList;
        //Get all names
        List<String> branch_name_list = new ArrayList<>();
        for(BranchSetupModelClass branch:branchSetupModelClassList)
            branch_name_list.add(branch.getBranchname());
        //Create adapter and set for spinner
        ArrayAdapter<String> branchsetupadapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,branch_name_list);
        mBranchsearchspinner.setAdapter(branchsetupadapter);

    }

    @Override
    public void onFirebaseLoadFailedBranchSetup(String message) {

    }


}
