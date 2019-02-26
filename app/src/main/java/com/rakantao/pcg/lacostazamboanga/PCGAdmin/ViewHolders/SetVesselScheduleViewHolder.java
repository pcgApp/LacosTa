package com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.rakantao.pcg.lacostazamboanga.R;

public class SetVesselScheduleViewHolder extends RecyclerView.ViewHolder {

    public TextView tvday;
    public TextView tvLocation;
    public TextView tvTime;
    public TextView tvDecision;
    public LinearLayout LLdelete;
    public View view;

    public SetVesselScheduleViewHolder(View itemView) {
        super(itemView);

        view = itemView;

        tvday = itemView.findViewById(R.id.HolderTvDay);
        tvLocation = itemView.findViewById(R.id.HolderTvLocation);
        tvTime = itemView.findViewById(R.id.HolderTvTime);
        tvDecision = itemView.findViewById(R.id.HolderTvDecision);
        LLdelete = itemView.findViewById(R.id.LLdelete);
    }
}
