package com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataImageReport;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.DetailedVesselViewHolder;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailedReport extends AppCompatActivity {

    ImageView vImag1e;
    TextView vName,vType,vOrigin,vDestination,vETA,vETD,vInvestigator,vTimeStamp,vPassengerCap,vNoOfCrew,iReportPc,iReportNc,dReportDistressDesc,dReportDistressType,dReportRemarks;
    RecyclerView recyclerView;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase,databaseReferencenew;
    String getkey,getVesselname,getVesselKey;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_report);

        getkey = getIntent().getStringExtra("key");
        getVesselname = getIntent().getStringExtra("vesselName");
        getVesselKey = getIntent().getStringExtra("vesselKey");

        vImag1e = findViewById(R.id.DetailReportImage);

        vName = findViewById(R.id.TVDetailReportVesselName);
        vType = findViewById(R.id.TVDetailReportVesselType);
        vOrigin = findViewById(R.id.TVDetailReportVesselOrigin);
        vDestination = findViewById(R.id.TVDetailReportVesselDestination);
        vETA = findViewById(R.id.TVDetailReportETA);
        vETD = findViewById(R.id.TVDetailReportETD);
        vInvestigator = findViewById(R.id.TVDetailReportInspector);
        vTimeStamp = findViewById(R.id.TVDetailReportTimeStamp);
        vPassengerCap = findViewById(R.id.TVDetailReportPassengerCapacity);
        vNoOfCrew = findViewById(R.id.TVDetailReportNoOfCrews);
        iReportPc = findViewById(R.id.TVDetailReportInspectionPC);
        iReportNc = findViewById(R.id.TVDetailReportInspectionNC);
        dReportDistressDesc = findViewById(R.id.TVDetailReportDistressDescription);
        dReportDistressType = findViewById(R.id.TVDetailReportDistressType);
        dReportRemarks = findViewById(R.id.TVDetailReportDistressRemarks);


        recyclerView = findViewById(R.id.recyclerVesselDetailReport);

        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("AdminImagesReport");

        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataImageReport, DetailedVesselViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataImageReport, DetailedVesselViewHolder>(

                        DataImageReport.class,
                        R.layout.detailedvessel_listrow,
                        DetailedVesselViewHolder.class,
                        childRef.child(getVesselname).child(getVesselKey)

                ) {
                    @Override
                    protected void populateViewHolder(final DetailedVesselViewHolder viewHolder, final DataImageReport model, int position) {
                        viewHolder.vImage(model.getImageUrl(), getApplicationContext());

                        mUserDatabase = FirebaseDatabase.getInstance().getReference();

                        mUserDatabase.child("VesselDetails").child(getVesselname).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final DataVesselSched dataVesselSched = dataSnapshot.getValue(DataVesselSched.class);
                                if (dataSnapshot.exists()){

                                    vName.setText(dataVesselSched.getVesselName());
                                    vType.setText(dataVesselSched.getVesselType());
                                    vDestination.setText(dataVesselSched.getDestination());
                                    vETA.setText(dataVesselSched.getArrivalTime());
                                    vETD.setText(dataVesselSched.getDepartureTime());
                                    vOrigin.setText(dataVesselSched.getOrigin());


                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                    databaseReference.child("Vessels").child(dataVesselSched.getVesselName()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                vPassengerCap.setText(dataSnapshot.child("VesselPassengerCapacity").getValue().toString());
                                                vNoOfCrew.setText(dataSnapshot.child("VesselNumberOfCrew").getValue().toString());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    databaseReferencenew = FirebaseDatabase.getInstance().getReference();

                                    databaseReferencenew.child("ReportAdmin").child(dataVesselSched.getVesselName()).child(dataVesselSched.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){

                                                String boardingA = dataSnapshot.child("bordingA").getValue().toString();
                                                String boardingB = dataSnapshot.child("bordingB").getValue().toString();
                                                String boardingC = dataSnapshot.child("bordingC").getValue().toString();
                                                String boardingD = dataSnapshot.child("bordingD").getValue().toString();

                                                vInvestigator.setText("POIC : " + boardingA + "\n Members : \n" + boardingB + "\n" + boardingC + "\n" + boardingD);
                                                vTimeStamp.setText(dataSnapshot.child("timeUploaded").getValue().toString());
                                                iReportPc.setText(dataSnapshot.child("numberTotalPassenger").getValue().toString());


                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                                databaseReference.child("DistressReport")
                                                        .child(getkey)
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()){
                                                                    dReportRemarks.setText(dataSnapshot.child("DistressRemarks").getValue().toString());
                                                                    dReportDistressType.setText(dataSnapshot.child("DistressType").getValue().toString());
                                                                    dReportDistressDesc.setText(dataSnapshot.child("DistressDescription").getValue().toString());
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });



                                                DatabaseReference mUserDatabase1 = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(dataVesselSched.getVesselName());

                                                mUserDatabase1.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            final String image = dataSnapshot.child("image").getValue().toString();

                                                            if (!image.equals("default")){
                                                                Picasso.with(DetailedReport.this)
                                                                        .load(image)
                                                                        .fit().centerCrop()
                                                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                                                        .placeholder(R.drawable.zz)
                                                                        .into(vImag1e , new Callback() {
                                                                            @Override
                                                                            public void onSuccess() {

                                                                            }

                                                                            @Override
                                                                            public void onError() {
                                                                                Picasso.with(DetailedReport.this)
                                                                                        .load(image)
                                                                                        .placeholder(R.drawable.zz)
                                                                                        .into(vImag1e);
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
