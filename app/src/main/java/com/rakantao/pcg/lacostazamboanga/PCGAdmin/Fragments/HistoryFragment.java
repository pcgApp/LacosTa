package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Activities.DetailViewHistoryReportsActivity;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Datas.DataHistoryReport;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.ViewHolders.ReportsViewHiolder;
import com.rakantao.pcg.lacostazamboanga.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class HistoryFragment extends Fragment {

    View view;
    RecyclerView recyclerViewHistory;
    Button btnDate;
    final Calendar myCalendar = Calendar.getInstance();
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mDatabaseHistory;
    TextView tvNotif;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_history, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistoryReps);
        btnDate = view.findViewById(R.id.btnselectDate);
        tvNotif = view.findViewById(R.id.TVnotif);


        mDatabaseHistory = FirebaseDatabase.getInstance().getReference().child("HistoryReportRecords");

        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewHistory.setLayoutManager(linearLayoutManager);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                String date = sdf.format(myCalendar.getTime());

                btnDate.setText(date);

                updateLabel(date);
            }
        };

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        return view;
    }

    private void updateLabel(String getDate) {


        if (getDate.equals("Select Date")) {
            tvNotif.setVisibility(View.VISIBLE);
            recyclerViewHistory.setVisibility(View.GONE);
        }else {
            tvNotif.setVisibility(View.GONE);
            recyclerViewHistory.setVisibility(View.VISIBLE);
            FirebaseRecyclerAdapter<DataHistoryReport, ReportsViewHiolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataHistoryReport, ReportsViewHiolder>(

                    DataHistoryReport.class,
                    R.layout.list_report,
                    ReportsViewHiolder.class,
                    mDatabaseHistory.orderByChild("timeUploaded").equalTo(getDate)

            ) {
                @Override
                protected void populateViewHolder(ReportsViewHiolder viewHolder, DataHistoryReport model, int position) {

                    final String valueKey = model.getKey();
                    final String key = model.getPushKey();
                    final String vesselName = model.getVesselName();

                    viewHolder.tvVesselName.setText("Vessel Name : " + model.getVesselName());
                    viewHolder.tvInspector.setText("POIC : " + model.getInspector());
                    viewHolder.tvTimeUploaded.setText("Date/Time Inspected : " + model.getTimeUploaded());
                    viewHolder.tvActualNumOfPassengers.setText("Actual Number Of Passengers/Crews : " + model.getNumberTotalPassenger());

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(getContext(), DetailViewHistoryReportsActivity.class);
                            intent.putExtra("valueKey", valueKey);
                            intent.putExtra("vesselName", vesselName);
                            intent.putExtra("key", key);
                            startActivity(intent);

                        }
                    });
                }
            };
            recyclerViewHistory.setAdapter(firebaseRecyclerAdapter);
        }
    }

    }
