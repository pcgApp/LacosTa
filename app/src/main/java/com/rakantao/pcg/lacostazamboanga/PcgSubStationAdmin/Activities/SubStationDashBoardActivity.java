package com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Fragments.StationScheduleDashBoard;
import com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Fragments.SubStationDashFragment;
import com.rakantao.pcg.lacostazamboanga.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SubStationDashBoardActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_station_dash_board);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.substationContainer, new SubStationDashFragment())
                .commit();
    }
}
