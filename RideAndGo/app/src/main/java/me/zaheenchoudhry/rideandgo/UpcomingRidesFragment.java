package me.zaheenchoudhry.rideandgo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UpcomingRidesFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upcoming_rides_fragment, container, false);

        UpcomingRidesViewPagerAdapter adapter = new UpcomingRidesViewPagerAdapter(getChildFragmentManager(), getActivity(), this);
        ViewPager viewPager = (ViewPager)view.findViewById(R.id.upcoming_rides_viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.upcoming_rides_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
