package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.DataUser;
import com.rakantao.pcg.lacostazamboanga.LoginActivity;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.PCGAdminHome;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.RegisterVessel;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.SetVesselScheduleActivity;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataSetVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataShippingProfileList;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataVesselInfo;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.ProfileShippingListViewHolder;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.SetVesselScheduleViewHolder;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Activities.PCGHomeActivity;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Activities.PcgStationAdminHome;
import com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Activities.SubStationAdminHome;
import com.rakantao.pcg.lacostazamboanga.PublicUser.Activities.UserHomeActivity;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShipsListProfileFragment extends Fragment {


    View view;
    FloatingActionButton btnAddVessel;
    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef,databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase;
    String userID;
    private EditText searchcontent;
    private ImageButton searchBtn;
    String usertype;

    public ShipsListProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_ships_list_profile, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext());
        Recyclerview = view.findViewById(R.id.recyclerShipsList);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        searchcontent = view.findViewById(R.id.ETSearchContent);
        searchBtn = view.findViewById(R.id.btnSearch);

        childRef = mDatabaseRef.child("Vessels");

        Recyclerview.setLayoutManager(linearLayoutManager);

        btnAddVessel = view.findViewById(R.id.btnAddVessel);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userID =  currentUser.getUid();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getSearchContent = searchcontent.getText().toString();

                firebaseUserSearch(getSearchContent);
            }
        });

        String searchtext = searchcontent.getText().toString();
        if (TextUtils.isEmpty(searchtext)){
            fetchDatas();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataUser user = dataSnapshot.getValue(DataUser.class);
                if(dataSnapshot.exists()){

                     usertype = (user.Usertype);
                        if (usertype.equals("admin")){
                            btnAddVessel.setVisibility(View.VISIBLE);
                        }else if (usertype.equals("pcgsubstation")){
                            btnAddVessel.setVisibility(View.GONE);
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAddVessel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RegisterVessel.class));
            }
        });

        return view;
    }

    private void firebaseUserSearch(String searchText) {

        Query firebaseSearchQuery = childRef.orderByChild("Vessel_Name").startAt(searchText).endAt(searchText+ "\uf8ff");


        FirebaseRecyclerAdapter<DataVesselInfo, ProfileShippingListViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataVesselInfo, ProfileShippingListViewHolder>(

                        DataVesselInfo.class,
                        R.layout.profile_shippinglist,
                        ProfileShippingListViewHolder.class,
                        firebaseSearchQuery

                ) {
                    @Override
                    protected void populateViewHolder(final ProfileShippingListViewHolder viewHolder, DataVesselInfo model, int position) {
                        viewHolder.vesselname.setText(model.getVessel_Name());
                        viewHolder.vesseltype.setText(model.getVessel_Type());
                        viewHolder.vesselorigin.setText("Passenger Capacity : "+model.getVesselPassengerCapacity());
                        viewHolder.vesseldestination.setText("Crew Capacity : "+model.getVesselNumberOfCrew());

                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(model.getVessel_Name());

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
                                                .into(viewHolder.imagevessel, new Callback() {
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


                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myIntent = new Intent(getContext(), SetVesselScheduleActivity.class);
                                myIntent.putExtra("vesselName", viewHolder.vesselname.getText().toString());
                                myIntent.putExtra("vesselType", viewHolder.vesseltype.getText().toString());
                                myIntent.putExtra("userType", usertype);
                                startActivity(myIntent);
                            }
                        });


                    }
                };

        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchDatas();
    }

    private void fetchDatas(){
        FirebaseRecyclerAdapter<DataVesselInfo, ProfileShippingListViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataVesselInfo, ProfileShippingListViewHolder>(

                        DataVesselInfo.class,
                        R.layout.profile_shippinglist,
                        ProfileShippingListViewHolder.class,
                        childRef

                ) {
                    @Override
                    protected void populateViewHolder(final ProfileShippingListViewHolder viewHolder, DataVesselInfo model, int position) {
                        viewHolder.vesselname.setText(model.getVessel_Name());
                        viewHolder.vesseltype.setText(model.getVessel_Type());
                        viewHolder.vesselorigin.setText("Passenger Capacity : "+model.getVesselPassengerCapacity());
                        viewHolder.vesseldestination.setText("Crew Capacity : "+model.getVesselNumberOfCrew());

                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(model.getVessel_Name());

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
                                                .into(viewHolder.imagevessel, new Callback() {
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


                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myIntent = new Intent(getContext(), SetVesselScheduleActivity.class);
                                myIntent.putExtra("vesselName", viewHolder.vesselname.getText().toString());
                                myIntent.putExtra("vesselType", viewHolder.vesseltype.getText().toString());
                                myIntent.putExtra("userType", usertype);
                                startActivity(myIntent);
                            }
                        });


                    }
                };

        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }
}
