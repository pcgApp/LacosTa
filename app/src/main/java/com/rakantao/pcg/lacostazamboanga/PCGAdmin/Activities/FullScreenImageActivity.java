package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakantao.pcg.lacostazamboanga.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {

    ImageView imageFull;
    String ImageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        imageFull = findViewById(R.id.IVImage);


        ImageURL = this.getIntent().getStringExtra("ImageURL");

        if (!ImageURL.equals("default")){
            Picasso.with(FullScreenImageActivity.this)
                    .load(ImageURL)
                    .fit().centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.zz)
                    .into(imageFull , new Callback() {
                        @Override
                        public void onSuccess() {

                            PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageFull);
                            photoViewAttacher.update();
                        }

                        @Override
                        public void onError() {
                            Picasso.with(FullScreenImageActivity.this)
                                    .load(ImageURL)
                                    .placeholder(R.drawable.zz)
                                    .into(imageFull);
                        }
                    });
        }
    }
}
