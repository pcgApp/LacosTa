package com.rakantao.pcg.lacostazamboanga.PCGAdmin.Activities;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rakantao.pcg.lacostazamboanga.PCGAdmin.Fragments.SchedulesDashBoard;
import com.rakantao.pcg.lacostazamboanga.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DashboardActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.dashContainer, new SchedulesDashBoard())
                .commit();

    }
}
