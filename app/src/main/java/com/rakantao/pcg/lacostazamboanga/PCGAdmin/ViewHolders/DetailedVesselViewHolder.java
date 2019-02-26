package com.rakantao.pcg.lacostazamboanga.PCGAdmin.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Picasso;

public class DetailedVesselViewHolder extends RecyclerView.ViewHolder {

    public ImageView vImage;
    public TextView ImageDesc,TimeStamp;
    View view;

    public DetailedVesselViewHolder(View itemView) {
        super(itemView);

        view = itemView;

        vImage = itemView.findViewById(R.id.DetailedVesselImage);
        ImageDesc = itemView.findViewById(R.id.DetailedVesselDescription);
        TimeStamp = itemView.findViewById(R.id.DetailedVesselDate);


    }

    public void vImage(String imageUrl, Context applicationContext) {
        Picasso.with(applicationContext).load(imageUrl).placeholder(R.drawable.zz).into(vImage);
    }
}
