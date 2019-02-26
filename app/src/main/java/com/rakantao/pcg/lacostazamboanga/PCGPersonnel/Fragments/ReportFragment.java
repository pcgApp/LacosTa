package com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Fragments;


import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities.ViewDetailedVessels;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Activities.DetailViewHistoryReportsActivity;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Activities.SendReportActivity;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Datas.DataHistoryReport;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.Datas.DataSendReport;
import com.rakantao.pcg.lacostazamboanga.PCGPersonnel.ViewHolders.ReportsViewHiolder;
import com.rakantao.pcg.lacostazamboanga.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ReportFragment extends Fragment {

    Button btnreport;
    RecyclerView recyclerView;
    View view;

     String dayOfWeek;

    private DatabaseReference mDatabaseHistory;
    private LinearLayoutManager linearLayoutManager;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_report, container, false);

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

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

        btnreport = view.findViewById(R.id.btnGoSendReport);
        recyclerView = view.findViewById(R.id.recyclerListOfReports);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mDatabaseHistory = FirebaseDatabase.getInstance().getReference().child("HistoryReportRecords");
        recyclerView.setLayoutManager(linearLayoutManager);

        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getContext(), SendReportActivity.class)
                         .putExtra("VesselName", "None")
                         .putExtra( "VesselKey", "None")
                         .putExtra( "VesselType", "None")
                         .putExtra( "VesselDay", "None"));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Query sortPost = mDatabaseHistory.orderByChild("counter");

        FirebaseRecyclerAdapter<DataHistoryReport, ReportsViewHiolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataHistoryReport, ReportsViewHiolder>(

                DataHistoryReport.class,
                R.layout.list_report,
                ReportsViewHiolder.class,
                sortPost

        ) {
            @Override
            protected void populateViewHolder(ReportsViewHiolder viewHolder, final DataHistoryReport model, int position) {

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
                    intent.putExtra("key",key);
                    startActivity(intent);

                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
