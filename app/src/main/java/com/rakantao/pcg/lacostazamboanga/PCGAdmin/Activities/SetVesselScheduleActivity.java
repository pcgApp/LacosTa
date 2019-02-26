package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataSetVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.ReportsViewHolder;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.SetVesselScheduleViewHolder;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Datas.DataHistoryReport;
import com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Activities.SubStationAdminHome;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SetVesselScheduleActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnExit,btnAddSched,btnEdit;
    Switch aSwitch;
    TextView vesselName,vesseltype;
    ImageView vesselImage;
    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;
    private RecyclerView recipeRecyclerview;
    private RecyclerView recyclerReports;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef,databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager1;
    String VesselName;
    String VesselType;
    String PassengerCapacity;
    String NumberOfCrew;
    String userType;
    private static final int GALLERY_PICK = 1;
    private StorageReference mImageStorage;
    private DatabaseReference mUserDatabase;
    private DatabaseReference databaseReferencez;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_vessel_schedule);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        VesselName = getIntent().getStringExtra("vesselName");
        VesselType = getIntent().getStringExtra("vesselType");
        PassengerCapacity = getIntent().getStringExtra("passengerCapacity");
        NumberOfCrew = getIntent().getStringExtra("numberOfCrew");



        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(VesselName);

        linearLayoutManager = new LinearLayoutManager(this);
        recipeRecyclerview = findViewById(R.id.recyclerSchedules);
        linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerReports = findViewById(R.id.recyclerReports);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child(VesselName+"DaysSched");

        databaseReferencez = FirebaseDatabase.getInstance().getReference("HistoryReportRecords");

        recipeRecyclerview.setLayoutManager(linearLayoutManager);
        recyclerReports.setLayoutManager(linearLayoutManager1);


        btnExit = findViewById(R.id.btnExit);
        btnAddSched = findViewById(R.id.btnAddSched);
        btnEdit = findViewById(R.id.btnEdit);

        aSwitch = findViewById(R.id.SWDock);

        vesselName = findViewById(R.id.TVVesselName);
        vesseltype = findViewById(R.id.TVVesselType);

        vesselImage = findViewById(R.id.IVVesselImage);

        vesselName.setText(VesselName);
        vesseltype.setText(VesselType);


        btnExit.setOnClickListener(this);
        btnAddSched.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        populateImage();
    }

    public void populateImage(){
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final String image = dataSnapshot.child("image").getValue().toString();

                    if (!image.equals("default")){
                        Picasso.with(SetVesselScheduleActivity.this)
                                .load(image)
                                .fit().centerCrop()
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.zz)
                                .into(vesselImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(SetVesselScheduleActivity.this)
                                                .load(image)
                                                .placeholder(R.drawable.zz)
                                                .into(vesselImage);
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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataSetVesselSched, SetVesselScheduleViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataSetVesselSched, SetVesselScheduleViewHolder>(

                        DataSetVesselSched.class,
                        R.layout.days_schedule_listrow,
                        SetVesselScheduleViewHolder.class,
                        childRef

                ) {
                    @Override
                    protected void populateViewHolder(final SetVesselScheduleViewHolder viewHolder, final DataSetVesselSched model, int position) {
                        viewHolder.tvday.setText(model.getDay()+"s");
                        viewHolder.tvLocation.setText(model.getLocation());
                        viewHolder.tvTime.setText(model.getTimes());
                        viewHolder.tvDecision.setText(model.getDecision());

                        userType = getIntent().getStringExtra("userType");

                        if (userType.equals("pcgsubstation")){
                            viewHolder.LLdelete.setVisibility(View.GONE);
                        }else {
                            viewHolder.LLdelete.setVisibility(View.VISIBLE);
                        }

                        viewHolder.LLdelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                AlertDialog.Builder builder = new AlertDialog.Builder(SetVesselScheduleActivity.this);

                                builder.setCancelable(true);
                                builder.setTitle("Alert!");
                                builder.setMessage("Are you sure you want to delete?");

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });

                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("VesselSchedule");

                                        databaseReference.child(model.getDay())
                                                .child("Pending")
                                                .child(model.getKey())
                                                .removeValue();

                                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(model.getVesselName()+"DaysSched");

                                        databaseReference1.child(model.getKey())
                                                .removeValue();

                                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("VesselsDashBoardAdmin");

                                        databaseReference2.child(model.getDay())
                                                .child(model.getKey())
                                                .removeValue();

                                        Toast.makeText(SetVesselScheduleActivity.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show();
                            }
                        });


                        viewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                if (userType.equals("admin")){
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(SetVesselScheduleActivity.this);

                                    builder.setCancelable(true);
                                    builder.setTitle("Alert!");
                                    builder.setMessage("Are you sure you want to edit?");

                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });


                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                            DatabaseReference fetchschedDetails = FirebaseDatabase.getInstance().getReference("VesselSchedule");

                                            fetchschedDetails.child(model.getDay())
                                                    .child("Pending")
                                                    .child(model.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){


                                                        //start
                                                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                                        LayoutInflater inflater = getLayoutInflater();
                                                        final View dialogView = inflater.inflate(R.layout.customlayout_addsched, null);
                                                        dialogBuilder.setView(dialogView);

                                                        final EditText etday = dialogView.findViewById(R.id.ETDay);
                                                        final EditText etOrigin = dialogView.findViewById(R.id.ETOrigin);
                                                        final EditText etDestination = dialogView.findViewById(R.id.ETDestination);
                                                        final EditText etTimeDepart = dialogView.findViewById(R.id.ETDepartTme);
                                                        final EditText etTimeArrive = dialogView.findViewById(R.id.ETArrivalTime);
                                                        final EditText etdayOfArrival = dialogView.findViewById(R.id.ETDayArrival);

                                                        Button btnSaveSched = dialogView.findViewById(R.id.btnSaveSched);



                                                        etday.setText(dataSnapshot.child("ScheduleDay").getValue().toString().trim());
                                                        etOrigin.setText(dataSnapshot.child("Origin").getValue().toString().trim());
                                                        etDestination.setText(dataSnapshot.child("Destination").getValue().toString().trim());
                                                        etTimeDepart.setText(dataSnapshot.child("DepartureTime").getValue().toString().trim());
                                                        etTimeArrive.setText(dataSnapshot.child("ArrivalTime").getValue().toString().trim());
                                                        etdayOfArrival.setText(dataSnapshot.child("DayOfArrival").getValue().toString().trim());


                                                        final AlertDialog dialog = dialogBuilder.create();

                                                        etdayOfArrival.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                final CharSequence[] items2 = {
                                                                        "Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
                                                                };
                                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                                                builder2.setTitle("Make your selection");
                                                                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int item) {
                                                                        // Do something with the selection
                                                                        etdayOfArrival.setText(items2[item]);
                                                                    }
                                                                });
                                                                AlertDialog alert2 = builder2.create();
                                                                alert2.show();
                                                            }
                                                        });


                                                        etday.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                final CharSequence[] items2 = {
                                                                        "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
                                                                };
                                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                                                builder2.setTitle("Make your selection");
                                                                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int item) {
                                                                        // Do something with the selection
                                                                        etday.setText(items2[item]);
                                                                    }
                                                                });
                                                                AlertDialog alert2 = builder2.create();
                                                                alert2.show();
                                                            }
                                                        });

                                                        etOrigin.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                final CharSequence[] items2 = {

                                                                        "ADUANA",
                                                                        "ARGAO",
                                                                        "BALICASAG",
                                                                        "BANTAYAN",
                                                                        "DANAO",
                                                                        "DUMANJUG",
                                                                        "GETAFE",
                                                                        "JAGNA",
                                                                        "LOAY",
                                                                        "MANDAUE",
                                                                        "NAGA",
                                                                        "PANGLAO",
                                                                        "PILAR",
                                                                        "PRES CARLOS P GARCIA",
                                                                        "PURO",
                                                                        "SAN FRANCISCO",
                                                                        "SAN REMIGIO",
                                                                        "STA FE",
                                                                        "SANTANDER",
                                                                        "TABUELAN",
                                                                        "TAGBILARAN",
                                                                        "TALIBON",
                                                                        "TINAGO",
                                                                        "TOLEDO",
                                                                        "TUBIGON",
                                                                        "UBAY",
                                                                        "Others"

                                                                };
                                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                                                builder2.setTitle("Make your selection");
                                                                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int item) {
                                                                        // Do something with the selection
                                                                        if (etDestination.getText().toString().equals(items2[item].toString())){
                                                                            Toast.makeText(SetVesselScheduleActivity.this, "You can't select the same value for origin and destination. Please select another.", Toast.LENGTH_SHORT).show();
                                                                            etOrigin.setText("Origin");
                                                                        }else if (items2[item].toString().equals("Others")){
                                                                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                                                            LayoutInflater inflater = getLayoutInflater();
                                                                            final View dialogView = inflater.inflate(R.layout.custom_add_location, null);
                                                                            dialogBuilder.setView(dialogView);

                                                                            final AlertDialog dialog1 = dialogBuilder.create();

                                                                            final EditText ETadd = dialogView.findViewById(R.id.ETaddLocation);
                                                                            Button BTNadd = dialogView.findViewById(R.id.btnAddLocation);

                                                                            BTNadd.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View view) {
                                                                                    String getETadd = ETadd.getText().toString();

                                                                                    if (TextUtils.isEmpty(getETadd)){
                                                                                        Toast.makeText(SetVesselScheduleActivity.this, "Please fill in the detail(s). ", Toast.LENGTH_SHORT).show();
                                                                                    }else {

                                                                                        etOrigin.setText(getETadd);
                                                                                        dialog1.dismiss();

                                                                                    }
                                                                                }
                                                                            });
                                                                            dialog1.show();

                                                                        }else {
                                                                            etOrigin.setText(items2[item]);
                                                                        }

                                                                    }
                                                                });
                                                                AlertDialog alert2 = builder2.create();
                                                                alert2.show();
                                                            }
                                                        });

                                                        etDestination.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                final CharSequence[] items2 = {
                                                                        "ADUANA",
                                                                        "ARGAO",
                                                                        "BALICASAG",
                                                                        "BANTAYAN",
                                                                        "DANAO",
                                                                        "DUMANJUG",
                                                                        "GETAFE",
                                                                        "JAGNA",
                                                                        "LOAY",
                                                                        "MANDAUE",
                                                                        "NAGA",
                                                                        "PANGLAO",
                                                                        "PILAR",
                                                                        "PRES CARLOS P GARCIA",
                                                                        "PURO",
                                                                        "SAN FRANCISCO",
                                                                        "SAN REMIGIO",
                                                                        "STA FE",
                                                                        "SANTANDER",
                                                                        "TABUELAN",
                                                                        "TAGBILARAN",
                                                                        "TALIBON",
                                                                        "TINAGO",
                                                                        "TOLEDO",
                                                                        "TUBIGON",
                                                                        "UBAY",
                                                                        "Others"
                                                                };
                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                                                builder1.setTitle("Make your selection");
                                                                builder1.setItems(items2, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int item) {
                                                                        // Do something with the selection
                                                                        if (etOrigin.getText().toString().equals(items2[item].toString())){
                                                                            Toast.makeText(SetVesselScheduleActivity.this, "You can't select the same value for origin and destination. Please select another.", Toast.LENGTH_SHORT).show();
                                                                            etDestination.setText("Destination");
                                                                        }else if (items2[item].toString().equals("Others")){
                                                                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                                                            LayoutInflater inflater = getLayoutInflater();
                                                                            final View dialogView = inflater.inflate(R.layout.custom_add_location, null);
                                                                            dialogBuilder.setView(dialogView);

                                                                            final AlertDialog dialog1 = dialogBuilder.create();

                                                                            final EditText ETadd = dialogView.findViewById(R.id.ETaddLocation);
                                                                            Button BTNadd = dialogView.findViewById(R.id.btnAddLocation);

                                                                            BTNadd.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View view) {
                                                                                    String getETadd = ETadd.getText().toString();

                                                                                    if (TextUtils.isEmpty(getETadd)){
                                                                                        Toast.makeText(SetVesselScheduleActivity.this, "Please fill in the detail(s). ", Toast.LENGTH_SHORT).show();
                                                                                    }else {
                                                                                        etDestination.setText(getETadd);
                                                                                        dialog1.dismiss();
                                                                                    }
                                                                                }
                                                                            });
                                                                            dialog1.show();

                                                                        }else {
                                                                            etDestination.setText(items2[item]);
                                                                        }

                                                                    }
                                                                });
                                                                AlertDialog alert1 = builder1.create();
                                                                alert1.show();
                                                            }
                                                        });

                                                        etTimeArrive.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                calendar = Calendar.getInstance();
                                                                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                                                                CalendarMinute = calendar.get(Calendar.MINUTE);
                                                                timepickerdialog = new TimePickerDialog(SetVesselScheduleActivity.this,
                                                                        new TimePickerDialog.OnTimeSetListener() {
                                                                            @Override
                                                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                                  int minute) {
                                                                                if (hourOfDay == 0) {
                                                                                    hourOfDay += 12;
                                                                                    format = "AM";
                                                                                }
                                                                                else if (hourOfDay == 12) {

                                                                                    format = "PM";
                                                                                }
                                                                                else if (hourOfDay > 12) {
                                                                                    hourOfDay -= 12;
                                                                                    format = "PM";
                                                                                }
                                                                                else {

                                                                                    format = "AM";
                                                                                }
                                                                                if (minute < 10){
                                                                                    etTimeArrive.setText(hourOfDay + ":0" + minute + " " + format);
                                                                                }else {
                                                                                    etTimeArrive.setText(hourOfDay + ":" + minute + " " + format);
                                                                                }

                                                                            }
                                                                        }, CalendarHour, CalendarMinute, false);
                                                                timepickerdialog.show();
                                                            }
                                                        });

                                                        etTimeDepart.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                calendar = Calendar.getInstance();
                                                                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                                                                CalendarMinute = calendar.get(Calendar.MINUTE);
                                                                timepickerdialog = new TimePickerDialog(SetVesselScheduleActivity.this,
                                                                        new TimePickerDialog.OnTimeSetListener() {
                                                                            @Override
                                                                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                                  int minute) {
                                                                                if (hourOfDay == 0) {
                                                                                    hourOfDay += 12;
                                                                                    format = "AM";
                                                                                }
                                                                                else if (hourOfDay == 12) {

                                                                                    format = "PM";
                                                                                }
                                                                                else if (hourOfDay > 12) {
                                                                                    hourOfDay -= 12;
                                                                                    format = "PM";
                                                                                }
                                                                                else {

                                                                                    format = "AM";
                                                                                }
                                                                                if (minute < 10){
                                                                                    etTimeDepart.setText(hourOfDay + ":0" + minute + " " + format);
                                                                                }else {
                                                                                    etTimeDepart.setText(hourOfDay + ":" + minute + " " + format);
                                                                                }

                                                                            }
                                                                        }, CalendarHour, CalendarMinute, false);
                                                                timepickerdialog.show();
                                                            }
                                                        });

                                                        btnSaveSched.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {

                                                                final String getday = etday.getText().toString();
                                                                String getOrigin = etOrigin.getText().toString();
                                                                String getDestination = etDestination.getText().toString();
                                                                String getTimeDepart = etTimeDepart.getText().toString();
                                                                String getTimeArrival = etTimeArrive.getText().toString();
                                                                String getDayofArrival = etdayOfArrival.getText().toString();
                                                                String originStation = null;
                                                                String destinationStation = null;

                                                                if (TextUtils.isEmpty(getday) ||
                                                                        TextUtils.isEmpty(getOrigin) ||
                                                                        TextUtils.isEmpty(getDestination) ||
                                                                        TextUtils.isEmpty(getTimeDepart) ||
                                                                        TextUtils.isEmpty(getTimeArrival) ||
                                                                        getOrigin.equals("Origin") ||
                                                                        getDestination.equals("Destination")){
                                                                    Toast.makeText(SetVesselScheduleActivity.this, "Please, don't leave a field blank.", Toast.LENGTH_SHORT).show();
                                                                }else {

                                                                    if (getDestination.equals("MANDAUE")
                                                                            || getDestination.equals("STA FE")
                                                                            || getDestination.equals("NAGA")
                                                                            || getDestination.equals("TOLEDO")
                                                                            || getDestination.equals("DANAO")
                                                                            || getDestination.equals("BANTAYAN")
                                                                            || getDestination.equals("ADUANA")
                                                                            || getDestination.equals("TABUELAN")
                                                                            || getDestination.equals("TINAGO")
                                                                            || getDestination.equals("SAN REMIGIO")
                                                                            || getDestination.equals("ARGAO")
                                                                            || getDestination.equals("SANTANDER")
                                                                            || getDestination.equals("DUMANJUG")){

                                                                        destinationStation = "CGS CEBU";

                                                                    }else if ((getDestination.equals("JAGNA")
                                                                            || getDestination.equals("UBAY")
                                                                            || getDestination.equals("TALIBON")
                                                                            || getDestination.equals("TUBIGON")
                                                                            || getDestination.equals("PANGLAO")
                                                                            || getDestination.equals("LOAY")
                                                                            || getDestination.equals("GETAFE")
                                                                            || getDestination.equals("BALICASAG")
                                                                            || getDestination.equals("TAGBILARAN")
                                                                            || getDestination.equals("PRES CARLOS P GARCIA"))){

                                                                        destinationStation = "CGS BOHOL";
                                                                    }else {
                                                                        destinationStation = "CGS CAMOTES";
                                                                    }


                                                                    if (getOrigin.equals("MANDAUE")
                                                                            || getOrigin.equals("STA FE")
                                                                            || getOrigin.equals("NAGA")
                                                                            || getOrigin.equals("TOLEDO")
                                                                            || getOrigin.equals("DANAO")
                                                                            || getOrigin.equals("BANTAYAN")
                                                                            || getOrigin.equals("ADUANA")
                                                                            || getOrigin.equals("TABUELAN")
                                                                            || getOrigin.equals("TINAGO")
                                                                            || getOrigin.equals("SAN REMIGIO")
                                                                            || getOrigin.equals("ARGAO")
                                                                            || getOrigin.equals("SANTANDER")
                                                                            || getOrigin.equals("DUMANJUG")){

                                                                        originStation = "CGS CEBU";

                                                                    }else if ((getOrigin.equals("JAGNA")
                                                                            || getOrigin.equals("UBAY")
                                                                            || getOrigin.equals("TALIBON")
                                                                            || getOrigin.equals("TUBIGON")
                                                                            || getOrigin.equals("PANGLAO")
                                                                            || getOrigin.equals("LOAY")
                                                                            || getOrigin.equals("GETAFE")
                                                                            || getOrigin.equals("TAGBILARAN")
                                                                            || getOrigin.equals("PRES CARLOS P GARCIA"))){

                                                                        originStation = "CGS BOHOL";
                                                                    }else {
                                                                        originStation = "CGS CAMOTES";
                                                                    }



                                                                    if (!TextUtils.isEmpty(destinationStation) || TextUtils.isEmpty(originStation)){

                                                                        if (dataSnapshot.child("ScheduleDay").getValue().toString().trim().equals(getday)){




                                                                            HashMap<String, String> HashString = new HashMap<String, String>();
                                                                            HashString.put("day", getday);
                                                                            HashString.put("locations", getOrigin +" - "+ getDestination);
                                                                            HashString.put("times", "ETD : " +getTimeDepart+ " ETA : "+getTimeArrival);
                                                                            HashString.put("decision", "on-going");
                                                                            HashString.put("Key", model.getKey());
                                                                            HashString.put("VesselName", VesselName);


                                                                            HashMap<String, String> HashString1 = new HashMap<String, String>();
                                                                            HashString1.put("VesselName", VesselName);
                                                                            HashString1.put("ScheduleDay", getday);
                                                                            HashString1.put("Origin", getOrigin);
                                                                            HashString1.put("Destination", getDestination);
                                                                            HashString1.put("DepartureTime", getTimeDepart);
                                                                            HashString1.put("ArrivalTime", getTimeArrival);
                                                                            HashString1.put("VesselType", VesselType);
                                                                            HashString1.put("VesselStatus", "Pending");
                                                                            HashString1.put("Key", model.getKey());
                                                                            HashString1.put("OriginStation", originStation);
                                                                            HashString1.put("DestinationStation", destinationStation);
                                                                            HashString1.put("DayOfArrival", getDayofArrival);
                                                                            HashString1.put("PassengerCapacity", PassengerCapacity);
                                                                            HashString1.put("NumberOfCrew", NumberOfCrew);
                                                                            HashString1.put("DistressStatus", "None");
                                                                            HashString1.put("DestinationSubStation", "CGSS "+getDestination);
                                                                            HashString1.put("OriginSubStation", "CGSS "+getOrigin);
                                                                            HashString1.put("LateIndicator", "Not Late");
                                                                            HashString1.put("ToAppear", "Appear");
                                                                            HashString1.put("Remarks", "None");

                                                                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                                                            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();

                                                                            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference(("VesselSchedule"));

                                                                            databaseReference = firebaseDatabase.getReference(VesselName+"DaysSched");
                                                                            databaseReference.child(model.getKey()).setValue(HashString);

                                                                            DatabaseReference databaseReference1 = firebaseDatabase1.getReference("VesselDetails").child(VesselName);
                                                                            databaseReference1.setValue(HashString1);

                                                                            databaseReference2.child(getday)
                                                                                    .child("Pending")
                                                                                    .child(model.getKey())
                                                                                    .setValue(HashString1);

                                                                            DatabaseReference databaseReference3 = firebaseDatabase1.getReference("VesselsDashBoardAdmin");

                                                                            databaseReference3.child(getday)
                                                                                    .child(model.getKey())
                                                                                    .setValue(HashString1);

                                                                            Toast.makeText(SetVesselScheduleActivity.this, "Schedule Updated", Toast.LENGTH_SHORT).show();
                                                                            dialog.dismiss();
                                                                        }else {

                                                                            final HashMap<String, String> HashString = new HashMap<String, String>();
                                                                            HashString.put("day", getday);
                                                                            HashString.put("locations", getOrigin +" - "+ getDestination);
                                                                            HashString.put("times", "ETD : " +getTimeDepart+ " ETA : "+getTimeArrival);
                                                                            HashString.put("decision", "on-going");
                                                                            HashString.put("Key", model.getKey());
                                                                            HashString.put("VesselName", VesselName);


                                                                            final HashMap<String, String> HashString1 = new HashMap<String, String>();
                                                                            HashString1.put("VesselName", VesselName);
                                                                            HashString1.put("ScheduleDay", getday);
                                                                            HashString1.put("Origin", getOrigin);
                                                                            HashString1.put("Destination", getDestination);
                                                                            HashString1.put("DepartureTime", getTimeDepart);
                                                                            HashString1.put("ArrivalTime", getTimeArrival);
                                                                            HashString1.put("VesselType", VesselType);
                                                                            HashString1.put("VesselStatus", "Pending");
                                                                            HashString1.put("Key", model.getKey());
                                                                            HashString1.put("OriginStation", originStation);
                                                                            HashString1.put("DestinationStation", destinationStation);
                                                                            HashString1.put("DayOfArrival", getDayofArrival);
                                                                            HashString1.put("PassengerCapacity", PassengerCapacity);
                                                                            HashString1.put("NumberOfCrew", NumberOfCrew);
                                                                            HashString1.put("DistressStatus", "None");
                                                                            HashString1.put("DestinationSubStation", "CGSS "+getDestination);
                                                                            HashString1.put("OriginSubStation", "CGSS "+getOrigin);
                                                                            HashString1.put("LateIndicator", "Not Late");
                                                                            HashString1.put("ToAppear", "Appear");
                                                                            HashString1.put("Remarks", "None");

                                                                            final DatabaseReference From1 = FirebaseDatabase.getInstance().getReference("VesselSchedule")
                                                                                    .child(dataSnapshot.child("ScheduleDay").getValue().toString().trim())
                                                                                    .child("Pending")
                                                                                    .child(model.getKey());

                                                                            final DatabaseReference From2 = FirebaseDatabase.getInstance().getReference(VesselName+"DaysSched");

                                                                            final DatabaseReference From3 = FirebaseDatabase.getInstance().getReference("VesselsDashBoardAdmin")
                                                                                    .child(dataSnapshot.child("ScheduleDay").getValue().toString().trim())
                                                                                    .child(model.getKey());

                                                                            From1.setValue(HashString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    From2.child(model.getKey()).setValue(HashString).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            From3.setValue(HashString1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    DatabaseReference To1 = FirebaseDatabase.getInstance().getReference("VesselSchedule")
                                                                                                            .child(getday)
                                                                                                            .child("Pending")
                                                                                                            .child(model.getKey());

                                                                                                    DatabaseReference To2 = FirebaseDatabase.getInstance().getReference("VesselsDashBoardAdmin")
                                                                                                            .child(getday)
                                                                                                            .child(model.getKey());

                                                                                                    moveFirebaseRecord1(From1, To1);
                                                                                                    moveFirebaseRecord2(From3, To2);

                                                                                                    Toast.makeText(SetVesselScheduleActivity.this, "Schedule Updated", Toast.LENGTH_SHORT).show();
                                                                                                    dialog.dismiss();
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });
                                                                                }
                                                                            });

                                                                        }

                                                                    }else {
                                                                        Toast.makeText(SetVesselScheduleActivity.this, "Station Destination "+destinationStation+" Station Origin "+ originStation, Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            }
                                                        });
                                                        dialog.show();
                                                        //end
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                    });
                                    builder.show();
                                }
                                return true;
                            }
                        });
                    }
                };

        recipeRecyclerview.setAdapter(firebaseRecyclerAdapter);

        FirebaseRecyclerAdapter<DataHistoryReport,ReportsViewHolder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<DataHistoryReport, ReportsViewHolder>(

                        DataHistoryReport.class,
                        R.layout.reports_listrow,
                        ReportsViewHolder.class,
                        databaseReferencez.orderByChild("vesselName").equalTo(VesselName)
                ) {
                    @Override
                    protected void populateViewHolder(ReportsViewHolder viewHolder, final DataHistoryReport model, int position) {

                        viewHolder.tvreportsboardA.setText(model.getBordingA());
                        viewHolder.tvreportsboardb.setText(model.getBordingB());
                        viewHolder.tvreportsboardc.setText(model.getBordingC());
                        viewHolder.tvreportsboardd.setText(model.getBordingD());

                        viewHolder.noadult.setText(model.getNumberAdult());
                        viewHolder.nochildren.setText(model.getNumberChildren());
                        viewHolder.nocrew.setText(model.getNumberCrew());
                        viewHolder.noinfant.setText(model.getNumberInfant());

                        viewHolder.totalpass.setText(model.getNumberTotalPassenger());
                        viewHolder.datetimeupload.setText(model.getTimeUploaded());
                        viewHolder.reportremarks.setText(model.getInspectionRemarks());


                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(SetVesselScheduleActivity.this, ViewDetailedVessels.class);
                                intent.putExtra("vesselName", VesselName);
                                intent.putExtra("Key", model.getKey());
                                startActivity(intent);
                            }
                        });
                    }
                };
        recyclerReports.setAdapter(firebaseRecyclerAdapter1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageURI = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageURI)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                //progress bar
                 AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                 View dialogView = inflater.inflate(R.layout.custom_progressdialog, null);

                 final AlertDialog dialog = dialogBuilder.create();

                dialog.show();
                Uri resultUri = result.getUri();

                //optional
                //compressor of image
                File thumb_filepath = new File(resultUri.getPath());


                //optional
                //compressor
                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //optional
                ByteArrayOutputStream baos  = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child("vessel_images").child(vesselName + ".jpg");

                //compress image to make thumbnaile
                final StorageReference thumb_file = mImageStorage.child("vessel_images").child("thumbs").child(vesselName + ".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){

                            UploadTask uploadTask = thumb_file.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    @SuppressWarnings("VisibleForTests")
                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()){

                                        Map updateHashMap = new HashMap<String, String>();
                                        updateHashMap.put("image", thumb_downloadUrl);
                                        updateHashMap.put("thumbimage", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    dialog.dismiss();
                                                    Toast.makeText(SetVesselScheduleActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    } else {

                                        Toast.makeText(SetVesselScheduleActivity.this, "Error uploading on Thumbnail", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                    }
                                }
                            });

                        } else {

                            Toast.makeText(SetVesselScheduleActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    @Override
    public void onBackPressed() {
        userType = getIntent().getStringExtra("userType");


        if (!userType.equals("admin")){
            new AlertDialog.Builder(SetVesselScheduleActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Are you sure you want to go back?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(SetVesselScheduleActivity.this, SubStationAdminHome.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }else {
            new AlertDialog.Builder(SetVesselScheduleActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Are you sure you want to go back?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(SetVesselScheduleActivity.this, ProfilesActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnEdit:
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_PICK);
                break;
            case R.id.btnExit:
                new AlertDialog.Builder(SetVesselScheduleActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to go back?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(SetVesselScheduleActivity.this, ProfilesActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
            case R.id.btnAddSched:

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.customlayout_addsched, null);
                dialogBuilder.setView(dialogView);

                final EditText etday = dialogView.findViewById(R.id.ETDay);
                final EditText etOrigin = dialogView.findViewById(R.id.ETOrigin);
                final EditText etDestination = dialogView.findViewById(R.id.ETDestination);
                final EditText etTimeDepart = dialogView.findViewById(R.id.ETDepartTme);
                final EditText etTimeArrive = dialogView.findViewById(R.id.ETArrivalTime);
                final EditText etdayOfArrival = dialogView.findViewById(R.id.ETDayArrival);

                Button btnSaveSched = dialogView.findViewById(R.id.btnSaveSched);


                final AlertDialog dialog = dialogBuilder.create();

                etdayOfArrival.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final CharSequence[] items2 = {
                                "Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
                        };
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                        builder2.setTitle("Make your selection");
                        builder2.setItems(items2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                etdayOfArrival.setText(items2[item]);
                            }
                        });
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                    }
                });


                etday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final CharSequence[] items2 = {
                                "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
                        };
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                        builder2.setTitle("Make your selection");
                        builder2.setItems(items2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                etday.setText(items2[item]);
                            }
                        });
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                    }
                });

                etOrigin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final CharSequence[] items2 = {

                                "ADUANA",
                                "ARGAO",
                                "BALICASAG",
                                "BANTAYAN",
                                "DANAO",
                                "DUMANJUG",
                                "GETAFE",
                                "JAGNA",
                                "LOAY",
                                "MANDAUE",
                                "NAGA",
                                "PANGLAO",
                                "PILAR",
                                "PRES CARLOS P GARCIA",
                                "PURO",
                                "SAN FRANCISCO",
                                "SAN REMIGIO",
                                "STA FE",
                                "SANTANDER",
                                "TABUELAN",
                                "TAGBILARAN",
                                "TALIBON",
                                "TINAGO",
                                "TOLEDO",
                                "TUBIGON",
                                "UBAY",
                                "Others"

                        };
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                        builder2.setTitle("Make your selection");
                        builder2.setItems(items2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection

                                if (etDestination.getText().toString().equals(items2[item].toString())){
                                    Toast.makeText(SetVesselScheduleActivity.this, "You can't select the same value for origin and destination. Please select another.", Toast.LENGTH_SHORT).show();
                                    etOrigin.setText("Origin");
                                }else if (items2[item].toString().equals("Others")){

                                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    final View dialogView = inflater.inflate(R.layout.custom_add_location, null);
                                    dialogBuilder.setView(dialogView);

                                    final AlertDialog dialog1 = dialogBuilder.create();

                                    final EditText ETadd = dialogView.findViewById(R.id.ETaddLocation);
                                    Button BTNadd = dialogView.findViewById(R.id.btnAddLocation);

                                    BTNadd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String getETadd = ETadd.getText().toString();

                                            if (TextUtils.isEmpty(getETadd)){
                                                Toast.makeText(SetVesselScheduleActivity.this, "Please fill in the detail(s). ", Toast.LENGTH_SHORT).show();
                                            }else {

                                                etOrigin.setText(getETadd);
                                                dialog1.dismiss();

                                            }
                                        }
                                    });
                                    dialog1.show();

                                }else {
                                    etOrigin.setText(items2[item]);
                                }

                            }
                        });
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
                    }
                });

                etDestination.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final CharSequence[] items2 = {
                                "ADUANA",
                                "ARGAO",
                                "BALICASAG",
                                "BANTAYAN",
                                "DANAO",
                                "DUMANJUG",
                                "GETAFE",
                                "JAGNA",
                                "LOAY",
                                "MANDAUE",
                                "NAGA",
                                "PANGLAO",
                                "PILAR",
                                "PRES CARLOS P GARCIA",
                                "PURO",
                                "SAN FRANCISCO",
                                "SAN REMIGIO",
                                "STA FE",
                                "SANTANDER",
                                "TABUELAN",
                                "TAGBILARAN",
                                "TALIBON",
                                "TINAGO",
                                "TOLEDO",
                                "TUBIGON",
                                "UBAY",
                                "Others"
                        };
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                        builder1.setTitle("Make your selection");
                        builder1.setItems(items2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                if (etOrigin.getText().toString().equals(items2[item].toString())){
                                    Toast.makeText(SetVesselScheduleActivity.this, "You can't select the same value for origin and destination. Please select another.", Toast.LENGTH_SHORT).show();
                                    etDestination.setText("Destination");
                                }else if (items2[item].toString().equals("Others")){
                                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    final View dialogView = inflater.inflate(R.layout.custom_add_location, null);
                                    dialogBuilder.setView(dialogView);

                                    final AlertDialog dialog1 = dialogBuilder.create();

                                    final EditText ETadd = dialogView.findViewById(R.id.ETaddLocation);
                                    Button BTNadd = dialogView.findViewById(R.id.btnAddLocation);

                                    BTNadd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String getETadd = ETadd.getText().toString();

                                            if (TextUtils.isEmpty(getETadd)){
                                                Toast.makeText(SetVesselScheduleActivity.this, "Please fill in the detail(s). ", Toast.LENGTH_SHORT).show();
                                            }else {
                                                etDestination.setText(getETadd);
                                                dialog1.dismiss();
                                            }
                                        }
                                    });
                                    dialog1.show();

                                }else {
                                    etDestination.setText(items2[item]);
                                }

                            }
                        });
                        AlertDialog alert1 = builder1.create();
                        alert1.show();
                    }
                });

                etTimeArrive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                        CalendarMinute = calendar.get(Calendar.MINUTE);
                        timepickerdialog = new TimePickerDialog(SetVesselScheduleActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        if (hourOfDay == 0) {
                                            hourOfDay += 12;
                                            format = "AM";
                                        }
                                        else if (hourOfDay == 12) {

                                            format = "PM";
                                        }
                                        else if (hourOfDay > 12) {
                                            hourOfDay -= 12;
                                            format = "PM";
                                        }
                                        else {

                                            format = "AM";
                                        }
                                        if (minute < 10){
                                            etTimeArrive.setText(hourOfDay + ":0" + minute + " " + format);
                                        }else {
                                            etTimeArrive.setText(hourOfDay + ":" + minute + " " + format);
                                        }

                                    }
                                }, CalendarHour, CalendarMinute, false);
                        timepickerdialog.show();
                    }
                });

                etTimeDepart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                        CalendarMinute = calendar.get(Calendar.MINUTE);
                        timepickerdialog = new TimePickerDialog(SetVesselScheduleActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        if (hourOfDay == 0) {
                                            hourOfDay += 12;
                                            format = "AM";
                                        }
                                        else if (hourOfDay == 12) {

                                            format = "PM";
                                        }
                                        else if (hourOfDay > 12) {
                                            hourOfDay -= 12;
                                            format = "PM";
                                        }
                                        else {

                                            format = "AM";
                                        }
                                        if (minute < 10){
                                            etTimeDepart.setText(hourOfDay + ":0" + minute + " " + format);
                                        }else {
                                            etTimeDepart.setText(hourOfDay + ":" + minute + " " + format);
                                        }

                                    }
                                }, CalendarHour, CalendarMinute, false);
                        timepickerdialog.show();
                    }
                });

                btnSaveSched.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String getday = etday.getText().toString();
                        String getOrigin = etOrigin.getText().toString();
                        String getDestination = etDestination.getText().toString();
                        String getTimeDepart = etTimeDepart.getText().toString();
                        String getTimeArrival = etTimeArrive.getText().toString();
                        String getDayofArrival = etdayOfArrival.getText().toString();
                        String originStation = null;
                        String destinationStation = null;

                        if (TextUtils.isEmpty(getday) ||
                                TextUtils.isEmpty(getOrigin) ||
                                TextUtils.isEmpty(getDestination) ||
                                TextUtils.isEmpty(getTimeDepart) ||
                                TextUtils.isEmpty(getTimeArrival) ||
                                getOrigin.equals("Origin") ||
                                getDestination.equals("Destination")){
                            Toast.makeText(SetVesselScheduleActivity.this, "Please, don't leave a field blank.", Toast.LENGTH_SHORT).show();
                        }else {

                            if (getDestination.equals("MANDAUE")
                                    || getDestination.equals("STA FE")
                                    || getDestination.equals("NAGA")
                                    || getDestination.equals("TOLEDO")
                                    || getDestination.equals("DANAO")
                                    || getDestination.equals("BANTAYAN")
                                    || getDestination.equals("ADUANA")
                                    || getDestination.equals("TABUELAN")
                                    || getDestination.equals("TINAGO")
                                    || getDestination.equals("SAN REMIGIO")
                                    || getDestination.equals("ARGAO")
                                    || getDestination.equals("SANTANDER")
                                    || getDestination.equals("DUMANJUG")){

                                destinationStation = "CGS CEBU";

                            }else if ((getDestination.equals("JAGNA")
                                    || getDestination.equals("UBAY")
                                    || getDestination.equals("TALIBON")
                                    || getDestination.equals("TUBIGON")
                                    || getDestination.equals("PANGLAO")
                                    || getDestination.equals("LOAY")
                                    || getDestination.equals("GETAFE")
                                    || getDestination.equals("BALICASAG")
                                    || getDestination.equals("TAGBILARAN")
                                    || getDestination.equals("PRES CARLOS P GARCIA"))){

                                destinationStation = "CGS BOHOL";
                            }else {
                                destinationStation = "CGS CAMOTES";
                            }


                            if (getOrigin.equals("MANDAUE")
                                    || getOrigin.equals("STA FE")
                                    || getOrigin.equals("NAGA")
                                    || getOrigin.equals("TOLEDO")
                                    || getOrigin.equals("DANAO")
                                    || getOrigin.equals("BANTAYAN")
                                    || getOrigin.equals("ADUANA")
                                    || getOrigin.equals("TABUELAN")
                                    || getOrigin.equals("TINAGO")
                                    || getOrigin.equals("SAN REMIGIO")
                                    || getOrigin.equals("ARGAO")
                                    || getOrigin.equals("SANTANDER")
                                    || getOrigin.equals("DUMANJUG")){

                                originStation = "CGS CEBU";

                            }else if ((getOrigin.equals("JAGNA")
                                    || getOrigin.equals("UBAY")
                                    || getOrigin.equals("TALIBON")
                                    || getOrigin.equals("TUBIGON")
                                    || getOrigin.equals("PANGLAO")
                                    || getOrigin.equals("LOAY")
                                    || getOrigin.equals("GETAFE")
                                    || getOrigin.equals("TAGBILARAN")
                                    || getOrigin.equals("PRES CARLOS P GARCIA"))){

                                originStation = "CGS BOHOL";
                            }else {
                                originStation = "CGS CAMOTES";
                            }



                            if (!TextUtils.isEmpty(destinationStation) || TextUtils.isEmpty(originStation)){

                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference(("VesselSchedule"));
                                String key = databaseReference2.child(getday).child("Pending").push().getKey();

                                HashMap<String, String> HashString = new HashMap<String, String>();
                                HashString.put("day", getday);
                                HashString.put("locations", getOrigin +" - "+ getDestination);
                                HashString.put("times", "ETD : " +getTimeDepart+ " ETA : "+getTimeArrival);
                                HashString.put("decision", "on-going");
                                HashString.put("Key", key);
                                HashString.put("VesselName", VesselName);

                                databaseReference = firebaseDatabase.getReference(VesselName+"DaysSched");
                                databaseReference.child(key).setValue(HashString);

                                HashMap<String, String> HashString1 = new HashMap<String, String>();
                                HashString1.put("VesselName", VesselName);
                                HashString1.put("ScheduleDay", getday);
                                HashString1.put("Origin", getOrigin);
                                HashString1.put("Destination", getDestination);
                                HashString1.put("DepartureTime", getTimeDepart);
                                HashString1.put("ArrivalTime", getTimeArrival);
                                HashString1.put("VesselType", VesselType);
                                HashString1.put("VesselStatus", "Pending");
                                HashString1.put("Key", key);
                                HashString1.put("OriginStation", originStation);
                                HashString1.put("DestinationStation", destinationStation);
                                HashString1.put("DayOfArrival", getDayofArrival);
                                HashString1.put("PassengerCapacity", PassengerCapacity);
                                HashString1.put("NumberOfCrew", NumberOfCrew);
                                HashString1.put("DistressStatus", "None");
                                HashString1.put("DestinationSubStation", "CGSS "+getDestination);
                                HashString1.put("OriginSubStation", "CGSS "+getOrigin);
                                HashString1.put("LateIndicator", "Not Late");
                                HashString1.put("ToAppear", "Appear");
                                HashString1.put("Remarks", "None");

                                DatabaseReference databaseReference1 = firebaseDatabase1.getReference("VesselDetails").child(VesselName);
                                databaseReference1.setValue(HashString1);

                                databaseReference2.child(getday)
                                        .child("Pending")
                                        .child(key)
                                        .setValue(HashString1);

                                DatabaseReference databaseReference3 = firebaseDatabase1.getReference("VesselsDashBoardAdmin");

                                databaseReference3.child(getday)
                                        .child(key)
                                        .setValue(HashString1);

                                DatabaseReference FreshSched = FirebaseDatabase.getInstance().getReference("FreshSchedule");

                                FreshSched.child(getday)
                                        .child("Pending")
                                        .child(key)
                                        .setValue(HashString1);

                                DatabaseReference FreshDashSched = FirebaseDatabase.getInstance().getReference("VesselsDashBoardAdmin");

                                FreshDashSched.child(getday)
                                        .child(key)
                                        .setValue(HashString1);


                                Toast.makeText(SetVesselScheduleActivity.this, "Schedule Saved", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }else {
                                Toast.makeText(SetVesselScheduleActivity.this, "Station Destination "+destinationStation+" Station Origin "+ originStation, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
                dialog.show();
                break;
        }
    }

    public void moveFirebaseRecord1(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {

                        } else {

                            fromPath.removeValue();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(SetVesselScheduleActivity.this, "Copy failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveFirebaseRecord2(final DatabaseReference fromPath2, final DatabaseReference toPath2) {
        fromPath2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath2.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                        } else {
                            fromPath2.removeValue();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(SetVesselScheduleActivity.this, "Copy failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

