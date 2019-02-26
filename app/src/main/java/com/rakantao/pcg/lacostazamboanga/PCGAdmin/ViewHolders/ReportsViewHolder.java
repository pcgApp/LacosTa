package com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rakantao.pcg.lacostazamboanga.R;

public class ReportsViewHolder extends RecyclerView.ViewHolder{

    View view;
    public TextView tvreportsboardA,tvreportsboardb,tvreportsboardc,tvreportsboardd,noadult,nochildren,noinfant,nocrew,totalpass,datetimeupload,reportremarks;

    public ReportsViewHolder(View itemView) {
        super(itemView);

        view = itemView;


        tvreportsboardA = itemView.findViewById(R.id.reportboardingA);
        tvreportsboardb = itemView.findViewById(R.id.reportboardingB);
        tvreportsboardc = itemView.findViewById(R.id.reportboardingC);
        tvreportsboardd = itemView.findViewById(R.id.reportboardingD);
        noadult = itemView.findViewById(R.id.reportAdult);
        nochildren = itemView.findViewById(R.id.reportChildren);
        noinfant = itemView.findViewById(R.id.reportInfant);
        nocrew = itemView.findViewById(R.id.reportCrew);
        totalpass = itemView.findViewById(R.id.reportTotalPassenger);
        datetimeupload = itemView.findViewById(R.id.reportTimeUploaded);
        reportremarks = itemView.findViewById(R.id.reportRemark);

    }
}
