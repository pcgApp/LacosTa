package com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SubStationPendingFragment extends Fragment {


    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth firebaseAuth;

    View view;
    String userID;
    public String Origin;

    public SubStationPendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_station_pending, container, false);


        firebaseAuth = FirebaseAuth.getInstance();

        linearLayoutManager = new LinearLayoutManager(getContext());
        Recyclerview = view.findViewById(R.id.recyclerPendingSubStationAdmin);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child("Sunday").child("Pending");
                break;
            case Calendar.MONDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Monday")).child("Pending");
                break;
            case Calendar.TUESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Tuesday")).child("Pending");
                break;
            case Calendar.WEDNESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Wednesday")).child("Pending");
                break;
            case Calendar.THURSDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Thursday")).child("Pending");
                break;
            case Calendar.FRIDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Friday")).child("Pending");
                break;
            case Calendar.SATURDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Saturday")).child("Pending");
                break;
        }

        Recyclerview.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID =  currentUser.getUid();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference1.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    Origin = dataSnapshot.child("SubStation").getValue().toString();

                    FirebaseRecyclerAdapter<DataVesselSched, PendingViewholder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataVesselSched, PendingViewholder>(

                                    DataVesselSched.class,
                                    R.layout.pending_listrow,
                                    PendingViewholder.class,
                                    childRef.orderByChild("OriginSubStation").equalTo(Origin).limitToLast(5)

                            ) {

                                @Override
                                protected void populateViewHolder(final PendingViewholder viewHolder, final DataVesselSched model, int position) {
                                    viewHolder.vesseltype.setText(model.getVesselType());
                                    viewHolder.vesselname.setText(model.getVesselName());
                                    viewHolder.origin.setText(model.getOrigin());
                                    viewHolder.destination.setText(model.getDestination());
                                    viewHolder.departime.setText(model.getDepartureTime());
                                    viewHolder.arrivaltime.setText(model.getArrivalTime());
                                    viewHolder.schedday.setText(model.getScheduleDay());

                                    viewHolder.btnclear.setVisibility(View.GONE);

                                    if (model.getToAppear().equals("Not Appear")){
                                        viewHolder.cardViewPending.setVisibility(View.GONE);
                                    }else {
                                        viewHolder.cardViewPending.setVisibility(View.VISIBLE);
                                    }

                                    DatabaseReference databaseReference99 = FirebaseDatabase.getInstance().getReference();

                                    databaseReference99.child("ReportAdmin").child(model.getVesselName()).child(model.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){

                                                viewHolder.vsei.setText(dataSnapshot.child("bordingA").getValue().toString());

                                                viewHolder.tvinfant.setText(dataSnapshot.child("numberInfant").getValue().toString());
                                                viewHolder.tvchildren.setText(dataSnapshot.child("numberChildren").getValue().toString());
                                                viewHolder.tvadults.setText(dataSnapshot.child("numberAdult").getValue().toString());
                                                viewHolder.tvcrew.setText(dataSnapshot.child("numberCrew").getValue().toString());
                                                viewHolder.totalpassenger.setText(dataSnapshot.child("numberTotalPassenger").getValue().toString());


                                                DatabaseReference fetchData = FirebaseDatabase.getInstance().getReference("Vessels");

                                                fetchData.child(model.getVesselName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String getTotal = viewHolder.totalpassenger.getText().toString();
                                                        viewHolder.totalpassenger.setText(getTotal+"/"+dataSnapshot.child("VesselPassengerCapacity").getValue().toString());


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

                                    final Handler handler = new Handler();
                                    final int delay = 1000; //milliseconds
                                    final long[] getMin = new long[1];

                                    handler.postDelayed(new Runnable(){
                                        public void run(){
                                            //do something
                                            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                                            DateFormat df = new SimpleDateFormat("h:mm a");
                                            String date = df.format(Calendar.getInstance().getTime());
                                            String actualTime = viewHolder.departime.getText().toString();
                                            Date time1;
                                            Date time2;

                                            try {
                                                time1 = format.parse(date);
                                                time2 = format.parse(actualTime);

                                                long diff = time2.getTime() - time1.getTime();
                                                long secondsInMilli = 1000;
                                                long minutesInMilli = secondsInMilli * 60;
                                                long hoursInMilli = minutesInMilli * 60;

                                                long elapsedHours = diff / hoursInMilli;
                                                diff = diff % hoursInMilli;

                                                long elapsedMinutes = diff / minutesInMilli;

                                                if (model.getRemarks().equals("OnHold")){
                                                    viewHolder.runnabletime.setText("On hold");
                                                    viewHolder.btnclear.setText("EMERGENCY CLEAR");
                                                }else {

                                                    viewHolder.btnclear.setText("CLEAR");
                                                    if (elapsedHours <= 0 && elapsedMinutes <=0){

                                                        DatabaseReference checkReport5 = FirebaseDatabase.getInstance().getReference();

                                                        checkReport5.child("AdminImagesReport").child(model.getVesselName()).child(model.getKey()).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()){

                                                                    viewHolder.runnabletime.setText("Waiting to Substation to Clear");

                                                                } else {

                                                                    viewHolder.runnabletime.setText("Vessel Not Yet Boarded");

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }else {
                                                        viewHolder.runnabletime.setText(elapsedHours+ " Hr(s) : "+ elapsedMinutes+" Min(s)");
                                                    }
                                                }
                                                getMin[0] = elapsedMinutes;
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            handler.postDelayed(this, delay);
                                        }
                                    }, delay);

                                    if (getMin[0] == 15){

                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(getContext());

                                        mBuilder.setSmallIcon(R.drawable.logo_pcg);
                                        mBuilder.setContentTitle("You've receive a notification");
                                        mBuilder.setContentText("The vessel "+ model.getVesselName() +" is leaving in less than 15 min");
                                        mBuilder.setPriority(Notification.PRIORITY_MAX);

                                        long[] vibrate = {0, 100, 200, 300};
                                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        mBuilder.setSound(alarmSound);
                                        mBuilder.setVibrate(vibrate);
                                        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                                        mNotificationManager.notify(001, mBuilder.build());

                                    }

                                    DatabaseReference checkReport1 = FirebaseDatabase.getInstance().getReference();

                                    checkReport1.child("AdminImagesReport").child(model.getVesselName()).child(model.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                viewHolder.btnclear.setVisibility(View.VISIBLE);

                                            } else {
                                                viewHolder.btnclear.setVisibility(View.GONE);                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DatabaseReference checkReport = FirebaseDatabase.getInstance().getReference();

                                            checkReport.child("AdminImagesReport").child(model.getVesselName()).child(model.getKey()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        viewHolder.btnclear.setVisibility(View.VISIBLE);

                                                    } else {
                                                        Toast.makeText(getContext(), "Vessel is still on scheduled for Inspection", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    });




                                    viewHolder.btnclear.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            DateFormat df = new SimpleDateFormat("h:mm a");
                                            String date = df.format(Calendar.getInstance().getTime());

                                            DatabaseReference SaveDeparted;

                                            SaveDeparted = FirebaseDatabase.getInstance()
                                                    .getReference("VesselDetails")
                                                    .child(model.getVesselName())
                                                    .child("VesselStatus");
                                            SaveDeparted.setValue("Departed");

                                            DatabaseReference saveActualTime = FirebaseDatabase.getInstance()
                                                    .getReference("VesselDetails")
                                                    .child((String) viewHolder.vesselname.getText())
                                                    .child("ActualDepartedTime");
                                            saveActualTime.setValue(date);

                                            DatabaseReference saveAnotherDeparted = FirebaseDatabase.getInstance()
                                                    .getReference("VesselSchedule")
                                                    .child(model.getScheduleDay())
                                                    .child("Pending")
                                                    .child(model.getKey())
                                                    .child("VesselStatus");
                                            saveAnotherDeparted.setValue("Departed");

                                            DatabaseReference SaveAnotherActualTime = FirebaseDatabase.getInstance()
                                                    .getReference("VesselSchedule")
                                                    .child(model.getScheduleDay())
                                                    .child("Pending")
                                                    .child(model.getKey())
                                                    .child("ActualDepartedTime");
                                            SaveAnotherActualTime.setValue(date);

                                            DatabaseReference SaveintoDashBoard = FirebaseDatabase.getInstance()
                                                    .getReference("VesselsDashBoardAdmin")
                                                    .child(model.getScheduleDay())
                                                    .child(model.getKey())
                                                    .child("VesselStatus");
                                            SaveintoDashBoard.setValue("Departed");

                                            //Move Queries

                                            DatabaseReference FromDB = FirebaseDatabase.getInstance()
                                                    .getReference("VesselSchedule")
                                                    .child(model.getScheduleDay())
                                                    .child("Pending")
                                                    .child(model.getKey());

                                            DatabaseReference ToDB = FirebaseDatabase.getInstance()
                                                    .getReference("VesselSchedule")
                                                    .child(model.getScheduleDay())
                                                    .child("Departed")
                                                    .child(model.getKey());

                                            //Move Queries
                                            moveFirebaseRecord1(FromDB ,ToDB);
                                        }
                                    });

                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                                            databaseReference.child("AdminImagesReport").child(model.getVesselName()).child(model.getKey()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        //Intent intent = new Intent(getContext(), DetailViewHistoryReportsActivity.class);
                                                        Intent intent = new Intent(getContext(), ViewDetailedVessels.class);
                                                        intent.putExtra("vesselName", model.getVesselName());
                                                        intent.putExtra("Key", model.getKey());
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(getContext(), "Vessel is still on scheduled for Inspection", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });


                                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(model.getVesselName());

                                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                final String image = dataSnapshot.child("image").getValue().toString();

                                                if (!image.equals("default")){
                                                    Picasso.with(getContext())
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
                                                                    Picasso.with(getContext())
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

    public void moveFirebaseRecord1(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            Toast.makeText(getContext(), "Copy failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            fromPath.removeValue();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(getContext(), "Copy failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
