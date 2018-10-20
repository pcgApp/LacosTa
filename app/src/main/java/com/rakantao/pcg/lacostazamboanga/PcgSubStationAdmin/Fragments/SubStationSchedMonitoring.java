package com.rakantao.pcg.lacostazamboanga.PcgSubStationAdmin.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Fragments.PendingFragment;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Fragments.ScheduleMonitoringFragment;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Fragments.StationArrivedFragment;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Fragments.StationArrivingFragment;
import com.rakantao.pcg.lacostazamboanga.PcgStationAdmin.Fragments.StationDepartedFragment;
import com.rakantao.pcg.lacostazamboanga.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubStationSchedMonitoring extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    View view;

    public SubStationSchedMonitoring() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_station_sched_monitoring, container, false);

        viewPager = view.findViewById(R.id.viewpagerVoyageSubStationAdmin);
        setupViewPager(viewPager);

        tabLayout = view.findViewById(R.id.tabsMonitoringSubStationAdmin);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SubStationPendingFragment(), "SCHEDULE");
        adapter.addFragment(new SubStationDepartedFragment(), "DEPARTED");
        adapter.addFragment(new SubStationArrivingFragment(), "ARRIVING");
        adapter.addFragment(new SubStationArrivedFragment(), "ARRIVED");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
