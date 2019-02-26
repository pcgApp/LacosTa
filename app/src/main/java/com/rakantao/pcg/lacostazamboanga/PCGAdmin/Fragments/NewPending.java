package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Fragments;

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
import android.widget.Button;
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
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Activities.StationDashBoard;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPending extends Fragment {

    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private DatabaseReference changeStatus;
    private DatabaseReference FinalChangeStats;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth firebaseAuth;
    View view;
    String userID;
    public String Origin;
    Button btnViewDash;
    private DatabaseReference databaseReference99;


    public NewPending() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_pending, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        linearLayoutManager = new LinearLayoutManager(getContext());
        Recyclerview = view.findViewById(R.id.recyclerPending1);
        btnViewDash = view.findViewById(R.id.viewdash1);
        databaseReference99 = FirebaseDatabase.getInstance().getReference();

        btnViewDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), StationDashBoard.class));
            }
        });

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


        FirebaseRecyclerAdapter<DataVesselSched, PendingViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataVesselSched, PendingViewholder>(

                        DataVesselSched.class,
                        R.layout.pending_listrow,
                        PendingViewholder.class,
                        childRef

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

                        if (model.getToAppear().equals("Not Appear")){
                            viewHolder.cardViewPending.setVisibility(View.GONE);
                        }else {
                            viewHolder.cardViewPending.setVisibility(View.VISIBLE);
                        }


/*
                        databaseReference99.child("ReportAdmin").child(model.getVesselName()).child(model.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    viewHolder.vsei.setText(dataSnapshot.child("bordingA").getValue().toString());

                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/

                        final Handler handler = new Handler();
                        final int delay = 1000; //milliseconds
                        final long[] getMin = new long[1];

                        viewHolder.btnclear.setVisibility(View.GONE);

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

                                    viewHolder.runnabletime.setText(elapsedHours+ " Hr(s) : "+ elapsedMinutes+" Min(s)");
                                    changeStatus = FirebaseDatabase.getInstance().getReference();

                                    if (model.getVesselType().equals("Fast Craft") || (model.getVesselType().equals("Passenger Vessel") || model.getVesselType().equals("Passenger Cargo") || model.getVesselType().equals("Motor Banca"))){
                                        if (elapsedHours == 0 && elapsedMinutes <= 30){



                                            Calendar calendar = Calendar.getInstance();
                                            int day = calendar.get(Calendar.DAY_OF_WEEK);

                                            switch (day) {
                                                case Calendar.SUNDAY:
                                                    changeStatus = FirebaseDatabase.getInstance().getReference();
                                                    FinalChangeStats = changeStatus.child("VesselSchedule").child("Sunday").child("Pending");
                                                    break;
                                                case Calendar.MONDAY:
                                                    changeStatus = FirebaseDatabase.getInstance().getReference();
                                                    FinalChangeStats = changeStatus.child("VesselSchedule").child("Monday").child("Pending");
                                                    break;
                                                case Calendar.TUESDAY:
                                                    changeStatus = FirebaseDatabase.getInstance().getReference();
                                                    FinalChangeStats = changeStatus.child("VesselSchedule").child("Tuesday").child("Pending");
                                                    break;
                                                case Calendar.WEDNESDAY:
                                                    changeStatus = FirebaseDatabase.getInstance().getReference();
                                                    FinalChangeStats = changeStatus.child("VesselSchedule").child("Wednesday").child("Pending");
                                                    break;
                                                case Calendar.THURSDAY:
                                                    changeStatus = FirebaseDatabase.getInstance().getReference();
                                                    FinalChangeStats = changeStatus.child("VesselSchedule").child("Thursday").child("Pending");
                                                    break;
                                                case Calendar.FRIDAY:
                                                    changeStatus = FirebaseDatabase.getInstance().getReference();
                                                    FinalChangeStats = changeStatus.child("VesselSchedule").child("Friday").child("Pending");
                                                    break;
                                                case Calendar.SATURDAY:
                                                    changeStatus = FirebaseDatabase.getInstance().getReference();
                                                    FinalChangeStats = changeStatus.child("VesselSchedule").child("Saturday").child("Pending");
                                                    break;
                                            }

                                            FinalChangeStats.child(model.getKey()).child("ToAppear").setValue("Appear");

                                        }
                                    }else{
                                        Calendar calendar = Calendar.getInstance();
                                        int day = calendar.get(Calendar.DAY_OF_WEEK);

                                        switch (day) {
                                            case Calendar.SUNDAY:
                                                changeStatus = FirebaseDatabase.getInstance().getReference();
                                                FinalChangeStats = changeStatus.child("VesselSchedule").child("Sunday").child("Pending");
                                                break;
                                            case Calendar.MONDAY:
                                                changeStatus = FirebaseDatabase.getInstance().getReference();
                                                FinalChangeStats = changeStatus.child("VesselSchedule").child("Monday").child("Pending");
                                                break;
                                            case Calendar.TUESDAY:
                                                changeStatus = FirebaseDatabase.getInstance().getReference();
                                                FinalChangeStats = changeStatus.child("VesselSchedule").child("Tuesday").child("Pending");
                                                break;
                                            case Calendar.WEDNESDAY:
                                                changeStatus = FirebaseDatabase.getInstance().getReference();
                                                FinalChangeStats = changeStatus.child("VesselSchedule").child("Wednesday").child("Pending");
                                                break;
                                            case Calendar.THURSDAY:
                                                changeStatus = FirebaseDatabase.getInstance().getReference();
                                                FinalChangeStats = changeStatus.child("VesselSchedule").child("Thursday").child("Pending");
                                                break;
                                            case Calendar.FRIDAY:
                                                changeStatus = FirebaseDatabase.getInstance().getReference();
                                                FinalChangeStats = changeStatus.child("VesselSchedule").child("Friday").child("Pending");
                                                break;
                                            case Calendar.SATURDAY:
                                                changeStatus = FirebaseDatabase.getInstance().getReference();
                                                FinalChangeStats = changeStatus.child("VesselSchedule").child("Saturday").child("Pending");
                                                break;
                                        }

                                        FinalChangeStats.child(model.getKey()).child("ToAppear").setValue("Appear");

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



                        /*mUserDatabase = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(model.getVesselName());

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
                        });*/
                    }
                };
        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }
}
