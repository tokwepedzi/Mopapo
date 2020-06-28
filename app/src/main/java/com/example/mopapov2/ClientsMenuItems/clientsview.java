package com.example.mopapov2.ClientsMenuItems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mopapov2.R;
import com.example.mopapov2.ClientsMenuItems.clientsviewInterface.IFirebaseLoadDone;
import com.example.mopapov2.ClientsMenuItems.Clientsviewodel.Clientie;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class clientsview extends AppCompatActivity implements IFirebaseLoadDone {

    SearchableSpinner searchableSpinner;

    DatabaseReference clientieRef;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<Clientie> clienties;
    BottomSheetDialog bottomSheetDialog;

    //View

    TextView clientname_,loanamount_;//,clientaddress_
    boolean isFirstTimeClick=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientsview);

        //Init dialog
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottom_sheet_dialog = getLayoutInflater().inflate(R.layout.list_layout_clients_view,null);
        clientname_ = (TextView) bottom_sheet_dialog.findViewById(R.id.clientname_titleTxtView);
        //clientaddress_ = (TextView) bottom_sheet_dialog.findViewById(R.id.clientaddress_titleTxtView);
        loanamount_ = (TextView) bottom_sheet_dialog.findViewById(R.id.clientLoanamnt_titleTxtView) ;

        bottomSheetDialog.setContentView(bottom_sheet_dialog);

        searchableSpinner = (SearchableSpinner) findViewById(R.id.searchable_spinner);
        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Fixed first time click
                if(!isFirstTimeClick)
                {
                    Clientie clientie = clienties.get(i);
                    clientname_.setText(clientie.getName());
                    //clientaddress_.setText(clientie.getAddress());
                    loanamount_.setText(clientie.getLoanamount());

                    bottomSheetDialog.show();
                }
                else
                    isFirstTimeClick=false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Init Db
        clientieRef = FirebaseDatabase.getInstance().getReference("Impact Financial Services").child("Chinhoyi");//reference works

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


//name = name, description= address, image = loanamount
//movieList = clientieList;