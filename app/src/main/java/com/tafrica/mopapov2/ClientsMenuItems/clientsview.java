package com.tafrica.mopapov2.ClientsMenuItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tafrica.mopapov2.AccountsMenuItems.DebtorsAccountsModel.DebtorsAccountsInfo;
import com.tafrica.mopapov2.BaseActivity;
import com.tafrica.mopapov2.R;
import com.tafrica.mopapov2.ClientsMenuItems.clientsviewInterface.IFirebaseLoadDone;
import com.tafrica.mopapov2.ClientsMenuItems.Clientsviewodel.Clientie;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class clientsview extends BaseActivity implements IFirebaseLoadDone {

    SearchableSpinner searchableSpinner;

    DatabaseReference clientieRef, ClientDetailsref,ClientLoanDetailsref;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<Clientie> clienties;
    BottomSheetDialog bottomSheetDialog;

    //View

    TextView mClientname,mLoanamount,mClientaddress,mClientcell,mClientcollateral,mClientgroupname,
    mClientIDnum,mNxtofkin,mNxtofkinaddress,mNxtofkincell,mLoanamountdue,mDuedate,mIntialloanamount;//,clientaddress_
    boolean isFirstTimeClick=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientsview);

        //Init dialog
        //bottomSheetDialog = new BottomSheetDialog(this);
        //View bottom_sheet_dialog = getLayoutInflater().inflate(R.layout.list_layout_clients_view,null);
        //mClientname = (TextView) bottom_sheet_dialog.findViewById(R.id.clientname_titleTxtView);
        //clientaddress_ = (TextView) bottom_sheet_dialog.findViewById(R.id.clientaddress_titleTxtView);
        //mLoanamount = (TextView) bottom_sheet_dialog.findViewById(R.id.clientLoanamnt_titleTxtView) ;
        mClientname = (TextView) findViewById(R.id.client_name_txtvw) ;
        mClientaddress = (TextView) findViewById(R.id.address_txtvw);
        mClientcell = (TextView) findViewById(R.id.Cell_num_txtvw);
        mClientcollateral = (TextView) findViewById(R.id.collateral_textvw) ;
        mClientgroupname = (TextView) findViewById(R.id.grou_name_txtvw);
        mClientIDnum = (TextView) findViewById(R.id.ID_num_txtvw) ;
        mNxtofkin = (TextView) findViewById(R.id.next_of_kin_txtvw);
        mNxtofkincell = (TextView) findViewById(R.id.next_of_kin_cell_txtvw);
        mNxtofkinaddress = (TextView) findViewById(R.id.next_of_kin_address_txtvw);
        mLoanamountdue = (TextView) findViewById(R.id.amountDue_txtvw);
        mDuedate = (TextView) findViewById(R.id.due_date_txtvw);
        mIntialloanamount  = (TextView) findViewById(R.id.initial_loanamount_txtvw);

        //Get stored shared preferences
        SharedPreferences sp = getApplicationContext().getSharedPreferences("DEVICE_PREFS", Context.MODE_PRIVATE);
        String industryname = sp.getString("industrynam", "");
        String companyname = sp.getString("companynam", "");
        String branchname = sp.getString("branchnam", "");


        searchableSpinner = (SearchableSpinner) findViewById(R.id.searchable_spinner);
        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Fixed first time click
               // if(!isFirstTimeClick)
                //{
                String clientname = searchableSpinner.getSelectedItem().toString().trim();

                ClientDetailsref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
            .child(companyname+ " Clients details").child(branchname).child(clientname);
                ClientDetailsref.keepSynced(true);

                ClientDetailsref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Clientie clientdetails = dataSnapshot.getValue(Clientie.class);
                        mClientname.setText(clientname);
                        mClientIDnum.setText(clientdetails.getIdnum());
                        mClientcell.setText(clientdetails.getCellNum());
                        mClientaddress.setText(clientdetails.getAddress());
                        mClientgroupname.setText(clientdetails.getGroupName());
                        mNxtofkin.setText(clientdetails.getNxtKinName());
                        mNxtofkincell.setText(clientdetails.getNxtKinCell());
                        mNxtofkinaddress.setText(clientdetails.getNxtKinAddrss());
                        mClientcollateral.setText(clientdetails.getCollateral());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //ClientLoanDetailsref = FirebaseDatabase.getInstance().getReference(companyname+ " debtors accounts").child(branchname).child(clientname);
                ClientLoanDetailsref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " debtors accounts").child(branchname).child(clientname);
                ClientLoanDetailsref.keepSynced(true);
                ClientLoanDetailsref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DebtorsAccountsInfo debtordetails = dataSnapshot.getValue(DebtorsAccountsInfo.class);
                        mLoanamountdue.setText(debtordetails.getAmountdue());
                        mDuedate.setText(debtordetails.getDuedate());
                        mIntialloanamount.setText(debtordetails.getLoanamount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Init Db
        //clientieRef = FirebaseDatabase.getInstance().getReference(companyname+" Clients details").child(branchname);//reference works
        clientieRef = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid())
                .child(companyname+ " Clients details").child(branchname);
        //Init interafce
        iFirebaseLoadDone = this;
        //Get data
        clientieRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Clientie> clienties = new ArrayList<>();
                for(DataSnapshot clientieSnapShot:dataSnapshot.getChildren())
                {
                    clienties.add(clientieSnapShot.getValue(Clientie.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(clienties);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());

            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<Clientie> clientieList) {
        clienties = clientieList;
        //Get all name
        List<String> name_list = new ArrayList<>();
        for(Clientie clientie:clientieList)
            name_list.add(clientie.getName());
        //Create adapter and set for Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);
        searchableSpinner.setAdapter(adapter);

    }

    @Override
    public void onFirebaseLoadFailed(String message) {


    }
}
