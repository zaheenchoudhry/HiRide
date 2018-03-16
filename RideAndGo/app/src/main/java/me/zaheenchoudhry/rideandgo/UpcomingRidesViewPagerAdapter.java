package me.zaheenchoudhry.rideandgo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class UpcomingRidesViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUMBER_OF_TABS = 3;
    private static final int TAB_SHORTLISTED_RIDES_INDEX= 0;
    private static final int TAB_BOOKED_RIDES_INDEX = 1;
    private static final int TAB_OFFERED_RIDES_INDEX = 2;
    private static final String TAB_SHORTLISTED_RIDES_TITLE = "Requests";
    private static final String TAB_BOOKED_RIDES_TITLE = "Booked";
    private static final String TAB_OFFERED_RIDES_TITLE = "Offered";

    private Context context;
    private UpcomingRidesFragment upcomingRidesFragment;

    public UpcomingRidesViewPagerAdapter(FragmentManager fragmentManager, Context context, UpcomingRidesFragment upcomingRidesFragment) {
        super(fragmentManager);
        this.context = context;
        this.upcomingRidesFragment = upcomingRidesFragment;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TAB_SHORTLISTED_RIDES_INDEX:
                return TAB_SHORTLISTED_RIDES_TITLE;
            case TAB_BOOKED_RIDES_INDEX:
                return TAB_BOOKED_RIDES_TITLE;
            case TAB_OFFERED_RIDES_INDEX:
                return TAB_OFFERED_RIDES_TITLE;
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_SHORTLISTED_RIDES_INDEX:
                return new RequestsFragment(upcomingRidesFragment);
            case TAB_BOOKED_RIDES_INDEX:
                return new BookedRidesFragment(upcomingRidesFragment);
            case TAB_OFFERED_RIDES_INDEX:
                return new OfferedRidesFragment(upcomingRidesFragment);
        }
        return null;
    }
}
