package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Datas.DataReportsStats;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders.ReportsVesselStatsViewHolder;
import com.rakantao.pcg.lacostazamboanga.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {


   private TextView TVTotal,TVMDSD,TVDetained,TVEIAR,TVMPF;
   private Button BTNToday,btnMonthly,BTNYearly;
   private RecyclerView recyclerReports;
   private DatabaseReference dbFetchDaily;
   private DatabaseReference dbFetchMonthly;
   private DatabaseReference dbFetchYearly;

    private LinearLayoutManager linearLayoutManager;

    private String today;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        linearLayoutManager = new LinearLayoutManager(this);

        TVTotal = findViewById(R.id.TVOverAllTotal);
        TVMDSD = findViewById(R.id.TVMDSD);
        TVDetained = findViewById(R.id.TVDetained);
        TVEIAR = findViewById(R.id.TVEIAR);
        TVMPF = findViewById(R.id.TVMPF);


        BTNToday = findViewById(R.id.btnToday);
        btnMonthly = findViewById(R.id.btnMonthly);
        BTNYearly = findViewById(R.id.btnYearly);

        BTNToday.setOnClickListener(this);
        btnMonthly.setOnClickListener(this);
        BTNYearly.setOnClickListener(this);


        recyclerReports = findViewById(R.id.recyclerVesselTypes);




        recyclerReports.setHasFixedSize(true);
        recyclerReports.setLayoutManager(linearLayoutManager);

        final DateFormat today1 = new SimpleDateFormat("yyyy-MM-dd");

        today = today1.format(Calendar.getInstance().getTime());




        fetchReportStatsToday(today);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnToday:Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {



                                String monthwithzero;
                                if (monthOfYear < 10){
                                    int monthadd = monthOfYear + 1;
                                    monthwithzero = "0"+ monthadd;
                                    String getToday = year + "-" + (monthwithzero) + "-" + dayOfMonth;
                                    fetchReportStatsToday(getToday);
                                }else {
                                    int monthadd = monthOfYear + 1;
                                    monthwithzero = ""+ monthadd;
                                    String getToday = year + "-" + (monthwithzero) + "-" + dayOfMonth;
                                    fetchReportStatsToday(getToday);
                                }



                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                break;
            case R.id.btnMonthly:
                Calendar c1 = Calendar.getInstance();
                int mYear1 = c1.get(Calendar.YEAR);
                int mMonth1 = c1.get(Calendar.MONTH);
                int mDay1 = c1.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog1 = new DatePickerDialog(ReportsActivity.this, AlertDialog.THEME_HOLO_DARK,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                String monthwithzero;
                                if (monthOfYear < 10){
                                    int monthadd = monthOfYear + 1;
                                    monthwithzero = "0"+ monthadd;
                                    String getmonth = (monthwithzero) + "-" + year;
                                    fetchReportStatsMonthly(getmonth);
                                }else {
                                    int monthadd = monthOfYear + 1;
                                    monthwithzero = ""+ monthadd;
                                    String getmonth = (monthwithzero) + "-" + year;
                                    fetchReportStatsMonthly(getmonth);
                                }
                            }
                        }, mYear1, mMonth1, mDay1);
                ((ViewGroup) datePickerDialog1.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog1.show();



                break;
            case R.id.btnYearly:
                final CharSequence[] items2 = {
                        "2019",
                        "2020",
                        "2021",
                        "2022",
                        "2023",
                        "2024",
                        "2025"

                };
                android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(ReportsActivity.this);
                builder2.setTitle("Make your selection");
                builder2.setItems(items2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection


                        fetchReportStatsYearly((String) items2[item]);

                    }
                });
                android.app.AlertDialog alert2 = builder2.create();
                alert2.show();


                break;
        }
    }

    private void fetchReportStatsToday(final String today){

        dbFetchDaily = FirebaseDatabase.getInstance().getReference("ClearedByDate").child(today);

        dbFetchDaily.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder>(

                                    DataReportsStats.class,
                                    R.layout.report_listrow,
                                    ReportsVesselStatsViewHolder.class,
                                    dbFetchDaily

                            ) {
                                @Override
                                protected void populateViewHolder(final ReportsVesselStatsViewHolder viewHolder, DataReportsStats model, int position) {

                                    viewHolder.TVVesselTypeReports.setText(model.getVesselTypeReports());


                                    DatabaseReference fetchMDSD = FirebaseDatabase.getInstance().getReference("ClearedByVesselType");

                                    fetchMDSD.child(model.getVesselTypeReports()).orderByChild("Date").equalTo(today).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                viewHolder.TVSubstationReports.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                viewHolder.tvTotalForStats.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                viewHolder.TVSubstationReports.setText("NIL");
                                                viewHolder.tvTotalForStats.setText("NIL");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchDetained = FirebaseDatabase.getInstance().getReference("Detained");

                                    fetchDetained.orderByChild("Date").equalTo(today).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVDetained.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVDetained.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchOverAll = FirebaseDatabase.getInstance().getReference("Cleared");

                                    fetchOverAll.orderByChild("Date").equalTo(today).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVTotal.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                TVMDSD.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVTotal.setText("NIL");
                                                TVMDSD.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            };
                    recyclerReports.setAdapter(firebaseRecyclerAdapter);

                }else {

                    Toast.makeText(ReportsActivity.this, "No Records in the database yet", Toast.LENGTH_SHORT).show();
                    TVDetained.setText("NIL");
                    TVTotal.setText("NIL");
                    TVMDSD.setText("NIL");


                    FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder>(

                                    DataReportsStats.class,
                                    R.layout.report_listrow,
                                    ReportsVesselStatsViewHolder.class,
                                    dbFetchDaily

                            ) {
                                @Override
                                protected void populateViewHolder(final ReportsVesselStatsViewHolder viewHolder, DataReportsStats model, int position) {

                                    viewHolder.TVVesselTypeReports.setText(model.getVesselTypeReports());


                                    DatabaseReference fetchMDSD = FirebaseDatabase.getInstance().getReference("ClearedByVesselType");

                                    fetchMDSD.child(model.getVesselTypeReports()).orderByChild("Date").equalTo(today).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                viewHolder.TVSubstationReports.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                viewHolder.tvTotalForStats.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                viewHolder.TVSubstationReports.setText("NIL");
                                                viewHolder.tvTotalForStats.setText("NIL");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchDetained = FirebaseDatabase.getInstance().getReference("Detained");

                                    fetchDetained.orderByChild("Date").equalTo(today).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVDetained.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVDetained.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchOverAll = FirebaseDatabase.getInstance().getReference("Cleared");

                                    fetchOverAll.orderByChild("Date").equalTo(today).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVTotal.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                TVMDSD.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVTotal.setText("NIL");
                                                TVMDSD.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            };
                    recyclerReports.setAdapter(firebaseRecyclerAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void fetchReportStatsMonthly(final String month){

        dbFetchMonthly = FirebaseDatabase.getInstance().getReference("ClearedByMonth").child(month);

        dbFetchMonthly.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder>(

                                    DataReportsStats.class,
                                    R.layout.report_listrow,
                                    ReportsVesselStatsViewHolder.class,
                                    dbFetchMonthly

                            ) {
                                @Override
                                protected void populateViewHolder(final ReportsVesselStatsViewHolder viewHolder, DataReportsStats model, int position) {

                                    viewHolder.TVVesselTypeReports.setText(model.getVesselTypeReports());


                                    DatabaseReference fetchMDSD = FirebaseDatabase.getInstance().getReference("ClearedByVesselType");

                                    fetchMDSD.child(model.getVesselTypeReports()).orderByChild("Month").equalTo(month).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                viewHolder.TVSubstationReports.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                viewHolder.tvTotalForStats.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                viewHolder.TVSubstationReports.setText("NIL");
                                                viewHolder.tvTotalForStats.setText("NIL");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchDetained = FirebaseDatabase.getInstance().getReference("Detained");

                                    fetchDetained.orderByChild("Month").equalTo(month).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVDetained.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVDetained.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchOverAll = FirebaseDatabase.getInstance().getReference("Cleared");

                                    fetchOverAll.orderByChild("Month").equalTo(month).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVTotal.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                TVMDSD.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVTotal.setText("NIL");
                                                TVMDSD.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            };
                    recyclerReports.setAdapter(firebaseRecyclerAdapter);
                }else {

                    Toast.makeText(ReportsActivity.this, "No Records in the database yet", Toast.LENGTH_SHORT).show();
                    TVTotal.setText("NIL");
                    TVMDSD.setText("NIL");
                    TVDetained.setText("NIL");

                    FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder>(

                                    DataReportsStats.class,
                                    R.layout.report_listrow,
                                    ReportsVesselStatsViewHolder.class,
                                    dbFetchMonthly

                            ) {
                                @Override
                                protected void populateViewHolder(final ReportsVesselStatsViewHolder viewHolder, DataReportsStats model, int position) {

                                    viewHolder.TVVesselTypeReports.setText(model.getVesselTypeReports());


                                    DatabaseReference fetchMDSD = FirebaseDatabase.getInstance().getReference("ClearedByVesselType");

                                    fetchMDSD.child(model.getVesselTypeReports()).orderByChild("Month").equalTo(month).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                viewHolder.TVSubstationReports.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                viewHolder.tvTotalForStats.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                viewHolder.TVSubstationReports.setText("NIL");
                                                viewHolder.tvTotalForStats.setText("NIL");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchDetained = FirebaseDatabase.getInstance().getReference("Detained");

                                    fetchDetained.orderByChild("Month").equalTo(month).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVDetained.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVDetained.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchOverAll = FirebaseDatabase.getInstance().getReference("Cleared");

                                    fetchOverAll.orderByChild("Month").equalTo(month).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVTotal.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                TVMDSD.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVTotal.setText("NIL");
                                                TVMDSD.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            };
                    recyclerReports.setAdapter(firebaseRecyclerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void fetchReportStatsYearly(final String year){

        dbFetchYearly = FirebaseDatabase.getInstance().getReference("ClearedByYear").child(year);

        dbFetchYearly.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder>(

                                    DataReportsStats.class,
                                    R.layout.report_listrow,
                                    ReportsVesselStatsViewHolder.class,
                                    dbFetchYearly

                            ) {
                                @Override
                                protected void populateViewHolder(final ReportsVesselStatsViewHolder viewHolder, DataReportsStats model, int position) {

                                    viewHolder.TVVesselTypeReports.setText(model.getVesselTypeReports());


                                    DatabaseReference fetchMDSD = FirebaseDatabase.getInstance().getReference("ClearedByVesselType");

                                    fetchMDSD.child(model.getVesselTypeReports()).orderByChild("Year").equalTo(year).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                viewHolder.TVSubstationReports.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                viewHolder.tvTotalForStats.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                viewHolder.TVSubstationReports.setText("NIL");
                                                viewHolder.tvTotalForStats.setText("NIL");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchDetained = FirebaseDatabase.getInstance().getReference("Detained");

                                    fetchDetained.orderByChild("Year").equalTo(year).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVDetained.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVDetained.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchOverAll = FirebaseDatabase.getInstance().getReference("Cleared");

                                    fetchOverAll.orderByChild("Year").equalTo(year).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVTotal.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                TVMDSD.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVTotal.setText("NIL");
                                                TVMDSD.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            };
                    recyclerReports.setAdapter(firebaseRecyclerAdapter);
                }else {

                    Toast.makeText(ReportsActivity.this, "No Records in the database yet", Toast.LENGTH_SHORT).show();
                    TVTotal.setText("NIL");
                    TVMDSD.setText("NIL");
                    TVDetained.setText("NIL");


                    FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataReportsStats, ReportsVesselStatsViewHolder>(

                                    DataReportsStats.class,
                                    R.layout.report_listrow,
                                    ReportsVesselStatsViewHolder.class,
                                    dbFetchYearly

                            ) {
                                @Override
                                protected void populateViewHolder(final ReportsVesselStatsViewHolder viewHolder, DataReportsStats model, int position) {

                                    viewHolder.TVVesselTypeReports.setText(model.getVesselTypeReports());


                                    DatabaseReference fetchMDSD = FirebaseDatabase.getInstance().getReference("ClearedByVesselType");

                                    fetchMDSD.child(model.getVesselTypeReports()).orderByChild("Year").equalTo(year).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                viewHolder.TVSubstationReports.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                viewHolder.tvTotalForStats.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                viewHolder.TVSubstationReports.setText("NIL");
                                                viewHolder.tvTotalForStats.setText("NIL");
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchDetained = FirebaseDatabase.getInstance().getReference("Detained");

                                    fetchDetained.orderByChild("Year").equalTo(year).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVDetained.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVDetained.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference fetchOverAll = FirebaseDatabase.getInstance().getReference("Cleared");

                                    fetchOverAll.orderByChild("Year").equalTo(year).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                TVTotal.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                                TVMDSD.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                                            }else {
                                                TVTotal.setText("NIL");
                                                TVMDSD.setText("NIL");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }
                            };
                    recyclerReports.setAdapter(firebaseRecyclerAdapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
