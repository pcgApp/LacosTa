package com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.ViewDetailedVessels;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.DepartedViewHolder;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Activities.DetailedReport;
import com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Activities.SubStationDashBoardActivity;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubStationArrivingFragment extends Fragment {

    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase,databaseReference;
    View view;
    private FirebaseAuth firebaseAuth;
    String userID;
    public String Origin;
    private String dayOfWeek;
    private String MyStation;
    Button btnDash;

    public SubStationArrivingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_station_arriving, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        linearLayoutManager = new LinearLayoutManager(getContext());
        Recyclerview = view.findViewById(R.id.recyclerArrivingSubStationAdmin);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child("Sunday").child("Departed");
                dayOfWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Monday")).child("Departed");
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Tuesday")).child("Departed");
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:

                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Wednesday")).child("Departed");
                dayOfWeek = "Wednesday";

                break;
            case Calendar.THURSDAY:

                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Thursday")).child("Departed");
                dayOfWeek = "Thursday";

                break;
            case Calendar.FRIDAY:

                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Friday")).child("Departed");
                dayOfWeek = "Friday";

                break;
            case Calendar.SATURDAY:

                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Saturday")).child("Departed");
                dayOfWeek = "Saturday";

                break;
        }

        Recyclerview.setLayoutManager(linearLayoutManager);

        btnDash = view.findViewById(R.id.SubStationviewdash);

        btnDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SubStationDashBoardActivity.class));
            }
        });

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
                if (dataSnapshot.exists()){

                    Origin = dataSnapshot.child("SubStation").getValue().toString();

                    FirebaseRecyclerAdapter<DataVesselSched, DepartedViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataVesselSched, DepartedViewHolder>(

                                    DataVesselSched.class,
                                    R.layout.departed_listrow,
                                    DepartedViewHolder.class,
                                    childRef.orderByChild("DestinationSubStation").equalTo(Origin)

                            ) {
                                @Override
                                protected void populateViewHolder(final DepartedViewHolder viewHolder, final DataVesselSched model, int position) {
                                    viewHolder.vesseltype.setText(model.getVesselType());
                                    viewHolder.vesselname.setText(model.getVesselName());
                                    viewHolder.vesselorigin.setText(model.getOrigin());
                                    viewHolder.vesseldesination.setText(model.getDestination());
                                    viewHolder.vesseldeparttime.setText(model.getDepartureTime());
                                    viewHolder.vesselarrivetime.setText(model.getArrivalTime());
                                    viewHolder.vesselschedday.setText(model.getScheduleDay());
                                    viewHolder.ATD.setText(model.getActualDepartedTime());

                                    DatabaseReference databaseReference99 = FirebaseDatabase.getInstance().getReference();

                                    databaseReference99.child("ReportAdmin").child(model.getVesselName()).child(model.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){

                                                viewHolder.tvvsei.setText(dataSnapshot.child("bordingA").getValue().toString());

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

                                    if (model.getDistressStatus().equals("Distress")){
                                        viewHolder.distressnotifieradmin.setVisibility(View.VISIBLE);
                                        viewHolder.distressnotifieradmin.setTextColor(Color.RED);
                                    }else {
                                        viewHolder.distressnotifieradmin.setVisibility(View.GONE);
                                    }


                                    final Handler handler = new Handler();
                                    final int delay = 1000; //milliseconds

                                    handler.postDelayed(new Runnable(){
                                        public void run(){

                                            if (viewHolder.distressnotifieradmin.getVisibility() == View.VISIBLE){
                                                if (model.getDistressStatus().equals("Distress")){
                                                    if (viewHolder.distressnotifieradmin.getCurrentTextColor() == Color.RED) {
                                                        viewHolder.distressnotifieradmin.setTextColor(Color.BLACK);
                                                        viewHolder.vesselname.setTextColor(Color.BLACK);
                                                    }else {
                                                        viewHolder.distressnotifieradmin.setTextColor(Color.RED);
                                                        viewHolder.vesselname.setTextColor(Color.RED);
                                                    }
                                                }
                                            }

                                            //do something
                                            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
                                            DateFormat df = new SimpleDateFormat("h:mm a");
                                            String date = df.format(Calendar.getInstance().getTime());
                                            String actualTime = viewHolder.ATD.getText().toString();
                                            Date time1;
                                            Date time2;

                                            try {
                                                time2 = format.parse(date);
                                                time1 = format.parse(actualTime);

                                                long diff = time2.getTime() - time1.getTime()  ;
                                                long secondsInMilli = 1000;
                                                long minutesInMilli = secondsInMilli * 60;
                                                long hoursInMilli = minutesInMilli * 60;

                                                long elapsedHours = diff / hoursInMilli;
                                                diff = diff % hoursInMilli;

                                                long elapsedMinutes = diff / minutesInMilli;

                                                viewHolder.vesselhourstravelled.setText(elapsedHours+ " Hr(s) : "+ elapsedMinutes+" Min(s)");


                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            handler.postDelayed(this, delay);
                                        }
                                    }, delay);



                                    viewHolder.btnDistress.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                                            LayoutInflater inflater = getActivity().getLayoutInflater();
                                            final View dialogView = inflater.inflate(R.layout.distress_layout, null);
                                            dialogBuilder.setView(dialogView);

                                            final EditText etdistresstype = dialogView.findViewById(R.id.ETDistressType);
                                            final EditText etDestription = dialogView.findViewById(R.id.ETDistressDescription);
                                            final EditText etRemarks = dialogView.findViewById(R.id.ETDistressRemarks);
                                            Button btnSendDistress = dialogView.findViewById(R.id.BTNSendDistress);

                                            final AlertDialog dialog = dialogBuilder.create();

                                            etdistresstype.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    final CharSequence[] items2 = {
                                                            "ENGINE TROUBLE",
                                                            "AGROUNDING",
                                                            "SINKING",
                                                            "CAPSIZING/ SUBMERGED",
                                                            "COLLISION",
                                                            "ALLISION/HITING/RAMMED",
                                                            "MAN OVERBOARD"	,
                                                            "FIRE ONBOARD",
                                                            "DEATH ONBOARD",
                                                            "STEERING CASUALTY",
                                                            "LISTING",
                                                            "DROWNING",
                                                            "PIRACY/ ARMED ROBBERY",
                                                            "ILLEGAL FISHING",
                                                            "CADAVER SIGHTINGS",
                                                            "MISSING",
                                                            "OTHERS"

                                                    };
                                                    android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(getActivity());
                                                    builder2.setTitle("Make your selection");
                                                    builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int item) {
                                                            // Do something with the selection
                                                            etdistresstype.setText(items2[item]);

                                                        }
                                                    });
                                                    android.app.AlertDialog alert2 = builder2.create();
                                                    alert2.show();
                                                }
                                            });

                                            btnSendDistress.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    final String getDistressType = etdistresstype.getText().toString();
                                                    final String getDistressDescription = etDestription.getText().toString();
                                                    String getDistressRemarks = etRemarks.getText().toString();

                                                    if (TextUtils.isEmpty(getDistressDescription) ||
                                                            TextUtils.isEmpty(getDistressType) ||
                                                            TextUtils.isEmpty(getDistressRemarks)){
                                                        Toast.makeText(getContext(), "Please, Don't leave any field blank.", Toast.LENGTH_SHORT).show();
                                                    }else {

                                                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                                        final String date = df.format(Calendar.getInstance().getTime());
                                                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DistressReport");

                                                        final String key = databaseReference.push().getKey();

                                                        final HashMap<String, String> HashString1 = new HashMap<String, String>();
                                                        HashString1.put("NotificationType", "Distress");
                                                        HashString1.put("DistressType", getDistressType);
                                                        HashString1.put("DistressDescription", getDistressDescription);
                                                        HashString1.put("DistressRemarks", getDistressRemarks);
                                                        HashString1.put("Key", key);
                                                        HashString1.put("OriginStation", model.getOriginStation());
                                                        HashString1.put("DestinationStation", model.getDestinationStation());
                                                        HashString1.put("NotifDate",date);
                                                        HashString1.put("NotifStatus", "unread");
                                                        HashString1.put("VesselName", model.getVesselName());

                                                        databaseReference.child(key)
                                                                .setValue(HashString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                                                    userID =  currentUser.getUid();

                                                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

                                                                    databaseReference1.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()){
                                                                                Origin = dataSnapshot.child("SubStation").getValue().toString();
                                                                                MyStation = dataSnapshot.child("MyStation").getValue().toString();


                                                                                if (MyStation.equals(model.getDestinationStation()) && MyStation.equals(model.getOriginStation())){
                                                                                    DatabaseReference datanotifAdmin = FirebaseDatabase.getInstance().getReference("AdminNotifHeader");

                                                                                    datanotifAdmin.child(key)
                                                                                            .setValue(HashString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()){

                                                                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("VesselsDashBoardAdmin");

                                                                                                databaseReference2.child(model.getScheduleDay())
                                                                                                        .child(model.getKey())
                                                                                                        .child("DistressStatus")
                                                                                                        .setValue("Distress");

                                                                                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("VesselSchedule");

                                                                                                databaseReference3.child(model.getScheduleDay())
                                                                                                        .child("Departed")
                                                                                                        .child(model.getKey())
                                                                                                        .child("DistressStatus")
                                                                                                        .setValue("Distress");
                                                                                                //////
                                                                                                Snackbar snackbar = Snackbar
                                                                                                        .make(getActivity().findViewById(R.id.myFramez), "Distress Sent.", Snackbar.LENGTH_LONG)
                                                                                                        .setAction("Review", new View.OnClickListener() {
                                                                                                            @Override
                                                                                                            public void onClick(View view) {
                                                                                                                Intent intent = new Intent(getContext(), DetailedReport.class);
                                                                                                                intent.putExtra("key", key);
                                                                                                                intent.putExtra("vesselName", model.getVesselName());
                                                                                                                startActivity(intent);
                                                                                                            }
                                                                                                        });

                                                                                                snackbar.show();
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                }else if (MyStation.equals(model.getDestinationStation())){
                                                                                    DatabaseReference datanotiforigin = FirebaseDatabase.getInstance().getReference("StationNotifHeader");

                                                                                    datanotiforigin.child(model.getOriginStation())
                                                                                            .child(key)
                                                                                            .setValue(HashString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()){
                                                                                                DatabaseReference datanotiforigin = FirebaseDatabase.getInstance().getReference("MyStationNotifHeader");

                                                                                                datanotiforigin.child(model.getOriginStation())
                                                                                                        .child(key)
                                                                                                        .setValue(HashString1);

                                                                                                DatabaseReference  dataNotifSubStat = FirebaseDatabase.getInstance().getReference("MySubStationNotifHeader");

                                                                                                dataNotifSubStat.child(model.getOriginSubStation())
                                                                                                        .child(key)
                                                                                                        .setValue(HashString1);

                                                                                                DatabaseReference dataNotifOtherSubStat = FirebaseDatabase.getInstance().getReference("SubStationNotifHeader");

                                                                                                dataNotifOtherSubStat.child(model.getOriginSubStation())
                                                                                                        .child(key)
                                                                                                        .setValue(HashString1);


                                                                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("VesselsDashBoardAdmin");

                                                                                                databaseReference2.child(model.getScheduleDay())
                                                                                                        .child(model.getKey())
                                                                                                        .child("DistressStatus")
                                                                                                        .setValue("Distress");

                                                                                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("VesselSchedule");

                                                                                                databaseReference3.child(model.getScheduleDay())
                                                                                                        .child("Departed")
                                                                                                        .child(model.getKey())
                                                                                                        .child("DistressStatus")
                                                                                                        .setValue("Distress");

                                                                                                DatabaseReference datanotifAdmin = FirebaseDatabase.getInstance().getReference("AdminNotifHeader");

                                                                                                datanotifAdmin.child(key)
                                                                                                        .setValue(HashString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()){

                                                                                                            Snackbar snackbar = Snackbar
                                                                                                                    .make(getActivity().findViewById(R.id.myFramez), "Distress Sent.", Snackbar.LENGTH_LONG)
                                                                                                                    .setAction("Review", new View.OnClickListener() {
                                                                                                                        @Override
                                                                                                                        public void onClick(View view) {
                                                                                                                            Intent intent = new Intent(getContext(), DetailedReport.class);
                                                                                                                            intent.putExtra("key", key);
                                                                                                                            intent.putExtra("vesselName", model.getVesselName());
                                                                                                                            startActivity(intent);
                                                                                                                        }
                                                                                                                    });

                                                                                                            snackbar.show();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }else {
                                                                                    DatabaseReference datanotifdestination = FirebaseDatabase.getInstance().getReference("StationNotifHeader");

                                                                                    datanotifdestination.child(model.getDestinationStation())
                                                                                            .child(key)
                                                                                            .setValue(HashString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()){

                                                                                                DatabaseReference datanotiforigin = FirebaseDatabase.getInstance().getReference("MyStationNotifHeader");

                                                                                                datanotiforigin.child(model.getOriginStation())
                                                                                                        .child(key)
                                                                                                        .setValue(HashString1);

                                                                                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("VesselSchedule");

                                                                                                databaseReference3.child(model.getScheduleDay())
                                                                                                        .child("Departed")
                                                                                                        .child(model.getKey())
                                                                                                        .child("DistressStatus")
                                                                                                        .setValue("Distress");

                                                                                                DatabaseReference  dataNotifSubStat = FirebaseDatabase.getInstance().getReference("MySubStationNotifHeader");

                                                                                                dataNotifSubStat.child(model.getOriginSubStation())
                                                                                                        .child(key)
                                                                                                        .setValue(HashString1);

                                                                                                DatabaseReference dataNotifOtherSubStat = FirebaseDatabase.getInstance().getReference("SubStationNotifHeader");

                                                                                                dataNotifOtherSubStat.child(model.getOriginSubStation())
                                                                                                        .child(key)
                                                                                                        .setValue(HashString1);



                                                                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("VesselsDashBoardAdmin");

                                                                                                databaseReference2.child(model.getScheduleDay())
                                                                                                        .child(model.getKey())
                                                                                                        .child("DistressStatus")
                                                                                                        .setValue("Distress");

                                                                                                DatabaseReference datanotifAdmin = FirebaseDatabase.getInstance().getReference("AdminNotifHeader");
                                                                                                datanotifAdmin.child(key)
                                                                                                        .setValue(HashString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                        if (task.isSuccessful()){
                                                                                                            Snackbar snackbar = Snackbar
                                                                                                                    .make(getActivity().findViewById(R.id.myFramez), "Distress Sent.", Snackbar.LENGTH_LONG)
                                                                                                                    .setAction("Review", new View.OnClickListener() {
                                                                                                                        @Override
                                                                                                                        public void onClick(View view) {
                                                                                                                            Intent intent = new Intent(getContext(), DetailedReport.class);
                                                                                                                            intent.putExtra("key", key);
                                                                                                                            intent.putExtra("vesselName", model.getVesselName());
                                                                                                                            startActivity(intent);
                                                                                                                        }
                                                                                                                    });

                                                                                                            snackbar.show();
                                                                                                        }
                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    DatabaseReference dailyDistressRef = FirebaseDatabase.getInstance().getReference().child("DistressDailyReport");
                                                                    dailyDistressRef.child(dayOfWeek).child(key).setValue(HashString1);

                                                                    dialog.dismiss();
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            });


                                            dialog.show();
                                        }
                                    });




                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(getContext(), ViewDetailedVessels.class);
                                            intent.putExtra("vesselName", model.getVesselName());
                                            intent.putExtra("Key", model.getKey());
                                            startActivity(intent);
                                        }
                                    });

                                    viewHolder.btnarrive.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DateFormat df = new SimpleDateFormat("h:mm a");
                                            String date = df.format(Calendar.getInstance().getTime());

                                            Calendar calendar1 = Calendar.getInstance();
                                            String weekday_name = null;
                                            int day1 = calendar1.get(Calendar.DAY_OF_WEEK);

                                            switch (day1) {

                                                case Calendar.SUNDAY:
                                                    weekday_name = "Sunday";
                                                    break;
                                                case Calendar.MONDAY:
                                                    weekday_name = "Monday";
                                                    break;
                                                case Calendar.TUESDAY:
                                                    weekday_name = "Tuesday";
                                                    break;
                                                case Calendar.WEDNESDAY:
                                                    weekday_name = "Wednesday";
                                                    break;
                                                case Calendar.THURSDAY:
                                                    weekday_name = "Thursday";
                                                    break;
                                                case Calendar.FRIDAY:
                                                    weekday_name = "Friday";
                                                    break;
                                                case Calendar.SATURDAY:
                                                    weekday_name = "Saturday";
                                                    break;
                                            }


                                            if (!model.getScheduleDay().equals(weekday_name)){

                                                databaseReference = FirebaseDatabase.getInstance()
                                                        .getReference("VesselDetails")
                                                        .child((String) viewHolder.vesselname.getText())
                                                        .child("VesselStatus");
                                                databaseReference.setValue("Arrived");

                                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselDetails")
                                                        .child((String) viewHolder.vesselname.getText())
                                                        .child("TravelledTime");
                                                databaseReference1.setValue(viewHolder.vesselhourstravelled.getText());

                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselDetails")
                                                        .child((String) viewHolder.vesselname.getText())
                                                        .child("ActualTimeArrived");
                                                databaseReference2.setValue(date);


                                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(weekday_name)
                                                        .child("Departed")
                                                        .child(model.getKey())
                                                        .child("VesselStatus");
                                                databaseReference3.setValue("Arrived");

                                                DatabaseReference databaseReference4 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(weekday_name)
                                                        .child("Departed")
                                                        .child(model.getKey())
                                                        .child("TravelledTime");
                                                databaseReference4.setValue(viewHolder.vesselhourstravelled.getText());

                                                DatabaseReference databaseReference5 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(weekday_name)
                                                        .child("Departed")
                                                        .child(model.getKey())
                                                        .child("ActualTimeArrived");
                                                databaseReference5.setValue(date);

                                                DatabaseReference setArrivedDashboard = FirebaseDatabase.getInstance()
                                                        .getReference("VesselsDashBoardAdmin")
                                                        .child(weekday_name)
                                                        .child(model.getKey())
                                                        .child("VesselStatus");
                                                setArrivedDashboard.setValue("Arrived");

                                                DatabaseReference setDistressStatus = FirebaseDatabase.getInstance()
                                                        .getReference("VesselsDashBoardAdmin")
                                                        .child(weekday_name)
                                                        .child(model.getKey())
                                                        .child("DistressStatus");
                                                setDistressStatus.setValue("None");

                                                DatabaseReference changeDistressStatus = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(weekday_name)
                                                        .child("Departed")
                                                        .child(model.getKey())
                                                        .child("DistressStatus");

                                                changeDistressStatus.setValue("None");


                                                //Schedule
                                                //Move Query
                                                DatabaseReference From = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(weekday_name)
                                                        .child("Departed")
                                                        .child(model.getKey());

                                                DatabaseReference To = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(weekday_name)
                                                        .child("Arrived")
                                                        .child(model.getKey());

                                                moveFirebaseRecord1(From ,To);
                                                //Move Query

                                            }else {

                                                //Details
                                                databaseReference = FirebaseDatabase.getInstance()
                                                        .getReference("VesselDetails")
                                                        .child((String) viewHolder.vesselname.getText())
                                                        .child("VesselStatus");
                                                databaseReference.setValue("Arrived");

                                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselDetails")
                                                        .child((String) viewHolder.vesselname.getText())
                                                        .child("TravelledTime");
                                                databaseReference1.setValue(viewHolder.vesselhourstravelled.getText());

                                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselDetails")
                                                        .child((String) viewHolder.vesselname.getText())
                                                        .child("ActualTimeArrived");
                                                databaseReference2.setValue(date);
                                                //Details
                                                //Schedule
                                                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(model.getScheduleDay())
                                                        .child("Departed")
                                                        .child(model.getKey())
                                                        .child("VesselStatus");
                                                databaseReference3.setValue("Arrived");

                                                DatabaseReference databaseReference4 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(model.getScheduleDay())
                                                        .child("Departed")
                                                        .child(model.getKey())
                                                        .child("TravelledTime");
                                                databaseReference4.setValue(viewHolder.vesselhourstravelled.getText());

                                                DatabaseReference databaseReference5 = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(model.getScheduleDay())
                                                        .child("Departed")
                                                        .child(model.getKey())
                                                        .child("ActualTimeArrived");
                                                databaseReference5.setValue(date);

                                                DatabaseReference setArrivedDashboard = FirebaseDatabase.getInstance()
                                                        .getReference("VesselsDashBoardAdmin")
                                                        .child(model.getScheduleDay())
                                                        .child(model.getKey())
                                                        .child("VesselStatus");
                                                setArrivedDashboard.setValue("Arrived");

                                                DatabaseReference setDistressStatus = FirebaseDatabase.getInstance()
                                                        .getReference("VesselsDashBoardAdmin")
                                                        .child(model.getScheduleDay())
                                                        .child(model.getKey())
                                                        .child("DistressStatus");
                                                setDistressStatus.setValue("None");

                                                DatabaseReference changeDistressStatus = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(model.getScheduleDay())
                                                        .child("Departed")
                                                        .child(model.getKey())
                                                        .child("DistressStatus");

                                                changeDistressStatus.setValue("None");


                                                //Schedule
                                                //Move Query
                                                DatabaseReference From = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(model.getScheduleDay())
                                                        .child("Departed")
                                                        .child(model.getKey());

                                                DatabaseReference To = FirebaseDatabase.getInstance()
                                                        .getReference("VesselSchedule")
                                                        .child(model.getScheduleDay())
                                                        .child("Arrived")
                                                        .child(model.getKey());

                                                moveFirebaseRecord1(From ,To);
                                                //Move Query
                                            }

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
                                                            .into(viewHolder.imagevessel , new Callback() {
                                                                @Override
                                                                public void onSuccess() {

                                                                }

                                                                @Override
                                                                public void onError() {
                                                                    Picasso.with(getContext())
                                                                            .load(image)
                                                                            .placeholder(R.drawable.zz)
                                                                            .into(viewHolder.imagevessel);
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
