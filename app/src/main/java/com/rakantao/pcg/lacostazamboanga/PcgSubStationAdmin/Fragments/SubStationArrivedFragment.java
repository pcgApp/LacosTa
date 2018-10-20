package com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Fragments;


import android.content.Intent;
import android.os.Bundle;
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
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.ArrivedDetails;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataArrivedInfo;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.ArrivedViewHolder;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.DepartedViewHolder;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubStationArrivedFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase;
    private FirebaseAuth firebaseAuth;
    View view;
    String userID;
    public String Origin;


    public SubStationArrivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_station_arrived, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerview = view.findViewById(R.id.recyclerArrivedSubStationAdmin);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child("Sunday").child("Arrived");
                break;
            case Calendar.MONDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Monday")).child("Arrived");
                break;
            case Calendar.TUESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Tuesday")).child("Arrived");
                break;
            case Calendar.WEDNESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Wednesday")).child("Arrived");
                break;
            case Calendar.THURSDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Thursday")).child("Arrived");
                break;
            case Calendar.FRIDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Friday")).child("Arrived");
                break;
            case Calendar.SATURDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("VesselSchedule").child(String.valueOf("Saturday")).child("Arrived");
                break;
        }

        mRecyclerview.setLayoutManager(linearLayoutManager);

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

                    FirebaseRecyclerAdapter<DataArrivedInfo, ArrivedViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataArrivedInfo, ArrivedViewHolder>(

                                    DataArrivedInfo.class,
                                    R.layout.arrived_listrow,
                                    ArrivedViewHolder.class,
                                    childRef.orderByChild("DestinationSubStation").equalTo(Origin)

                            ) {
                        @Override
                        protected void populateViewHolder(final ArrivedViewHolder viewHolder, final DataArrivedInfo model, int position) {
                            viewHolder.vtype.setText(model.getVesselType());
                            viewHolder.vname.setText(model.getVesselName());
                            viewHolder.vOrgin.setText(model.getOrigin());
                            viewHolder.vDestination.setText(model.getDestination());
                            viewHolder.vETD.setText(model.getDepartureTime());
                            viewHolder.vETA.setText(model.getArrivalTime());
                            viewHolder.vdaysched.setText(model.getScheduleDay());
                            viewHolder.vHoursTrav.setText(model.getTravelledTime());
                            viewHolder.vTimeArrived.setText(model.getActualTimeArrived());

                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(), ArrivedDetails.class);
                                    intent.putExtra("vesselName", model.getVesselName());
                                    startActivity(intent);
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
                                                    .into(viewHolder.vImage , new Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                        }

                                                        @Override
                                                        public void onError() {
                                                            Picasso.with(getContext())
                                                                    .load(image)
                                                                    .placeholder(R.drawable.zz)
                                                                    .into(viewHolder.vImage);
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
                    mRecyclerview.setAdapter(firebaseRecyclerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
