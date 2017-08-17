package me.zaheenchoudhry.rideandgo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppActivity extends Activity {

    private float screenX, screenY;

    private RelativeLayout nameMenuHolder, accountOptionHolder, rideListingOptionHolder;
    private RelativeLayout upcomingRidesOptionHolder, createRideHolder, historyStatsOptionHolder;
    private NavigationView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setUnit();
        setMenuSize();

        LoginFragment createRideFragment = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, createRideFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0 ){
            fragmentManager.popBackStack();
            //fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void setUnit() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
    }

    private void setMenuSize() {
        menu = (NavigationView)findViewById(R.id.nav_view);
        ViewGroup.LayoutParams menuParams = menu.getLayoutParams();
        menuParams.width = (int)(screenX * 0.75f);

        nameMenuHolder = (RelativeLayout)findViewById(R.id.name_holder_menu);
        ViewGroup.LayoutParams nameMenuHolderParams = nameMenuHolder.getLayoutParams();
        nameMenuHolderParams.height = (int)(screenY * 0.3f);

        accountOptionHolder = (RelativeLayout)findViewById(R.id.account_option_holder);
        ViewGroup.LayoutParams accountOptionHolderParams = accountOptionHolder.getLayoutParams();
        accountOptionHolderParams.height = (int)(screenY * 0.105f);

        rideListingOptionHolder = (RelativeLayout)findViewById(R.id.ride_listing_option_holder);
        ViewGroup.LayoutParams rideListingOptionHolderParams = rideListingOptionHolder.getLayoutParams();
        rideListingOptionHolderParams.height = (int)(screenY * 0.105f);

        upcomingRidesOptionHolder = (RelativeLayout)findViewById(R.id.upcoming_rides_option_holder);
        ViewGroup.LayoutParams upcomingRidesOptionHolderParams = upcomingRidesOptionHolder.getLayoutParams();
        upcomingRidesOptionHolderParams.height = (int)(screenY * 0.105f);

        createRideHolder = (RelativeLayout)findViewById(R.id.create_ride_holder);
        ViewGroup.LayoutParams createRideHolderParams = createRideHolder.getLayoutParams();
        createRideHolderParams.height = (int)(screenY * 0.105f);

        historyStatsOptionHolder = (RelativeLayout)findViewById(R.id.history_stats_option_holder);
        ViewGroup.LayoutParams historyStatsOptionHolderParams = historyStatsOptionHolder.getLayoutParams();
        historyStatsOptionHolderParams.height = (int)(screenY * 0.105f);


        RelativeLayout accountOptionIndicator = (RelativeLayout)findViewById(R.id.account_option_indicator);
        ViewGroup.LayoutParams accountOptionIndicatorParams = accountOptionIndicator.getLayoutParams();
        accountOptionIndicatorParams.width = (int)(screenX * 0.02f);

        RelativeLayout rideListingOptionIndicator = (RelativeLayout)findViewById(R.id.ride_listing_option_indicator);
        ViewGroup.LayoutParams rideListingOptionIndicatorParams = rideListingOptionIndicator.getLayoutParams();
        rideListingOptionIndicatorParams.width = (int)(screenX * 0.02f);

        RelativeLayout upcomingRidesOptionIndicator = (RelativeLayout)findViewById(R.id.upcoming_rides_option_indicator);
        ViewGroup.LayoutParams upcomingRidesOptionIndicatorParams = upcomingRidesOptionIndicator.getLayoutParams();
        upcomingRidesOptionIndicatorParams.width = (int)(screenX * 0.02f);

        RelativeLayout createRideIndicator = (RelativeLayout)findViewById(R.id.create_ride_indicator);
        ViewGroup.LayoutParams createRideIndicatorParams = createRideIndicator.getLayoutParams();
        createRideIndicatorParams.width = (int)(screenX * 0.02f);

        RelativeLayout historyStatsOptionIndicator = (RelativeLayout)findViewById(R.id.history_stats_option_indicator);
        ViewGroup.LayoutParams historyStatsOptionIndicatorParams = historyStatsOptionIndicator.getLayoutParams();
        historyStatsOptionIndicatorParams.width = (int)(screenX * 0.02f);


        RelativeLayout menuOptionsSeparator = (RelativeLayout)findViewById(R.id.menu_options_separator);
        ViewGroup.LayoutParams menuOptionsSeparatorParams = menuOptionsSeparator.getLayoutParams();
        menuOptionsSeparatorParams.height = (int)(screenY * 0.002f);


        ImageView accountOptionIcon = (ImageView)findViewById(R.id.account_option_icon);
        RelativeLayout.LayoutParams accountOptionIconParams = (RelativeLayout.LayoutParams)accountOptionIcon.getLayoutParams();
        accountOptionIconParams.width = (int)(screenY * 0.044f);
        accountOptionIconParams.height = (int)(screenY * 0.044f);
        accountOptionIconParams.setMarginStart((int)(screenX * 0.092f));

        TextView accountOptionText = (TextView)findViewById(R.id.account_option_text);
        RelativeLayout.LayoutParams accountOptionTextParams = (RelativeLayout.LayoutParams)accountOptionText.getLayoutParams();
        accountOptionText.setTextSize((int)(screenX * 0.012f));
        accountOptionTextParams.setMarginStart((int)(screenX * 0.195f));


        ImageView rideListingOptionIcon = (ImageView)findViewById(R.id.ride_listing_option_icon);
        RelativeLayout.LayoutParams rideListingOptionIconParams = (RelativeLayout.LayoutParams)rideListingOptionIcon.getLayoutParams();
        rideListingOptionIconParams.width = (int)(screenY * 0.031f);
        rideListingOptionIconParams.height = (int)(screenY * 0.031f);
        rideListingOptionIconParams.setMarginStart((int)(screenX * 0.102f));

        TextView rideListingOptionText = (TextView)findViewById(R.id.ride_listing_option_text);
        RelativeLayout.LayoutParams rideListingOptionTextParams = (RelativeLayout.LayoutParams)rideListingOptionText.getLayoutParams();
        rideListingOptionText.setTextSize((int)(screenX * 0.012f));
        rideListingOptionTextParams.setMarginStart((int)(screenX * 0.195f));


        ImageView upcomingRidesOptionIcon = (ImageView)findViewById(R.id.upcoming_rides_option_icon);
        RelativeLayout.LayoutParams upcomingRidesOptionIconParams = (RelativeLayout.LayoutParams)upcomingRidesOptionIcon.getLayoutParams();
        upcomingRidesOptionIconParams.width = (int)(screenY * 0.032f);
        upcomingRidesOptionIconParams.height = (int)(screenY * 0.032f);
        upcomingRidesOptionIconParams.setMarginStart((int)(screenX * 0.1f));

        TextView upcomingRidesOptionText = (TextView)findViewById(R.id.upcoming_rides_option_text);
        RelativeLayout.LayoutParams upcomingRidesOptionTextParams = (RelativeLayout.LayoutParams)upcomingRidesOptionText.getLayoutParams();
        upcomingRidesOptionText.setTextSize((int)(screenX * 0.012f));
        upcomingRidesOptionTextParams.setMarginStart((int)(screenX * 0.195f));


        ImageView createRideOptionIcon = (ImageView)findViewById(R.id.create_ride_option_icon);
        RelativeLayout.LayoutParams createRideOptionIconParams = (RelativeLayout.LayoutParams)createRideOptionIcon.getLayoutParams();
        createRideOptionIconParams.width = (int)(screenY * 0.057f);
        createRideOptionIconParams.height = (int)(screenY * 0.057f);
        createRideOptionIconParams.setMarginStart((int)(screenX * 0.083f));

        TextView createRideOptionText = (TextView)findViewById(R.id.create_ride_option_text);
        RelativeLayout.LayoutParams createRideOptionTextParams = (RelativeLayout.LayoutParams)createRideOptionText.getLayoutParams();
        createRideOptionText.setTextSize((int)(screenX * 0.012f));
        createRideOptionTextParams.setMarginStart((int)(screenX * 0.195f));


        ImageView historyStatsOptionIcon = (ImageView)findViewById(R.id.history_stats_option_icon);
        RelativeLayout.LayoutParams historyStatsOptionIconParams = (RelativeLayout.LayoutParams)historyStatsOptionIcon.getLayoutParams();
        historyStatsOptionIconParams.width = (int)(screenY * 0.04f);
        historyStatsOptionIconParams.height = (int)(screenY * 0.04f);
        historyStatsOptionIconParams.setMarginStart((int)(screenX * 0.093f));

        TextView historyStatsOptionText = (TextView)findViewById(R.id.history_stats_option_text);
        RelativeLayout.LayoutParams historyStatsOptionTextParams = (RelativeLayout.LayoutParams)historyStatsOptionText.getLayoutParams();
        historyStatsOptionText.setTextSize((int)(screenX * 0.012f));
        historyStatsOptionTextParams.setMarginStart((int)(screenX * 0.195f));
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }
}
