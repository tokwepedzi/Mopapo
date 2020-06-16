package com.example.mopapov2.DeviceConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mopapov2.BranchSelectorInterface.IFFirebaseLoadDoneBranchSelect;
import com.example.mopapov2.BranchSelectorModel.BranchSelector;
import com.example.mopapov2.DeviceConfig.BranchSetupInterface.IFFirebaseLoadDoneBranchSetup;
import com.example.mopapov2.DeviceConfig.BranchSetupModel.BranchSetupModelClass;
import com.example.mopapov2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class BranchSetup extends AppCompatActivity implements IFFirebaseLoadDoneBranchSetup {

    TextView mCompany,mIndustry,mBranchname;
    SearchableSpinner mBranchsearchspinner;
    DatabaseReference mBranchesRef;
    IFFirebaseLoadDoneBranchSetup ifFirebaseLoadDoneBranchSetup;
    List<BranchSetupModelClass> branchies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_setup);

        //Get device setup details from the Setup actitivy
        Intent intent = getIntent();
        String industryname = intent.getStringExtra(WelcomenSetup.INDUSTRY_NAME);
        String companyname = intent.getStringExtra((WelcomenSetup.COMPANY_NAME));
        LinearLayout linearLayout = findViewById(R.id.branch_setup_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();

        mIndustry = (TextView) findViewById(R.id.industryTxtVw);
        mCompany = (TextView) findViewById(R.id.companyNameTxtVw);
        mBranchname = (TextView) findViewById(R.id.branchNameTxtVw);
        mIndustry.setText(industryname);
        mCompany.setText(companyname);

        mBranchsearchspinner = (SearchableSpinner) findViewById(R.id.selectBranchSearchbleSpinner);
        //Init Db
        mBranchesRef = FirebaseDatabase.getInstance().getReference(companyname+" branches");
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
