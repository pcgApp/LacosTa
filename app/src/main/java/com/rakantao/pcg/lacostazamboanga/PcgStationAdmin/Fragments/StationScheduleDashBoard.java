package com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Fragments;


import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselSched;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.ViewHolders.StationArrivalsViewHolder;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.ViewHolders.StationDeparturesViewHolder;
import com.rakantao.pcg.lacostazamboanga.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

public class StationScheduleDashBoard extends Fragment {

    View view;
    RecyclerView depart;
    RecyclerView arrival;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef,childRef2;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager2;
    String userID;
    public String Origin;
    private FirebaseAuth firebaseAuth;

    public StationScheduleDashBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_station_schedule_dash_board, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager2 = new LinearLayoutManager(getContext());

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager2.setReverseLayout(true);

        depart = view.findViewById(R.id.recyclerStationDepart);
        arrival = view.findViewById(R.id.recyclerStationArrivals);


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child("Sunday");

                childRef2 = mDatabaseRef.child("VesselsDashBoardAdmin").child("Sunday");
                break;
            case Calendar.MONDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Monday"));

                childRef2 = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Monday"));
                break;
            case Calendar.TUESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Tuesday"));

                childRef2 = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Tuesday"));
                break;
            case Calendar.WEDNESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Wednesday"));

                childRef2 = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Wednesday"));
                break;
            case Calendar.THURSDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Thursday"));

                childRef2 = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Thursday"));
                break;
            case Calendar.FRIDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Friday"));

                childRef2 = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Friday"));
                break;
            case Calendar.SATURDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Saturday"));

                childRef2 = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Saturday"));
                break;
        }

        depart.setLayoutManager(linearLayoutManager);
        arrival.setLayoutManager(linearLayoutManager2);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID =  currentUser.getUid();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

        databaseReference1.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Origin = dataSnapshot.child("Station").getValue().toString();

                    FirebaseRecyclerAdapter<DataVesselSched, StationDeparturesViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataVesselSched, StationDeparturesViewHolder>(

                                    DataVesselSched.class,
                                    R.layout.station_schedule_dashboard_listrow,
                                    StationDeparturesViewHolder.class,
                                    childRef.orderByChild("OriginStation").equalTo(Origin)

                            ) {
                                @Override
                                protected void populateViewHolder(final StationDeparturesViewHolder viewHolder, final DataVesselSched model, int position) {

                                    viewHolder.tvorigin.setText(model.getOrigin());
                                    viewHolder.tvTime.setText(model.getDepartureTime());
                                    viewHolder.tvvesNme.setText(model.getVesselName());
                                    viewHolder.tvRemarks.setText(model.getVesselStatus());
                                    viewHolder.tvdes.setText(model.getDestination());
                                    viewHolder.tveta.setText(model.getArrivalTime());

                                    if (model.getVesselStatus().equals("Pending") && model.getVesselStatus().equals("Arrived")){
                                        viewHolder.cardviewDepart.setVisibility(View.GONE);
                                    }else {
                                        viewHolder.cardviewDepart.setVisibility(View.VISIBLE);
                                    }

                                    if (model.getDistressStatus().equals("Distress")){
                                        viewHolder.tvRemarks.setText(model.getDistressStatus());
                                    }else {
                                        if (model.getVesselStatus().equals("Departed")){
                                            viewHolder.tvRemarks.setText("Departed");
                                        }else {
                                            viewHolder.tvRemarks.setText(model.getVesselStatus());
                                        }
                                    }

                                    final Handler handler = new Handler();
                                    final int delay = 2000; //milliseconds

                                    handler.postDelayed(new Runnable(){
                                        public void run(){

                                            if (viewHolder.tvRemarks.getText().toString().equals("Departed")){
                                                SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                                                DateFormat df = new SimpleDateFormat("h:mm a");
                                                String date = df.format(Calendar.getInstance().getTime());
                                                Date time1;
                                                Date time2;
                                                String getETA = (model.getArrivalTime().toString());


                                                try {
                                                    time1 = format.parse(date);
                                                    time2 = format.parse(getETA);


                                                    long arrivaltime = time2.getTime();
                                                    long currenttime = time1.getTime();



                                                    if (currenttime > arrivaltime) {

                                                        if (viewHolder.tvdes.getCurrentTextColor() == Color.WHITE) {

                                                            viewHolder.tvdes.setTextColor(Color.BLUE);
                                                            viewHolder.tvTime.setTextColor(Color.BLUE);
                                                            viewHolder.tvvesNme.setTextColor(Color.BLUE);
                                                            viewHolder.tveta.setTextColor(Color.BLUE);
                                                            viewHolder.tvorigin.setTextColor(Color.BLUE);
                                                            viewHolder.tvRemarks.setTextColor(Color.BLUE);

                                                        }else {
                                                            viewHolder.tvdes.setTextColor(Color.WHITE);
                                                            viewHolder.tvTime.setTextColor(Color.WHITE);
                                                            viewHolder.tvvesNme.setTextColor(Color.WHITE);
                                                            viewHolder.tveta.setTextColor(Color.WHITE);
                                                            viewHolder.tvorigin.setTextColor(Color.WHITE);
                                                            viewHolder.tvRemarks.setTextColor(Color.WHITE);
                                                        }

                                                    }


                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                handler.postDelayed(this, delay);
                                            }else if (viewHolder.tvRemarks.getText().toString().equals("Distress")) {
                                                if (viewHolder.tvdes.getCurrentTextColor() == Color.WHITE) {

                                                    viewHolder.tvdes.setTextColor(Color.RED);
                                                    viewHolder.tvTime.setTextColor(Color.RED);
                                                    viewHolder.tvvesNme.setTextColor(Color.RED);
                                                    viewHolder.tveta.setTextColor(Color.RED);
                                                    viewHolder.tvorigin.setTextColor(Color.RED);
                                                    viewHolder.tvRemarks.setTextColor(Color.RED);

                                                } else {

                                                    viewHolder.tvdes.setTextColor(Color.WHITE);
                                                    viewHolder.tvTime.setTextColor(Color.WHITE);
                                                    viewHolder.tvvesNme.setTextColor(Color.WHITE);
                                                    viewHolder.tveta.setTextColor(Color.WHITE);
                                                    viewHolder.tvorigin.setTextColor(Color.WHITE);
                                                    viewHolder.tvRemarks.setTextColor(Color.WHITE);

                                                }

                                                handler.postDelayed(this, delay);
                                            }else {
                                                handler.removeCallbacks(this);
                                            }

                                        }
                                    }, delay);


                                }
                            };
                    depart.setAdapter(firebaseRecyclerAdapter);

                    FirebaseRecyclerAdapter<DataVesselSched, StationArrivalsViewHolder> firebaseRecyclerAdapter2 =
                            new FirebaseRecyclerAdapter<DataVesselSched, StationArrivalsViewHolder>(

                                    DataVesselSched.class,
                                    R.layout.station_arrival_dashboard_listrow,
                                    StationArrivalsViewHolder.class,
                                    childRef2.orderByChild("DestinationStation").equalTo(Origin)

                            ) {
                                @Override
                                protected void populateViewHolder(final StationArrivalsViewHolder viewHolder, final DataVesselSched model, int position) {

                                    viewHolder.tvdes.setText(model.getDestination());
                                    viewHolder.tvTime.setText(model.getArrivalTime());
                                    viewHolder.tvvesNme.setText(model.getVesselName());
                                    viewHolder.tvorigintime.setText(model.getDepartureTime());
                                    viewHolder.tvorigin.setText(model.getOrigin());

                                    if (model.getVesselStatus().equals("Pending") && model.getVesselStatus().equals("Arrived")){
                                        viewHolder.cardviewArrivals.setVisibility(View.GONE);
                                    }else {
                                        viewHolder.cardviewArrivals.setVisibility(View.VISIBLE);
                                    }

                                    if (model.getDistressStatus().equals("Distress")){
                                        viewHolder.tvRemarks.setText(model.getDistressStatus());
                                    }else {
                                        if (model.getVesselStatus().equals("Departed")){
                                            viewHolder.tvRemarks.setText("Arriving");
                                        }else {
                                            viewHolder.tvRemarks.setText(model.getVesselStatus());
                                        }
                                    }

                                    final Handler handler = new Handler();
                                    final int delay = 2000; //milliseconds

                                    handler.postDelayed(new Runnable(){
                                        public void run(){

                                            if (viewHolder.tvRemarks.getText().toString().equals("Arriving")){
                                                SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                                                DateFormat df = new SimpleDateFormat("h:mm a");
                                                String date = df.format(Calendar.getInstance().getTime());
                                                Date time1;
                                                Date time2;
                                                String getETA = (model.getArrivalTime().toString());


                                                try {
                                                    time1 = format.parse(date);
                                                    time2 = format.parse(getETA);


                                                    long arrivaltime = time2.getTime();
                                                    long currenttime = time1.getTime();



                                                    if (currenttime > arrivaltime) {

                                                        if (viewHolder.tvdes.getCurrentTextColor() == Color.WHITE) {

                                                            viewHolder.tvdes.setTextColor(Color.BLUE);
                                                            viewHolder.tvTime.setTextColor(Color.BLUE);
                                                            viewHolder.tvvesNme.setTextColor(Color.BLUE);
                                                            viewHolder.tvorigintime.setTextColor(Color.BLUE);
                                                            viewHolder.tvorigin.setTextColor(Color.BLUE);
                                                            viewHolder.tvRemarks.setTextColor(Color.BLUE);

                                                        }else {
                                                            viewHolder.tvdes.setTextColor(Color.WHITE);
                                                            viewHolder.tvTime.setTextColor(Color.WHITE);
                                                            viewHolder.tvvesNme.setTextColor(Color.WHITE);
                                                            viewHolder.tvorigintime.setTextColor(Color.WHITE);
                                                            viewHolder.tvorigin.setTextColor(Color.WHITE);
                                                            viewHolder.tvRemarks.setTextColor(Color.WHITE);
                                                        }

                                                    }


                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                handler.postDelayed(this, delay);
                                            }else if (viewHolder.tvRemarks.getText().toString().equals("Distress")) {
                                                if (viewHolder.tvdes.getCurrentTextColor() == Color.WHITE) {

                                                    viewHolder.tvdes.setTextColor(Color.RED);
                                                    viewHolder.tvTime.setTextColor(Color.RED);
                                                    viewHolder.tvvesNme.setTextColor(Color.RED);
                                                    viewHolder.tvorigintime.setTextColor(Color.RED);
                                                    viewHolder.tvorigin.setTextColor(Color.RED);
                                                    viewHolder.tvRemarks.setTextColor(Color.RED);

                                                } else {

                                                    viewHolder.tvdes.setTextColor(Color.WHITE);
                                                    viewHolder.tvTime.setTextColor(Color.WHITE);
                                                    viewHolder.tvvesNme.setTextColor(Color.WHITE);
                                                    viewHolder.tvorigintime.setTextColor(Color.WHITE);
                                                    viewHolder.tvorigin.setTextColor(Color.WHITE);
                                                    viewHolder.tvRemarks.setTextColor(Color.WHITE);

                                                }

                                                handler.postDelayed(this, delay);
                                            }else {
                                                handler.removeCallbacks(this);
                                            }


                                        }
                                    }, delay);
                                }
                            };
                    arrival.setAdapter(firebaseRecyclerAdapter2);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
