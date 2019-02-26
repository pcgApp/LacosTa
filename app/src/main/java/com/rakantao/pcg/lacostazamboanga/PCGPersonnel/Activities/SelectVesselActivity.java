package com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.ViewDetailedVessels;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.PendingViewholder;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectVesselActivity extends AppCompatActivity {

    private RecyclerView Recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase,getPersonnalDatas;
    private DatabaseReference databaseDailyVessels;
    String getStation;
    String getSubStation;
    private String dayOfWeek;
    String UserID;
    private DatabaseReference checkReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vessel);


        linearLayoutManager = new LinearLayoutManager(this);
        Recyclerview = findViewById(R.id.recyclerSelectVessel);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserID = firebaseUser.getUid().toString();


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "Saturday";
                break;
        }

        getPersonnalDatas = FirebaseDatabase.getInstance().getReference();
        databaseDailyVessels = FirebaseDatabase.getInstance().getReference();
        databaseDailyVessels = getPersonnalDatas.child("VesselSchedule").child(dayOfWeek).child("Pending");




        Recyclerview.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference getPersonnelInfo = FirebaseDatabase.getInstance().getReference("Personnel");

        getPersonnelInfo.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    getStation = dataSnapshot.child("Station").getValue().toString();
                    getSubStation = dataSnapshot.child("SubStation").getValue().toString();



                    FirebaseRecyclerAdapter<DataVesselSched, PendingViewholder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataVesselSched, PendingViewholder>(


                                    DataVesselSched.class,
                                    R.layout.pending_listrow,
                                    PendingViewholder.class,
                                    databaseDailyVessels.orderByChild("OriginSubStation").equalTo("CGSS " + getSubStation)
                            ) {
                                @Override
                                protected void populateViewHolder(final PendingViewholder viewHolder, final DataVesselSched model, int position) {

                                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(model.getVesselName());

                                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                final String image = dataSnapshot.child("image").getValue().toString();

                                                if (!image.equals("default")){
                                                    Picasso.with(SelectVesselActivity.this)
                                                            .load(image)
                                                            .fit().centerCrop()
                                                            .networkPolicy(NetworkPolicy.OFFLINE)
                                                            .placeholder(R.drawable.zz)
                                                            .into(viewHolder.vesselimage , new Callback() {
                                                                @Override
                                                                public void onSuccess() {

                                                                }

                                                                @Override
                                                                public void onError() {
                                                                    Picasso.with(SelectVesselActivity.this)
                                                                            .load(image)
                                                                            .placeholder(R.drawable.zz)
                                                                            .into(viewHolder.vesselimage);
                                                                }
                                                            });
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    viewHolder.vesseltype.setText(model.getVesselType());
                                    viewHolder.vesselname.setText(model.getVesselName());
                                    viewHolder.origin.setText(model.getOrigin());
                                    viewHolder.destination.setText(model.getDestination());
                                    viewHolder.departime.setText(model.getDepartureTime());
                                    viewHolder.arrivaltime.setText(model.getArrivalTime());
                                    viewHolder.schedday.setText(model.getScheduleDay());


                                    viewHolder.btnclear.setVisibility(View.GONE);
                                    viewHolder.btnSelectVessel.setVisibility(View.VISIBLE);


                                    viewHolder.btnSelectVessel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            checkReport = FirebaseDatabase.getInstance().getReference();

                                            checkReport.child("AdminImagesReport").child(model.getVesselName()).child(model.getKey()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        Toast.makeText(SelectVesselActivity.this, "This Vessel had already been inspected.", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        String getVesselName = model.getVesselName();
                                                        String getVesselKey = model.getKey();
                                                        String getVesselType = model.getVesselType();
                                                        String getVesselDay = model.getScheduleDay();

                                                        Toast.makeText(SelectVesselActivity.this, "Vessel Selected", Toast.LENGTH_SHORT).show();

                                                        startActivity(new Intent(SelectVesselActivity.this, SendReportActivity.class)
                                                                .putExtra("VesselName", getVesselName)
                                                                .putExtra( "VesselKey", getVesselKey)
                                                                .putExtra( "VesselType", getVesselType)
                                                                .putExtra( "VesselDay", getVesselDay));
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });




                                        }
                                    });



                                    final Handler handler = new Handler();
                                    final int delay = 1000; //milliseconds

                                    viewHolder.btnclear.setVisibility(View.GONE);


                                    handler.postDelayed(new Runnable(){
                                        public void run(){
                                            //do something
                                            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                                            DateFormat df = new SimpleDateFormat("h:mm a");
                                            String date = df.format(Calendar.getInstance().getTime());
                                            String actualTime = viewHolder.departime.getText().toString();
                                            Date time1 = null;
                                            Date time2 = null;
                                            try {
                                                time1 = format.parse(date);
                                                time2 = format.parse(actualTime);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                            long diff = time2.getTime() - time1.getTime();
                                            long secondsInMilli = 1000;
                                            long minutesInMilli = secondsInMilli * 60;
                                            long hoursInMilli = minutesInMilli * 60;

                                            final long elapsedHours = diff / hoursInMilli;
                                            diff = diff % hoursInMilli;

                                            final long elapsedMinutes = diff / minutesInMilli;

                                            viewHolder.runnabletime.setText(elapsedHours+ " Hr(s) : "+ elapsedMinutes+" Min(s)");


                                            /*if (model.getVesselType().equals("Fast Craft")){
                                                if (elapsedHours == 0 && elapsedMinutes <= 30){
                                                    viewHolder.cardViewPending.setVisibility(View.VISIBLE);
                                                }else {
                                                    viewHolder.cardViewPending.setVisibility(View.GONE);
                                                }
                                            }else if (model.getVesselType().equals("Passenger Vessel") || model.getVesselType().equals("Passenger Cargo") || model.getVesselType().equals("Motor Banca")){
                                                if (elapsedHours == 0 && elapsedMinutes <= 30){
                                                    viewHolder.cardViewPending.setVisibility(View.VISIBLE);
                                                }else {
                                                    viewHolder.cardViewPending.setVisibility(View.GONE);
                                                }
                                            }else {
                                                viewHolder.cardViewPending.setVisibility(View.VISIBLE);
                                            }*/
                                            handler.postDelayed(this, delay);
                                        }
                                    }, delay);


                                }
                            };
                    Recyclerview.setAdapter(firebaseRecyclerAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
