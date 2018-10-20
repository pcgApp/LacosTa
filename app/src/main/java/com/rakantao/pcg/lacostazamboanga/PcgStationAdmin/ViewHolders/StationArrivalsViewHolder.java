package com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.ViewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rakantao.pcg.lacostazamboanga.R;

public class StationArrivalsViewHolder extends RecyclerView.ViewHolder {

    public TextView tvdes,tvvesNme,tvTime,tvorigin,tvorigintime,tvRemarks;
    public CardView cardviewArrivals;

    public StationArrivalsViewHolder(View itemView) {
        super(itemView);

        tvdes = itemView.findViewById(R.id.StationArrivalDashboardDestination);
        tvvesNme = itemView.findViewById(R.id.StationArrivalDashboardVessel);
        tvTime = itemView.findViewById(R.id.StationArrivalDashboardTime);
        tvRemarks = itemView.findViewById(R.id.StationArrivalDashboardRemarks);
        tvorigin = itemView.findViewById(R.id.StationArrivalDashboardOrigin);
        tvorigintime = itemView.findViewById(R.id.StationArrivalDashboardOriginTime);
        cardviewArrivals = itemView.findViewById(R.id.cardViewArrivals);
    }
}
