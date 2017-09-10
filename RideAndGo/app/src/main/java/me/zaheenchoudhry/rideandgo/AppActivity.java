package me.zaheenchoudhry.rideandgo;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppActivity extends FragmentActivity {

    public static final int NUM_OF_MENU_OPTIONS = 5;
    public static final int MENU_OPTION_ACCOUNT = 0;
    public static final int MENU_OPTION_RIDE_LISTING = 1;
    public static final int MENU_OPTION_UPCOMING_RIDES = 2;
    public static final int MENU_OPTION_CREATE_RIDE = 3;
    public static final int MENU_OPTION_HISTORY_STATS = 4;
    public static final int RIDE_DETAIL_PAGE = -1;

    private float screenX, screenY;
    private int currentPage;

    private UserAccount userAccount;

    private RelativeLayout menuProfileHolder;
    private RelativeLayout[] menuOptions, menuOptionsIndicator;
    private NavigationView menu;
    private DrawerLayout appDrawerLayout;
    private TextView[] menuOptionsText;
    private ImageView menuProfileImage;
    private TextView menuProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        userAccount = (UserAccount)intent.getSerializableExtra(getString(R.string.user_account_serialized_name));

        currentPage = -2;

        setContentView(R.layout.activity_app);
        setUnit();

        menuOptions = new RelativeLayout[NUM_OF_MENU_OPTIONS];
        menuOptionsIndicator = new RelativeLayout[NUM_OF_MENU_OPTIONS];
        menuOptionsText = new TextView[NUM_OF_MENU_OPTIONS];

        appDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        menu = (NavigationView)findViewById(R.id.nav_view);
        menuProfileHolder = (RelativeLayout)findViewById(R.id.menu_profile_holder);
        menuOptions[0] = (RelativeLayout)findViewById(R.id.account_option_holder);
        menuOptions[1] = (RelativeLayout)findViewById(R.id.ride_listing_option_holder);
        menuOptions[2] = (RelativeLayout)findViewById(R.id.upcoming_rides_option_holder);
        menuOptions[3] = (RelativeLayout)findViewById(R.id.create_ride_holder);
        menuOptions[4] = (RelativeLayout)findViewById(R.id.history_stats_option_holder);
        menuOptionsIndicator[0] = (RelativeLayout)findViewById(R.id.account_option_indicator);
        menuOptionsIndicator[1] = (RelativeLayout)findViewById(R.id.ride_listing_option_indicator);
        menuOptionsIndicator[2] = (RelativeLayout)findViewById(R.id.upcoming_rides_option_indicator);
        menuOptionsIndicator[3] = (RelativeLayout)findViewById(R.id.create_ride_indicator);
        menuOptionsIndicator[4] = (RelativeLayout)findViewById(R.id.history_stats_option_indicator);
        menuOptionsText[0] = (TextView)findViewById(R.id.account_option_text);
        menuOptionsText[1] = (TextView)findViewById(R.id.ride_listing_option_text);
        menuOptionsText[2] = (TextView)findViewById(R.id.upcoming_rides_option_text);
        menuOptionsText[3] = (TextView)findViewById(R.id.create_ride_option_text);
        menuOptionsText[4] = (TextView)findViewById(R.id.history_stats_option_text);
        menuProfileImage = (ImageView)findViewById(R.id.menu_profile_image);
        menuProfileName = (TextView)findViewById(R.id.menu_profile_name);

        initializeMenuAndProfileDisplay();
        initializeMenuOptions();
        initializeMenuActions();

        menuOptions[MENU_OPTION_UPCOMING_RIDES].setBackgroundColor(Color.parseColor("#e3ebee"));
        menuOptionsIndicator[MENU_OPTION_UPCOMING_RIDES].setBackgroundColor(Color.parseColor("#22409A"));
        menuOptionsText[MENU_OPTION_UPCOMING_RIDES].setTextColor(Color.parseColor("#303030"));
        switchToPage(MENU_OPTION_UPCOMING_RIDES);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
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

    public void openMenu() {
        appDrawerLayout.openDrawer(Gravity.START, false);
    }

    private void setUnit() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
    }

    private void initializeMenuAndProfileDisplay() {
        ViewGroup.LayoutParams menuParams = menu.getLayoutParams();
        menuParams.width = (int)(screenX * 0.75f);

        ViewGroup.LayoutParams nameMenuHolderParams = menuProfileHolder.getLayoutParams();
        nameMenuHolderParams.height = (int)(screenY * 0.3f);

        RelativeLayout.LayoutParams menuProfileImageParams = (RelativeLayout.LayoutParams)menuProfileImage.getLayoutParams();
        menuProfileImageParams.width = (int)(screenY * 0.09f);
        menuProfileImageParams.height = (int)(screenY * 0.09f);
        menuProfileImageParams.topMargin = (int)(screenY * 0.1f);

        RelativeLayout.LayoutParams menuProfileNameParams = (RelativeLayout.LayoutParams)menuProfileName.getLayoutParams();
        menuProfileName.setTextSize((int)(screenX * 0.0115f));
        menuProfileNameParams.topMargin = (int)(screenY * 0.02f);
    }

    private void initializeMenuOptions() {
        ImageView[] menuOptionsIcon = new ImageView[NUM_OF_MENU_OPTIONS];
        menuOptionsIcon[0] = (ImageView)findViewById(R.id.account_option_icon);
        menuOptionsIcon[1] = (ImageView)findViewById(R.id.ride_listing_option_icon);
        menuOptionsIcon[2] = (ImageView)findViewById(R.id.upcoming_rides_option_icon);
        menuOptionsIcon[3] = (ImageView)findViewById(R.id.create_ride_option_icon);
        menuOptionsIcon[4] = (ImageView)findViewById(R.id.history_stats_option_icon);

        int[] menuOptionsIconDimension = new int[NUM_OF_MENU_OPTIONS];
        menuOptionsIconDimension[0] = (int)(screenY * 0.044f);
        menuOptionsIconDimension[1] = (int)(screenY * 0.031f);
        menuOptionsIconDimension[2] = (int)(screenY * 0.032f);
        menuOptionsIconDimension[3] = (int)(screenY * 0.057f);
        menuOptionsIconDimension[4] = (int)(screenY * 0.04f);

        int[] menuOptionsIconMarginStart = new int[NUM_OF_MENU_OPTIONS];
        menuOptionsIconMarginStart[0] = (int)(screenX * 0.092f);
        menuOptionsIconMarginStart[1] = (int)(screenX * 0.102f);
        menuOptionsIconMarginStart[2] = (int)(screenX * 0.1f);
        menuOptionsIconMarginStart[3] = (int)(screenX * 0.083f);
        menuOptionsIconMarginStart[4] = (int)(screenX * 0.093f);

        for (int i = 0; i < NUM_OF_MENU_OPTIONS; ++i) {
            ViewGroup.LayoutParams menuOptionsParams = menuOptions[i].getLayoutParams();
            ViewGroup.LayoutParams menuOptionsIndicatorParams = menuOptionsIndicator[i].getLayoutParams();
            RelativeLayout.LayoutParams menuOptionsIconParams = (RelativeLayout.LayoutParams)menuOptionsIcon[i].getLayoutParams();
            RelativeLayout.LayoutParams menuOptionsTextParams = (RelativeLayout.LayoutParams)menuOptionsText[i].getLayoutParams();

            menuOptionsParams.height = (int)(screenY * 0.105f);
            menuOptionsIndicatorParams.width = (int)(screenX * 0.02f);

            menuOptionsIconParams.width = menuOptionsIconDimension[i];
            menuOptionsIconParams.height = menuOptionsIconDimension[i];
            menuOptionsIconParams.setMarginStart(menuOptionsIconMarginStart[i]);

            menuOptionsText[i].setTextSize((int)(screenX * 0.012f));
            menuOptionsTextParams.setMarginStart((int)(screenX * 0.195f));
        }
    }

    private void initializeMenuActions() {
        for (int i = 0; i < NUM_OF_MENU_OPTIONS; ++i) {
            final int currentIndex = i;
            menuOptions[currentIndex].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menuOptions[currentIndex].setBackgroundColor(Color.parseColor("#e3ebee"));
                    menuOptionsIndicator[currentIndex].setBackgroundColor(Color.parseColor("#22409A"));
                    menuOptionsText[currentIndex].setTextColor(Color.parseColor("#303030"));
                    for (int j = 0; j < NUM_OF_MENU_OPTIONS; ++j) {
                        if (j != currentIndex) {
                            menuOptions[j].setBackgroundColor(Color.TRANSPARENT);
                            menuOptionsIndicator[j].setBackgroundColor(Color.TRANSPARENT);
                            menuOptionsText[j].setTextColor(Color.parseColor("#686868"));
                        }
                    }
                    switchToPage(currentIndex);
                }
            });
        }
    }

    public void switchToPage(int pageNumber) {
        appDrawerLayout.closeDrawer(Gravity.START);
        if (pageNumber != currentPage) {
            currentPage = pageNumber;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (pageNumber == MENU_OPTION_RIDE_LISTING) {
                RideListingFragment rideListingFragment = new RideListingFragment();
                transaction.replace(R.id.fragment_container, rideListingFragment);
            } else if (pageNumber == MENU_OPTION_CREATE_RIDE) {
                CreateRideFragment createRideFragment = new CreateRideFragment();
                transaction.replace(R.id.fragment_container, createRideFragment);
            } else if (pageNumber == MENU_OPTION_UPCOMING_RIDES) {
                UpcomingRidesFragment upcomingRidesFragment = new UpcomingRidesFragment();
                transaction.replace(R.id.fragment_container, upcomingRidesFragment);
            }
            //transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void setCurrentPageNumber(int currentPage) {
        this.currentPage = currentPage;
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
