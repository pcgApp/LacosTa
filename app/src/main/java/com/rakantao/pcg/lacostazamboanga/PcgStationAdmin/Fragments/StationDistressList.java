package com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
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
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Activities.DetailedReport;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Datas.DataStationAdminNotif;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.ViewHolders.StationNotifViewHolder;
import com.rakantao.pcg.lacostazamboanga.R;

import org.joda.time.Interval;
import org.joda.time.Period;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class StationDistressList extends Fragment {

    View view;
    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    String userID;
    public String Origin;

    public StationDistressList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_station_distress_list, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        linearLayoutManager = new LinearLayoutManager(getContext());
        Recyclerview = view.findViewById(R.id.recyclerStationDistressList);
        Recyclerview.setLayoutManager(linearLayoutManager);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID = currentUser.getUid();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

        databaseReference1.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Origin = dataSnapshot.child("Station").getValue().toString();


                    mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                    childRef = mDatabaseRef.child("MyStationNotifHeader").child(Origin);


                    FirebaseRecyclerAdapter<DataStationAdminNotif, StationNotifViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataStationAdminNotif, StationNotifViewHolder>(

                                    DataStationAdminNotif.class,
                                    R.layout.notif_listrow,
                                    StationNotifViewHolder.class,
                                    childRef.orderByChild("NotificationType").equalTo("Distress")

                            )
                            {
                                @Override
                                protected void populateViewHolder(final StationNotifViewHolder viewHolder, final DataStationAdminNotif model, int position) {

                                    String boldText = model.getOriginStation();
                                    SpannableString str = new SpannableString(boldText);
                                    str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    viewHolder.stationname.setText(str);

                                    if (model.getNotificationType().equals("Distress")){

                                        String boldVessel = model.getVesselName();
                                        SpannableString strz = new SpannableString(boldVessel);
                                        strz.setSpan(new StyleSpan(Typeface.BOLD), 0, boldVessel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        String boldStatus = "Distress status";
                                        SpannableString strzq = new SpannableString(boldStatus);
                                        strzq.setSpan(new StyleSpan(Typeface.BOLD), 0, boldStatus.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                        viewHolder.notifdesc.setText("Had reported that " +  strz +" is in "+ strzq);
                                    }

                                    if (!model.getNotifStatus().equals("unread")){
                                        viewHolder.notifLayout.setBackgroundColor(getResources().getColor(R.color.white));
                                    }

                                    final Handler handler = new Handler();
                                    final int delay = 1000; //milliseconds

                                    handler.postDelayed(new Runnable(){
                                        public void run(){

                                            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                            DateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                            String date = df.format(Calendar.getInstance().getTime());
                                            String actualTime = model.getNotifDate().toString();
                                            Date time1;
                                            Date time2;

                                            try {

                                                time2 = format.parse(date);
                                                time1 = format.parse(actualTime);

                                                Interval interval =
                                                        new Interval(time1.getTime(), time2.getTime());
                                                Period period = interval.toPeriod();


                                                viewHolder.notifduration.setText(period.getMinutes()+ " min(s) ago");

                                                if ( (period.getHours() >= 1)) {
                                                    if (period.getDays() >= 1){
                                                        viewHolder.notifduration.setText(period.getDays() +" day(s) ago");
                                                    }else {
                                                        viewHolder.notifduration.setText(period.getHours()+ " hr(s) ago");
                                                    }
                                                }

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            handler.postDelayed(this, delay);
                                        }
                                    }, delay);


                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                                    .getReference("StationNotifHeader")
                                                    .child(Origin)
                                                    .child(model.getKey())
                                                    .child("NotifStatus");
                                            databaseReference.setValue("read");

                                            Intent intent = new Intent(getContext(), DetailedReport.class);
                                            intent.putExtra("key", model.getKey());
                                            intent.putExtra("vesselName", model.getVesselName());
                                            intent.putExtra("vesselKey", model.getVesselKey());
                                            startActivity(intent);

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
}
