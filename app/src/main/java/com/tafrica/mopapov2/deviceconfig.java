package com.tafrica.mopapov2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class deviceconfig extends AppCompatActivity  {
/*
    SearchableSpinner searchableSpinnerBranch;
    Button applyBranchButton,saveBranchBtn;
    TextView branchtextvw;
    String branch;
    public static final String TEXT= "text";
    public static final String SHARED_PREFS ="sharedPrefs";
    public String textpaste;

    DatabaseReference BranchesRef;
    IFFirebaseLoadDoneBranchSelect ifFirebaseLoadDoneBranchSelect;
    List<BranchSelector> branches;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceconfig);
        /*

        searchableSpinnerBranch = (SearchableSpinner) findViewById(R.id.branch_select_spinner);
        applyBranchButton = (Button) findViewById(R.id.apply_branch_btn);
        branchtextvw = (TextView) findViewById(R.id.saved_branch_textview);
        saveBranchBtn = (Button) findViewById(R.id.next_button);

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
                BranchSelector branchspecimen = new BranchSelector();
                branchspecimen.setBranchname(branch);
            }
        });
        
        saveBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBranchData();
            }
        });
        loadBranchData();
        updateViews();
    }

    public void saveBranchData() {
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
    public String getTextpaste(){
        return textpaste;
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
    public void onFirebaseLoadFailureBranch(String message) {*/
    }
}
