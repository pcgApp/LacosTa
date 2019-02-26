package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselInfo;
import com.rakantao.pcg.lacostazamboanga.R;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterVessel extends AppCompatActivity {

    EditText VesselName, VesselType, VesselDesc, VesselCapacity, VesselCrew;
    Button btnNext, btnBackRegVessel;
    DatabaseReference databaseReference, getDatabaseReference;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vessel);

        getDatabaseReference = FirebaseDatabase.getInstance().getReference();

        VesselName = findViewById(R.id.ETVesselName);
        VesselType = findViewById(R.id.ETVesselType);
        VesselDesc = findViewById(R.id.ETVesselDesc);
        VesselCapacity = findViewById(R.id.ETEstimatedCapaticity);
        VesselCrew = findViewById(R.id.ETNumOfCrew);

        btnNext = findViewById(R.id.btnNext);
        btnBackRegVessel = findViewById(R.id.btnBackRegVessel);

        VesselType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items2 = {
                        "Fast Craft","Passenger Vessel", "Cargo Vessel","Tanker","Small Boat","Passenger Cargo", "Barge", "Motor Banca","Tramping"};

                AlertDialog.Builder builder2 = new AlertDialog.Builder(RegisterVessel.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        VesselType.setText(items2[item]);
                    }
                });
                AlertDialog alert2 = builder2.create();
                alert2.show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getVesselName = VesselName.getText().toString().trim();
                final String getVesselType = VesselType.getText().toString().trim();
                final String getVesselDesc = VesselDesc.getText().toString().trim();
                final String getNumberOfPassenger = VesselCapacity.getText().toString().trim();
                final String getNumberOfCrew = VesselCrew.getText().toString().trim();


                if (TextUtils.isEmpty(getVesselName) ||
                        TextUtils.isEmpty(getVesselType) ||
                        TextUtils.isEmpty(getVesselDesc)){

                    Toast.makeText(getApplicationContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();

                }else {

                    getDatabaseReference.child("Vessels").orderByChild("Vessel_Name").equalTo(getVesselName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                                Toast.makeText(getApplicationContext(), "This vessel already exist, please use another name.", Toast.LENGTH_SHORT).show();


                            }else {

                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                HashMap<String, String> HashString = new HashMap<String, String>();
                                HashString.put("Vessel_Name", getVesselName);
                                HashString.put("Vessel_Type", getVesselType);
                                HashString.put("Vessel_Desc", getVesselDesc);
                                HashString.put("VesselPassengerCapacity", getNumberOfPassenger);
                                HashString.put("VesselNumberOfCrew", getNumberOfCrew);


                                    databaseReference = firebaseDatabase.getReference("Vessels").child(getVesselName);
                                    databaseReference.setValue(HashString);

                                Intent myIntent = new Intent(RegisterVessel.this, SetVesselScheduleActivity.class);
                                myIntent.putExtra("vesselName", getVesselName);
                                myIntent.putExtra("vesselType", getVesselType);
                                myIntent.putExtra("passengerCapacity", getNumberOfPassenger);
                                myIntent.putExtra("numberOfCrew", getNumberOfCrew);

                                    startActivity(myIntent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        btnBackRegVessel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}