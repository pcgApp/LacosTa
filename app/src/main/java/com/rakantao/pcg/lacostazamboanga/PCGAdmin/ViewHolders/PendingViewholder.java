package com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rakantao.pcg.lacostazamboanga.R;

public class PendingViewholder extends RecyclerView.ViewHolder {

    public TextView vesselname,vesseltype,origin,destination,departime,arrivaltime,runnabletime,schedday,getMin,distressnotifieradmin,vsei;
    public TextView tvchildren,tvadults,tvinfant,tvcrew,totalpassenger;
    public ImageView vesselimage;
    public Button btnclear,btnSelectVessel;
    View view;
    public CardView cardViewPending;

    public PendingViewholder(View itemView) {
        super(itemView);
        view = itemView;

        tvchildren = itemView.findViewById(R.id.TVchildrenAdmin);
        tvadults = itemView.findViewById(R.id.TVAdultsAdmin);
        tvinfant = itemView.findViewById(R.id.TVInfantAdmin);
        tvcrew = itemView.findViewById(R.id.TVcrewAdmin);
        totalpassenger = itemView.findViewById(R.id.TVTotalAdmin);

        vesselname = itemView.findViewById(R.id.TVPendingVesselName);
        vesseltype = itemView.findViewById(R.id.TVPendingVesselType);
        origin = itemView.findViewById(R.id.TVPendingOrigin);
        destination = itemView.findViewById(R.id.TVPendingDestination);
        departime = itemView.findViewById(R.id.TVPendingDepartureTime);
        arrivaltime = itemView.findViewById(R.id.TVPendingArrivalTime);
        runnabletime = itemView.findViewById(R.id.TVPendingRunningTime);
        schedday = itemView.findViewById(R.id.TVPendingSchedDay);
        getMin = itemView.findViewById(R.id.getMin);
        distressnotifieradmin = itemView.findViewById(R.id.TvDistressNotifierAdmin);
        vsei = itemView.findViewById(R.id.TVVseiLeader);

        cardViewPending = itemView.findViewById(R.id.cardViewPending);

        vesselimage = itemView.findViewById(R.id.IVPendingImageVessel);

        btnclear = itemView.findViewById(R.id.BtnPendingClear);

        btnSelectVessel = itemView.findViewById(R.id.btnSelectVessel);

    }
}
