package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.DashboardActivity;
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


public class ForDepartFragment extends Fragment {


    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase,databaseReference;
    View view;
    Button viewdash;

    public ForDepartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_for_depart, container, false);


        linearLayoutManager = new LinearLayoutManager(getContext());
        Recyclerview = view.findViewById(R.id.recyclerPending);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child("Sunday").child("Pending");
                break;
            case Calendar.MONDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child("Monday").child("Pending");
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

        viewdash = view.findViewById(R.id.viewdash);

        viewdash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DashboardActivity.class));
            }
        });


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
                        childRef.limitToLast(5)
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

                                if (model.getRemarks().equals("OnHold")){
                                    viewHolder.runnabletime.setText("On hold");
                                }else {
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


                                handler.postDelayed(this, delay);
                            }
                        }, delay);

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

                        viewHolder.btnclear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DateFormat df = new SimpleDateFormat("h:mm a");
                                String date = df.format(Calendar.getInstance().getTime());

                                databaseReference = FirebaseDatabase.getInstance()
                                        .getReference("VesselDetails")
                                        .child(model.getVesselName())
                                        .child("VesselStatus");
                                databaseReference.setValue("Departed");

                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                                        .getReference("VesselDetails")
                                        .child((String) viewHolder.vesselname.getText())
                                        .child("ActualDepartedTime");
                                databaseReference1.setValue(date);


                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                                        .getReference("VesselSchedule")
                                        .child(model.getScheduleDay())
                                        .child("Pending")
                                        .child(model.getKey())
                                        .child("VesselStatus");
                                databaseReference2.setValue("Departed");

                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                                        .getReference("VesselSchedule")
                                        .child(model.getScheduleDay())
                                        .child("Pending")
                                        .child(model.getKey())
                                        .child("ActualDepartedTime");
                                databaseReference3.setValue(date);

                                DatabaseReference databaseReference4 = FirebaseDatabase.getInstance()
                                        .getReference("VesselsDashBoardAdmin")
                                        .child(model.getScheduleDay())
                                        .child(model.getKey())
                                        .child("VesselStatus");
                                databaseReference4.setValue("Departed");

                                DatabaseReference From = FirebaseDatabase.getInstance()
                                        .getReference("VesselSchedule")
                                        .child(model.getScheduleDay())
                                        .child("Pending")
                                        .child(model.getKey());

                                DatabaseReference To = FirebaseDatabase.getInstance()
                                        .getReference("VesselSchedule")
                                        .child(model.getScheduleDay())
                                        .child("Departed")
                                        .child(model.getKey());


                                moveFirebaseRecord1(From ,To);

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
