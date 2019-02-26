package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.ArrivalDashBoardViewHolder;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.DeparturesDashBoardViewHolder;
import com.rakantao.pcg.lacostazamboanga.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SchedulesDashBoard extends Fragment {


    View view;
    RecyclerView depart;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;


    public SchedulesDashBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedules_dash_board, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext());


        depart = view.findViewById(R.id.RecyclerDepartures);



        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child("Sunday");


                break;
            case Calendar.MONDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Monday"));

                break;
            case Calendar.TUESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Tuesday"));

                break;
            case Calendar.WEDNESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Wednesday"));

                break;
            case Calendar.THURSDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Thursday"));


                break;
            case Calendar.FRIDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Friday"));

                break;
            case Calendar.SATURDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselsDashBoardAdmin").child(String.valueOf("Saturday"));

                break;
        }

        depart.setLayoutManager(linearLayoutManager);


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataVesselSched, DeparturesDashBoardViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataVesselSched, DeparturesDashBoardViewHolder>(

                        DataVesselSched.class,
                        R.layout.departed_dashboard_listrow,
                        DeparturesDashBoardViewHolder.class,
                        childRef

                ) {
                    @Override
                    protected void populateViewHolder(final DeparturesDashBoardViewHolder viewHolder, final DataVesselSched model, int position) {

                        viewHolder.tvdes.setText(model.getDestination());
                        viewHolder.tvorigin.setText(model.getOrigin());
                        viewHolder.tvTime.setText(model.getDepartureTime());
                        viewHolder.tvvesNme.setText(model.getVesselName());
                        viewHolder.tvRemarks.setText(model.getVesselStatus());

                        if (model.getVesselStatus().equals("Pending")){
                            viewHolder.cardViewAdmin.setVisibility(View.GONE);
                        }else {
                            viewHolder.cardViewAdmin.setVisibility(View.VISIBLE);
                        }


                        if (model.getDistressStatus().equals("Distress")){
                            viewHolder.tvRemarks.setText(model.getDistressStatus());
                        }else {
                            viewHolder.tvRemarks.setText(model.getVesselStatus());
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
                                                viewHolder.tvorigin.setTextColor(Color.BLUE);
                                                viewHolder.tvRemarks.setTextColor(Color.BLUE);

                                            }else {
                                                viewHolder.tvdes.setTextColor(Color.WHITE);
                                                viewHolder.tvTime.setTextColor(Color.WHITE);
                                                viewHolder.tvvesNme.setTextColor(Color.WHITE);
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
                                        viewHolder.tvorigin.setTextColor(Color.RED);
                                        viewHolder.tvRemarks.setTextColor(Color.RED);

                                    } else {

                                        viewHolder.tvdes.setTextColor(Color.WHITE);
                                        viewHolder.tvTime.setTextColor(Color.WHITE);
                                        viewHolder.tvvesNme.setTextColor(Color.WHITE);
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
    }
}
