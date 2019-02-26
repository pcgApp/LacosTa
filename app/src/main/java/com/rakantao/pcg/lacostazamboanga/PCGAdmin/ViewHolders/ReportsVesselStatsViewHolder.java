package com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rakantao.pcg.lacostazamboanga.R;

public class ReportsVesselStatsViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTotalForStats,TVVesselTypeReports,TVSubstationReports,TVStationReports;

    public ReportsVesselStatsViewHolder(View itemView) {
        super(itemView);

        tvTotalForStats = itemView.findViewById(R.id.TVTotalForStats);
        TVVesselTypeReports = itemView.findViewById(R.id.TVVesselTypeReport);
        TVSubstationReports = itemView.findViewById(R.id.TVSubStationReport);
        TVStationReports = itemView.findViewById(R.id.TVStationReport);
    }
}
