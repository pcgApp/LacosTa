package com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rakantao.pcg.lacostazamboanga.DataUser;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.UploadListAdapter;
import com.rakantao.pcg.lacostazamboanga.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SendReportActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;

    private RecyclerView mUploadList;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;

    private EditText etSelectVesselName;

    private EditText etActualNumberInfant;
    private EditText etActualNumberChildren;
    private EditText etActualNumberAdult;
    private EditText etActualNumberCrew;

    private TextView tvBordingA;
    private TextView tvBordingB;
    private TextView tvBordingC;
    private TextView tvBordingD;

    private RadioGroup rgRemarks;
    private RadioButton rbOnHold;
    private RadioButton rbClear;

    public TextView fullname;

    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,getPersonnalDatas;
    private DatabaseReference databaseDailyVessels;
    private DatabaseReference databaseHistoryReports;
    public String userID;
    private String dayOfWeek;

    private String mRemarks;
    private String pushKey;
    String getStation;
    String getSubStation;
    private long countpost = 0;

    private FloatingActionButton mFab;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);


        Toolbar toolbar = findViewById(R.id.toolbar_sendReport);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Predeparture Inspections");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        getPersonnalDatas = FirebaseDatabase.getInstance().getReference();

        fullname = findViewById(R.id.fullname);
        mUploadList = findViewById(R.id.recyclerPersonnelImageList);
        mFab = findViewById(R.id.fabUploadInspect);

        rgRemarks = findViewById(R.id.radioGroupRemarks);
        rbClear = findViewById(R.id.rbClear);
        rbOnHold = findViewById(R.id.rbOnHold);

        etSelectVesselName = findViewById(R.id.etSelectVesselName);

        etActualNumberAdult = findViewById(R.id.etActualAdult);
        etActualNumberChildren = findViewById(R.id.etActualNumberChildren);
        etActualNumberInfant = findViewById(R.id.etActualNumberInfant);
        etActualNumberCrew = findViewById(R.id.etActualCrew);

        tvBordingA = findViewById(R.id.tvBordingA);
        tvBordingB = findViewById(R.id.tvBordingB);
        tvBordingC = findViewById(R.id.tvBordingC);
        tvBordingD = findViewById(R.id.tvBordingD);

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();

        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                dayOfWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayOfWeek = "Saturday";
                break;
        }

        databaseDailyVessels = FirebaseDatabase.getInstance().getReference();
        databaseDailyVessels = getPersonnalDatas.child("VesselSchedule").child(dayOfWeek).child("Pending");
        databaseHistoryReports = FirebaseDatabase.getInstance().getReference().child("HistoryReportRecords");

        databaseHistoryReports.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countpost = dataSnapshot.getChildrenCount();
                } else {
                    countpost = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        loadRadioGroup();
        loadBoardingTeam();

        etSelectVesselName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String UserID = firebaseUser.getUid().toString();


                DatabaseReference getPersonnelInfo = FirebaseDatabase.getInstance().getReference("Personnel");

                getPersonnelInfo.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            getStation = dataSnapshot.child("Station").getValue().toString();
                            getSubStation = dataSnapshot.child("SubStation").getValue().toString();



                            databaseDailyVessels.orderByChild("OriginSubStation").equalTo("CGSS " + getSubStation).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final List<String> areas = new ArrayList<String>();

                                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                                        String areaName = areaSnapshot.child("VesselName").getValue(String.class);
                                        Log.d("area", areaName);
                                        areas.add(areaName);
                                    }
                                    final CharSequence[] itemsz = areas.toArray(new CharSequence[areas.size()]);
                                    AlertDialog.Builder builderz = new AlertDialog.Builder(SendReportActivity.this);
                                    builderz.setTitle("Make a Selection");
                                    builderz.setItems(itemsz, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            etSelectVesselName.setText(itemsz[i]);
                                        }
                                    });
                                    AlertDialog alertDialogz = builderz.create();
                                    alertDialogz.show();
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


            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);
            }
        });

        getData();

    }

    private void loadBoardingTeam() {

        tvBordingB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String UserID = firebaseUser.getUid().toString();


                DatabaseReference getPersonnelInfo = FirebaseDatabase.getInstance().getReference("Personnel");

                getPersonnelInfo.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getStation = dataSnapshot.child("Station").getValue().toString();
                        getSubStation = dataSnapshot.child("SubStation").getValue().toString();


                        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();

                        databaseReference.child("NewPersonnelTable").child(getStation).child(getSubStation).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<String> persons = new ArrayList<String>();

                                if (dataSnapshot.exists()){

                                    for (DataSnapshot personSnap: dataSnapshot.getChildren()) {
                                        String personas = personSnap.child("LastName").getValue(String.class);
                                        String fname = personSnap.child("FirstName").getValue(String.class);
                                        Log.d("personas", personas);
                                        persons.add(personas + ", " + fname);
                                    }
                                    final CharSequence[] boardingB = persons.toArray(new CharSequence[persons.size()]);
                                    AlertDialog.Builder builderz = new AlertDialog.Builder(SendReportActivity.this);
                                    builderz.setTitle("Add members to your team");
                                    builderz.setItems(boardingB, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            tvBordingB.setText(boardingB[i]);
                                        }
                                    });
                                    AlertDialog alertDialogz = builderz.create();
                                    alertDialogz.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        tvBordingC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String UserID = firebaseUser.getUid().toString();


                DatabaseReference getPersonnelInfo = FirebaseDatabase.getInstance().getReference("Personnel");

                getPersonnelInfo.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getStation = dataSnapshot.child("Station").getValue().toString();
                        getSubStation = dataSnapshot.child("SubStation").getValue().toString();


                        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();

                        databaseReference.child("NewPersonnelTable").child(getStation).child(getSubStation).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<String> persons = new ArrayList<String>();

                                if (dataSnapshot.exists()){

                                    for (DataSnapshot personSnap: dataSnapshot.getChildren()) {
                                        String personas = personSnap.child("LastName").getValue(String.class);
                                        String fname = personSnap.child("FirstName").getValue(String.class);
                                        Log.d("personas", personas);
                                        persons.add(personas + ", " + fname);
                                    }
                                    final CharSequence[] boardingB = persons.toArray(new CharSequence[persons.size()]);
                                    AlertDialog.Builder builderz = new AlertDialog.Builder(SendReportActivity.this);
                                    builderz.setTitle("Add members to your team");
                                    builderz.setItems(boardingB, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            tvBordingC.setText(boardingB[i]);
                                        }
                                    });
                                    AlertDialog alertDialogz = builderz.create();
                                    alertDialogz.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        tvBordingD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String UserID = firebaseUser.getUid().toString();


                DatabaseReference getPersonnelInfo = FirebaseDatabase.getInstance().getReference("Personnel");

                getPersonnelInfo.child(UserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getStation = dataSnapshot.child("Station").getValue().toString();
                        getSubStation = dataSnapshot.child("SubStation").getValue().toString();


                        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();

                        databaseReference.child("NewPersonnelTable").child(getStation).child(getSubStation).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<String> persons = new ArrayList<String>();

                                if (dataSnapshot.exists()){

                                    for (DataSnapshot personSnap: dataSnapshot.getChildren()) {
                                        String personas = personSnap.child("LastName").getValue(String.class);
                                        String fname = personSnap.child("FirstName").getValue(String.class);
                                        Log.d("personas", personas);
                                        persons.add(personas + ", " + fname);
                                    }
                                    final CharSequence[] boardingB = persons.toArray(new CharSequence[persons.size()]);
                                    AlertDialog.Builder builderz = new AlertDialog.Builder(SendReportActivity.this);
                                    builderz.setTitle("Add members to your team");
                                    builderz.setItems(boardingB, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            tvBordingD.setText(boardingB[i]);
                                        }
                                    });
                                    AlertDialog alertDialogz = builderz.create();
                                    alertDialogz.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void loadRadioGroup() {

        rgRemarks.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rbClear:
                        mRemarks = "Clear";
                        break;
                    case R.id.rbOnHold:
                        mRemarks = "OnHold";
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //get vessel name before uploading the images
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm");
        final String format = simpleDateFormat.format(new Date());

        final Long tsLong = System.currentTimeMillis()/1000;

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = current_user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Report").child(uid).child(dayOfWeek).push();

        final DatabaseReference databaseNumberPassenger = FirebaseDatabase.getInstance().getReference().child("ReportAdminPassengerStats").child(dayOfWeek).push();
        final DatabaseReference databaseReport = FirebaseDatabase.getInstance().getReference().child("HistoryReportRecords").push();
        final DatabaseReference databaseReportImages = FirebaseDatabase.getInstance().getReference().child("HistoryReportImages");

        final String vesselName = etSelectVesselName.getText().toString().trim();
        final String getFullname = fullname.getText().toString().trim();
        final String numberInfant = etActualNumberInfant.getText().toString().trim();
        final String numberChildren = etActualNumberChildren.getText().toString().trim();
        final String numberAdult = etActualNumberAdult.getText().toString().trim();
        final String numberCrew = etActualNumberCrew.getText().toString().trim();

        final String bordA = tvBordingA.getText().toString().trim();
        final String bordB = tvBordingB.getText().toString().trim();
        final String bordC = tvBordingC.getText().toString().trim();
        final String bordD = tvBordingD.getText().toString().trim();

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
        final String date = df.format(Calendar.getInstance().getTime());

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null){

                //Toast.makeText(MainActivity.this, "Select files ", Toast.LENGTH_SHORT).show();

                    if (TextUtils.isEmpty(vesselName)){
                        Toast.makeText(SendReportActivity.this, "Please, Select Vessel Name", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(numberInfant)){
                        Toast.makeText(SendReportActivity.this, "Please, Enter Number of Infant", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(numberChildren)){
                        Toast.makeText(SendReportActivity.this, "Please, Enter Number of Children", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(numberAdult)) {
                        Toast.makeText(SendReportActivity.this, "Please, Enter Number of Adult", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(numberCrew)){
                        Toast.makeText(SendReportActivity.this, "Please, Enter Number of Crews", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(bordA)) {
                        Toast.makeText(SendReportActivity.this, "Select Bording Member A", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(bordB)){
                        Toast.makeText(SendReportActivity.this, "Select Bording Member B", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(bordC)){
                        Toast.makeText(SendReportActivity.this, "Select Bording Member C", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(bordD)){
                        Toast.makeText(SendReportActivity.this, "Select Bording Member D", Toast.LENGTH_SHORT).show();
                    } else if (mRemarks.equals("OnHold")){
                        Toast.makeText(SendReportActivity.this, "Still on Process", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(mRemarks)){
                        Toast.makeText(SendReportActivity.this, "Please Select Remarks", Toast.LENGTH_SHORT).show();
                    } else if (bordA.equals(bordB) || bordA.equals(bordC) || bordA.equals(bordD) ) {
                        Toast.makeText(SendReportActivity.this, "Please Select Other Team Members", Toast.LENGTH_SHORT).show();
                    } else {

                        int totalItemSelected = data.getClipData().getItemCount();

                        for (int i = 0; i < totalItemSelected; i++){

                            final Uri fileUri = data.getClipData().getItemAt(i).getUri();

                            final String filename = getFileName(fileUri);

                            fileNameList.add(filename);
                            fileDoneList.add("Uploading");
                            uploadListAdapter.notifyDataSetChanged();



                            StorageReference fileToUpload = mStorage.child("report_images").child(format).child(uid).child(vesselName).child(filename);

                            final int finalI = i;

                            final int counter = finalI;

                            fileToUpload.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    final String thumb_downloadUrl = task.getResult().getDownloadUrl().toString();

                                    if (task.isSuccessful()){

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

                                        final HashMap<String, String> HashString1 = new HashMap<String, String>();

                                        if (finalI == counter){
                                            String imageKey = databaseReference1.child("AdminImagesReport").child(vesselName).push().getKey();
                                            HashString1.put("imageUrl" ,thumb_downloadUrl);

                                            databaseReference.child("PersonnelReport").child(uid).child(vesselName).push().setValue(HashString1);
                                            databaseReference1.child("AdminImagesReport").child(vesselName).push().setValue(HashString1);
                                        }

                                        fileDoneList.remove(finalI);
                                        fileDoneList.add(finalI, "done");


                                        final HashMap HashString = new HashMap();


                                        //total number of passengers
                                        int totalNumberPassenger = Integer.parseInt(numberInfant) + Integer.parseInt(numberChildren) + Integer.parseInt(numberAdult) + Integer.parseInt(numberCrew);

                                        pushKey = databaseReport.getKey();

                                        HashString.put("pushKey", pushKey);
                                        HashString.put("timeUploaded", date);
                                        HashString.put("vesselName", vesselName);
                                        HashString.put("inspector", getFullname);
                                        HashString.put("numberInfant", numberInfant);
                                        HashString.put("numberChildren", numberChildren);
                                        HashString.put("numberAdult", numberAdult);
                                        HashString.put("numberCrew", numberCrew);
                                        HashString.put("inspectionRemarks", mRemarks);
                                        HashString.put("numberTotalPassenger", String.valueOf(totalNumberPassenger));
                                        HashString.put("bordingA", bordA);
                                        HashString.put("bordingB", bordB);
                                        HashString.put("bordingC", bordC);
                                        HashString.put("bordingD", bordD);
                                        HashString.put("counter", countpost);

                                        tvBordingA.setText(getFullname);


                                        databaseReport.setValue(HashString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                databaseNumberPassenger.setValue(HashString);

                                                databaseReference1.child("AdminImagesReport").child(vesselName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        databaseReportImages.child(pushKey).setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                                if (databaseError != null){
                                                                    Toast.makeText(SendReportActivity.this, "Copy Failed", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(SendReportActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                                    finish();
                                                                }
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        Toast.makeText(SendReportActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });

                                        mDatabase.setValue(HashString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SendReportActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();

                                                finish();
                                            }
                                        });

                                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("ReportAdmin").child(vesselName);
                                        databaseReference2.setValue(HashString);
                                        uploadListAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                    }
                }

            } else if (data.getData() != null){

                Toast.makeText(SendReportActivity.this, "Please Select Files to Upload", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    void getData(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userID =  currentUser.getUid();
        getPersonnalDatas.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataUser user = dataSnapshot.getValue(DataUser.class);
                if (dataSnapshot.exists()){

                    String firstname = user.FirstName;
                    String middlename = user.MiddleName;
                    String lastname = user.LastName;
                    
                    fullname.setText(lastname + ", "+ firstname +" " + middlename);
                    tvBordingA.setText(lastname + ", "+ firstname +" " + middlename);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
