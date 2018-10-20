package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.rakantao.pcg.lacostazamboanga.LoginActivity;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataSetVesselSched;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.SetVesselScheduleViewHolder;
import com.rakantao.pcg.lacostazamboanga.R;
import com.rakantao.pcg.lacostazamboanga.RegisterUser;
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
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef,databaseReference;
    private LinearLayoutManager linearLayoutManager;
    String VesselName;
    String VesselType;
    String PassengerCapacity;
    String NumberOfCrew;
    private static final int GALLERY_PICK = 1;
    private StorageReference mImageStorage;
    private DatabaseReference mUserDatabase;

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
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child(VesselName+"DaysSched");

        recipeRecyclerview.setLayoutManager(linearLayoutManager);


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




                        viewHolder.LLdelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

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

                            }
                        });
                    }
                };

        recipeRecyclerview.setAdapter(firebaseRecyclerAdapter);
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

                            final String download_url = task.getResult().getDownloadUrl().toString();

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
                                "Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
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
                                "MANDAUE",
                                "HAGNAYA",
                                "NAGA",
                                "TOLEDO",
                                "CAMOTES",
                                "DANAO",
                                "BANTAYAN",
                                "ADUANA",
                                "TABUELAN",
                                "TINAGO",
                                "BATO",
                                "ARGAO",
                                "TANGIL",
                                "JAGNA",
                                "UBAY",
                                "TALIBON",
                                "TUBIGON",
                                "PANGLAO",
                                "LOAY",
                                "GETAFE",
                                "TAGBILARAN",
                                "PRESIDENT CARLOS P GARCIA"
                        };
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                        builder2.setTitle("Make your selection");
                        builder2.setItems(items2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                if (etDestination.getText().toString().equals(etOrigin.getText().toString())){
                                    Toast.makeText(SetVesselScheduleActivity.this, "You can't select the same value for origin and destination. Please select another.", Toast.LENGTH_SHORT).show();
                                    etOrigin.setText("Origin");
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
                                "MANDAUE",
                                "HAGNAYA",
                                "NAGA",
                                "TOLEDO",
                                "CAMOTES",
                                "DANAO",
                                "BANTAYAN",
                                "ADUANA",
                                "TABUELAN",
                                "TINAGO",
                                "BATO",
                                "ARGAO",
                                "TANGIL",
                                "JAGNA",
                                "UBAY",
                                "TALIBON",
                                "TUBIGON",
                                "PANGLAO",
                                "LOAY",
                                "GETAFE",
                                "TAGBILARAN",
                                "PRESIDENT CARLOS P GARCIA"
                        };
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(SetVesselScheduleActivity.this);
                        builder2.setTitle("Make your selection");
                        builder2.setItems(items2, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                // Do something with the selection
                                if (etDestination.getText().toString().equals(etOrigin.getText().toString())){
                                    Toast.makeText(SetVesselScheduleActivity.this, "You can't select the same value for origin and destination. Please select another.", Toast.LENGTH_SHORT).show();
                                    etDestination.setText("Destination");

                                }else {
                                    etDestination.setText(items2[item]);
                                }

                            }
                        });
                        AlertDialog alert2 = builder2.create();
                        alert2.show();
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
                        String getday = etday.getText().toString().trim();
                        String getOrigin = etOrigin.getText().toString().trim();
                        String getDestination = etDestination.getText().toString().trim();
                        String getTimeDepart = etTimeDepart.getText().toString().trim();
                        String getTimeArrival = etTimeArrive.getText().toString().trim();
                        String getDayofArrival = etdayOfArrival.getText().toString().trim();

                        if (TextUtils.isEmpty(getday) ||
                                TextUtils.isEmpty(getOrigin) ||
                                TextUtils.isEmpty(getDestination) ||
                                TextUtils.isEmpty(getTimeDepart) ||
                                TextUtils.isEmpty(getTimeArrival) ||
                                getOrigin.equals("Origin") ||
                                getDestination.equals("Destination")){
                            Toast.makeText(SetVesselScheduleActivity.this, "Please, don't leave a field blank.", Toast.LENGTH_SHORT).show();
                        }else {

                            String originStation = null;
                            if (getOrigin.equals("MANDAUE")
                                    || getOrigin.equals("HAGNAYA")
                                    || getOrigin.equals("NAGA")
                                    || getOrigin.equals("TOLEDO")
                                    || getOrigin.equals("CAMOTES")
                                    || getOrigin.equals("DANAO")
                                    || getOrigin.equals("BANTAYAN")
                                    || getOrigin.equals("ADUANA")
                                    || getOrigin.equals("TABUELAN")
                                    || getOrigin.equals("TINAGO")
                                    || getOrigin.equals("BATO")
                                    || getOrigin.equals("ARGAO")
                                    || getOrigin.equals("TANGIL")){

                                originStation = "CGS CEBU";
                            }else if ((getOrigin.equals("JAGNA")
                                    || getOrigin.equals("UBAY")
                                    || getOrigin.equals("TALIBON")
                                    || getOrigin.equals("TUBIGON")
                                    || getOrigin.equals("PANGLAO")
                                    || getOrigin.equals("LOAY")
                                    || getOrigin.equals("GETAFE")
                                    || getOrigin.equals("TAGBILARAN")
                                    || getOrigin.equals("PRESIDENT CARLOS P GARCIA"))){

                                originStation = "CGS BOHOL";
                            }

                            String destinationStation = null;
                            if (getDestination.equals("MANDAUE")
                                    || getDestination.equals("HAGNAYA")
                                    || getDestination.equals("NAGA")
                                    || getDestination.equals("TOLEDO")
                                    || getDestination.equals("CAMOTES")
                                    || getDestination.equals("DANAO")
                                    || getDestination.equals("BANTAYAN")
                                    || getDestination.equals("ADUANA")
                                    || getDestination.equals("TABUELAN")
                                    || getDestination.equals("TINAGO")
                                    || getDestination.equals("BATO")
                                    || getDestination.equals("ARGAO")
                                    || getDestination.equals("TANGIL")){

                                destinationStation = "CGS CEBU";
                            }else if (getDestination.equals("JAGNA")
                                    || getDestination.equals("UBAY")
                                    || getDestination.equals("TALIBON")
                                    || getDestination.equals("TUBIGON")
                                    || getDestination.equals("PANGLAO")
                                    || getDestination.equals("LOAY")
                                    || getDestination.equals("GETAFE")
                                    || getOrigin.equals("TAGBILARAN")
                                    || getDestination.equals("PRESIDENT CARLOS P GARCIA")) {

                                destinationStation = "CGS BOHOL";
                            }

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
                            HashString1.put("Decision", "on-going");
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

                            Toast.makeText(SetVesselScheduleActivity.this, "Schedule Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
                break;
        }
    }
}
