package com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.FullScreenImageActivity;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.ViewDetailedVessels;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataImageReport;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.DetailedVesselViewHolder;
import com.rakantao.pcg.lacostazamboanga.R;

public class DetailViewHistoryReportsActivity extends AppCompatActivity {

    private TextView tvDetailHistoryVesselName;
    private TextView tvDetailHistoryVesselType;
    private TextView tvDetailHistoryOrigin;
    private TextView tvDetailHistoryDestination;
    private TextView tvDetailHistoryETD;
    private TextView tvDetailHistoryETA;
    private TextView tvDetailHistoryCapacity;
    private TextView tvDetailHistoryCrew;
    private TextView tvDetailHistoryPersonA;
    private TextView tvDetailHistoryPersonB;
    private TextView tvDetailHistoryPersonC;
    private TextView tvDetailHistoryPersonD;
    private TextView tvDetailHistoryTime;
    private TextView tvDetailHistoryRemarks;
    private TextView tvDetailHistoryPassengers;
    private TextView tvDetailHistoryInfant;
    private TextView tvDetailHistoryChildren;
    private TextView tvDetailHistoryAdult;

    private TextView tvDetailHistoryCrewNumber;

    private DatabaseReference mDabatabaseHistory;
    private DatabaseReference mDatabaseHistoryImages;
    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    private String valueKey;
    private String key;
    private String vesselName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_history_reports);

        valueKey = getIntent().getStringExtra("valueKey");
        key = getIntent().getStringExtra("key");
        vesselName = getIntent().getStringExtra("vesselName");

        tvDetailHistoryVesselName = findViewById(R.id.TVDetailHistoryReportVesselName);
        tvDetailHistoryVesselType  = findViewById(R.id.TVDetailHistoryReportVesselType);
        tvDetailHistoryOrigin  = findViewById(R.id.TVDetailHistoryVesselOrigin);
        tvDetailHistoryDestination  = findViewById(R.id.TVDetailHistoryVesselDestination);
        tvDetailHistoryETD  = findViewById(R.id.TVDetailHistoryETD);
        tvDetailHistoryETA  = findViewById(R.id.TVDetailHistoryETA);
        tvDetailHistoryCapacity  = findViewById(R.id.TVDetailHistoryPassengerCapacity);
        tvDetailHistoryCrew  = findViewById(R.id.TVDetailHistoryNoOfCrews);
        tvDetailHistoryPersonA  = findViewById(R.id.tvDetailHistoryPersonA);
        tvDetailHistoryPersonB  = findViewById(R.id.tvDetailHistoryPersonB);
        tvDetailHistoryPersonC  = findViewById(R.id.tvDetailHistoryPersonC);
        tvDetailHistoryPersonD  = findViewById(R.id.tvDetailHistoryPersonD);
        tvDetailHistoryTime  = findViewById(R.id.TVDetailHistoryTimeStamp);
        tvDetailHistoryRemarks  = findViewById(R.id.tvDetailHistoryRemarks);
        tvDetailHistoryPassengers  = findViewById(R.id.tvDetailHIstoryTotalPassenger);
        tvDetailHistoryInfant  = findViewById(R.id.tvDetailHIstoryInfant);
        tvDetailHistoryChildren  = findViewById(R.id.tvDetailHIstorytvDetailHIstoryChildren);
        tvDetailHistoryAdult  = findViewById(R.id.tvDetailHIstoryAdult);
        tvDetailHistoryCrewNumber = findViewById(R.id.TVDetailHistoryCrew);

        mRecyclerView = findViewById(R.id.recyclerHistoryImages);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDabatabaseHistory = FirebaseDatabase.getInstance().getReference("HistoryReportRecords");

        mDatabaseHistoryImages = FirebaseDatabase.getInstance().getReference("AdminImagesReport").child(vesselName).child(valueKey);
        mDabatabaseHistory.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String bordingA = dataSnapshot.child("bordingA").getValue().toString();
                    String bordingB = dataSnapshot.child("bordingB").getValue().toString();
                    String bordingC = dataSnapshot.child("bordingC").getValue().toString();
                    String bordingD = dataSnapshot.child("bordingD").getValue().toString();
                    String inspectionRemarks = dataSnapshot.child("inspectionRemarks").getValue().toString();
                    String numberAdult = dataSnapshot.child("numberAdult").getValue().toString();
                    String numberChildren = dataSnapshot.child("numberChildren").getValue().toString();
                    String numberCrew = dataSnapshot.child("numberCrew").getValue().toString();
                    String numberInfant = dataSnapshot.child("numberInfant").getValue().toString();
                    String numberTotalPassenger = dataSnapshot.child("numberTotalPassenger").getValue().toString();
                    String timeUploaded = dataSnapshot.child("timeUploaded").getValue().toString();
                    String vesselName = dataSnapshot.child("vesselName").getValue().toString();

                    tvDetailHistoryVesselName.setText(vesselName);
                    tvDetailHistoryCrew.setText(numberCrew);
                    tvDetailHistoryPersonA.setText(bordingA);
                    tvDetailHistoryPersonB.setText(bordingB);
                    tvDetailHistoryPersonC.setText(bordingC);
                    tvDetailHistoryPersonD.setText(bordingD);
                    tvDetailHistoryTime.setText(timeUploaded);
                    tvDetailHistoryRemarks.setText("Remarks : " + inspectionRemarks);
                    tvDetailHistoryPassengers.setText(numberTotalPassenger);
                    tvDetailHistoryInfant.setText(numberInfant);
                    tvDetailHistoryChildren.setText(numberChildren);
                    tvDetailHistoryAdult.setText(numberAdult);



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter <DataImageReport, DetailedVesselViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataImageReport, DetailedVesselViewHolder>(

                DataImageReport.class,
                R.layout.detailedvessel_listrow,
                DetailedVesselViewHolder.class,
                mDatabaseHistoryImages
        ) {
            @Override
            protected void populateViewHolder(DetailedVesselViewHolder viewHolder, final DataImageReport model, int position) {
                viewHolder.vImage(model.getImageUrl(), getApplicationContext());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailViewHistoryReportsActivity.this, FullScreenImageActivity.class);
                        intent.putExtra("ImageURL", model.getImageUrl());
                        startActivity(intent);
                    }
                });

                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("VesselDetails").child(vesselName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final DataVesselSched dataVesselSched = dataSnapshot.getValue(DataVesselSched.class);
                        if (dataSnapshot.exists()){

                            tvDetailHistoryVesselName.setText(vesselName);
                            tvDetailHistoryVesselType.setText(dataVesselSched.getVesselType());
                            tvDetailHistoryOrigin.setText(dataVesselSched.getOrigin());
                            tvDetailHistoryDestination.setText(dataVesselSched.getDestination());
                            tvDetailHistoryETD.setText(dataVesselSched.getDepartureTime());
                            tvDetailHistoryETA.setText(dataVesselSched.getArrivalTime());


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mDatabase.child("Vessels").child(vesselName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            tvDetailHistoryCapacity.setText(dataSnapshot.child("VesselPassengerCapacity").getValue().toString());
                            tvDetailHistoryCrewNumber.setText(dataSnapshot.child("VesselNumberOfCrew").getValue().toString());

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
