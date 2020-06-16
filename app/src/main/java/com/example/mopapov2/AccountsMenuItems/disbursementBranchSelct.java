package com.example.mopapov2.AccountsMenuItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mopapov2.BranchSelectorInterface.IFFirebaseLoadDoneBranchSelect;
import com.example.mopapov2.BranchSelectorModel.BranchSelector;
import com.example.mopapov2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class disbursementBranchSelct extends AppCompatActivity implements IFFirebaseLoadDoneBranchSelect  {

    SearchableSpinner searchableSpinnerBranch;
    Button applyBranchButton,NextBtn;
    TextView branchtextvw;
    String branch;
    public static final String TEXT= "text";
    public static final String SHARED_PREFS ="sharedPrefs";
    public String textpaste;

    DatabaseReference BranchesRef;
    IFFirebaseLoadDoneBranchSelect ifFirebaseLoadDoneBranchSelect;
    List<BranchSelector> branches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disbursement_branch_selct);

        searchableSpinnerBranch = (SearchableSpinner) findViewById(R.id.branch_select_spinner);
        applyBranchButton = (Button) findViewById(R.id.apply_branch_btn);
        branchtextvw = (TextView) findViewById(R.id.saved_branch_textview);
        NextBtn = (Button) findViewById(R.id.next_button);

        //Init Db
        BranchesRef = FirebaseDatabase.getInstance().getReference("Branches");
        //Init interface
        ifFirebaseLoadDoneBranchSelect = this;
        //Get data
        BranchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BranchSelector> branches = new ArrayList<>();
                for(DataSnapshot branchesSnapshot:dataSnapshot.getChildren())
                {
                    branches.add(branchesSnapshot.getValue(BranchSelector.class));

                }
                ifFirebaseLoadDoneBranchSelect.onFirebaseLoadSuccessBranch(branches);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ifFirebaseLoadDoneBranchSelect.onFirebaseLoadFailureBranch(databaseError.getMessage());

            }
        });
        applyBranchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                branch = searchableSpinnerBranch.getSelectedItem().toString().trim();
                branchtextvw.setText(branch);
               // BranchSelector branchspecimen = new BranchSelector();
                //branchspecimen.setBranchname(branch);
            }
        });

        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String branchname = branchtextvw.getText().toString();
                saveBranchData();
                Intent intent = new Intent(disbursementBranchSelct.this, disbursement.class);
                intent.putExtra("branchkey",branchname);//passing the selected name(eg Budiriro)of the branch to the next activity(disbursement)
                startActivity(intent);

            }
        });
        loadBranchData();
        updateViews();
    }



    public void saveBranchData() {//saves the activity so that it wont lose data when you clos the activity and open again
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT,branchtextvw.getText().toString());
        editor.apply();
        Toast.makeText(this,"Branch selection saved",Toast.LENGTH_LONG).show();
    }
    public void loadBranchData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        textpaste = sharedPreferences.getString(TEXT,"");
    }
    public void updateViews(){
        branchtextvw.setText(textpaste);

    }


    @Override
    public void onFirebaseLoadSuccessBranch(List<BranchSelector> branchSelectorList) {
        branches = branchSelectorList;
        //Get all branchnames
        List<String> branch_name_list = new ArrayList<>();
        for(BranchSelector branch:branchSelectorList)
            branch_name_list.add(branch.getBranchname());
        //Create adapter and set for spinner
        ArrayAdapter<String> adapterBranch = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,branch_name_list);
        searchableSpinnerBranch.setAdapter(adapterBranch);

    }

    @Override
    public void onFirebaseLoadFailureBranch(String message) {

    }
}