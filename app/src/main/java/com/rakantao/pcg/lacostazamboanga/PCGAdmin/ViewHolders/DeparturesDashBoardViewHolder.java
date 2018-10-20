package com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rakantao.pcg.lacostazamboanga.R;

public class DeparturesDashBoardViewHolder extends RecyclerView.ViewHolder {

    public TextView tvdes,tvorigin,tvvesNme,tvTime,tvRemarks;
    public CardView cardViewAdmin;

    public DeparturesDashBoardViewHolder(View itemView) {
        super(itemView);

        tvdes = itemView.findViewById(R.id.DeparturesDashboardDestination);
        tvorigin = itemView.findViewById(R.id.DeparturesDashboardOrigin);
        tvvesNme = itemView.findViewById(R.id.DeparturesDashboardVessel);
        tvTime = itemView.findViewById(R.id.DeparturesDashboardTime);
        tvRemarks = itemView.findViewById(R.id.DeparturesDashboardRemarks);
        cardViewAdmin = itemView.findViewById(R.id.cardviewAdmin);

    }
}
