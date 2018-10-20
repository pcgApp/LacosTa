package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataImageReport;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.DetailedVesselViewHolder;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.PendingViewholder;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewDetailedVessels extends AppCompatActivity {

    ImageView vImag1e;
    TextView vName,vType,vOrigin,vDestination,vETA,vETD,vInvestigator,vTimeStamp;
    private TextView TVDetailedvTeam;
    RecyclerView recyclerView;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase,databaseReferencenew;
    String VesselName;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detailed_vessels);

        vImag1e = findViewById(R.id.TVDetailedvImage);

        vName = findViewById(R.id.TVDetailedvName);
        vType = findViewById(R.id.TVDetailedvType);
        vOrigin = findViewById(R.id.TVDetailedvOrigin);
        vDestination = findViewById(R.id.TVDetailedvDestination);
        vETA = findViewById(R.id.TVDetailedvETA);
        vETD = findViewById(R.id.TVDetailedvETD);
        vInvestigator = findViewById(R.id.TVDetailedvInspector);
        vTimeStamp = findViewById(R.id.TVDetailedvTimeStamp);

        TVDetailedvTeam = findViewById(R.id.TVDetailedvTeam);

        recyclerView = findViewById(R.id.recyclerVesselDetail);

        VesselName = this.getIntent().getStringExtra("vesselName");

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
                        childRef.child(VesselName)
                ) {
                    @Override
                    protected void populateViewHolder(final DetailedVesselViewHolder viewHolder, final DataImageReport model, int position) {
                        viewHolder.vImage(model.getImageUrl(), getApplicationContext());

                        mUserDatabase = FirebaseDatabase.getInstance().getReference();

                        mUserDatabase.child("VesselDetails").child(VesselName).addValueEventListener(new ValueEventListener() {
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

                                    databaseReferencenew = FirebaseDatabase.getInstance().getReference();

                                    databaseReferencenew.child("ReportAdmin").child(dataVesselSched.getVesselName()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){

                                                String boardingA = dataSnapshot.child("bordingA").getValue().toString();
                                                String boardingB = dataSnapshot.child("bordingB").getValue().toString();
                                                String boardingC = dataSnapshot.child("bordingC").getValue().toString();
                                                String boardingD = dataSnapshot.child("bordingD").getValue().toString();

                                                vInvestigator.setText(dataSnapshot.child("inspector").getValue().toString());
                                                vTimeStamp.setText(dataSnapshot.child("timeUploaded").getValue().toString());
                                                TVDetailedvTeam.setText("POIC : " + boardingA + "\n Members : \n" + boardingB + "\n" + boardingC + "\n" + boardingD);

                                                DatabaseReference mUserDatabase1 = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(dataVesselSched.getVesselName());

                                                mUserDatabase1.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            final String image = dataSnapshot.child("image").getValue().toString();

                                                            if (!image.equals("default")){
                                                                Picasso.with(ViewDetailedVessels.this)
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
                                                                                Picasso.with(ViewDetailedVessels.this)
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

                                }else {
                                    Toast.makeText(ViewDetailedVessels.this, "No Reports have been made yet.", Toast.LENGTH_SHORT).show();
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