package me.zaheenchoudhry.rideandgo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static me.zaheenchoudhry.rideandgo.MainActivity.UNIT_NTH_ROOT;

public class AppActivity extends Activity {

    private float screenX, screenY;
    private float unitX, unitY;
    private RelativeLayout layout;
    private Toolbar toolbar;
    private LinearLayout.LayoutParams toolbarParams;
    private CoordinatorLayout coordinatorLayout;
    private CoordinatorLayout.LayoutParams coordinatorLayoutParams;
    private AppBarLayout appBarLayout;
    private AppBarLayout.LayoutParams appBarLayoutParams;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CollapsingToolbarLayout.LayoutParams collapsingToolbarLayoutParams;
    private ImageView toolbarImage;
    private LinearLayout.LayoutParams toolbarImageParams;

    private Toolbar toolbar2;
    private CollapsingToolbarLayout collapsingToolbarLayout2;
    private TextView expandedToolbarText1, expandedToolbarText2;
    private TabLayout listingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //layout = new RelativeLayout(this);
        //setContentView(layout);
        //layout.setId(R.id.WelcomeActivity);

        setContentView(R.layout.activity_app);

        RideListingFragment createRideFragment = new RideListingFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, createRideFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        setUnit();

        /*toolbar2 = (Toolbar)findViewById(R.id.toolbar);

        collapsingToolbarLayout2 = (CollapsingToolbarLayout)findViewById(R.id.collapsingToolbar);

        expandedToolbarText1 = (TextView)findViewById(R.id.love_music);
        expandedToolbarText1.setText("LOVE MUSIC");
        expandedToolbarText2 = (TextView)findViewById(R.id.love_music2);
        expandedToolbarText2.setText("This season top 20 albums");*/

        /*listingTabLayout = (TabLayout)findViewById(R.id.listing_tabs);
        listingTabLayout.addTab(listingTabLayout.newTab().setText("List"));
        listingTabLayout.addTab(listingTabLayout.newTab().setText("Map"));*/

        /*coordinatorLayout = new CoordinatorLayout(this);
        coordinatorLayoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        coordinatorLayout.setLayoutParams(coordinatorLayoutParams);
        coordinatorLayout.setFitsSystemWindows(true);
        layout.addView(coordinatorLayout);

        appBarLayout = new AppBarLayout(this);
        appBarLayoutParams = new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(screenY * 0.3f));
        appBarLayout.setLayoutParams(appBarLayoutParams);
        appBarLayout.setFitsSystemWindows(true);
        appBarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        coordinatorLayout.addView(appBarLayout);

        collapsingToolbarLayout = new CollapsingToolbarLayout(this);
        collapsingToolbarLayoutParams = new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        collapsingToolbarLayout.setLayoutParams(collapsingToolbarLayoutParams);
        collapsingToolbarLayout.setFitsSystemWindows(true);
        collapsingToolbarLayoutParams.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX);
        //collapsingToolbarLayout.setContentScrim();
        //collapsingToolbarLayout.setExpandedTitleMarginStart(40);
        collapsingToolbarLayout.setTitle("Test");
        appBarLayout.addView(collapsingToolbarLayout);

        toolbarImage = new ImageView(this);
        toolbarImage.setImageResource(R.drawable.city6);
        toolbarImageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbarImage.setLayoutParams(toolbarImageParams);
        toolbarImage.setFitsSystemWindows(true);
        toolbarImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        collapsingToolbarLayout.addView(toolbarImage);

        toolbar = new Toolbar(this);
        toolbarParams = new LinearLayout.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, (int)(screenY * 0.13f));
        toolbar.setLayoutParams(toolbarParams);
        toolbar.setVisibility(View.VISIBLE);
        //toolbar.setBackgroundColor(Color.BLUE);
        //toolbar.setTitle("Blaaaah");
        collapsingToolbarLayout.addView(toolbar);*/


        /*FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RideListingFragment rideListingFragment = new RideListingFragment();
        fragmentTransaction.add(layout.getId(), rideListingFragment);
        fragmentTransaction.commit();*/
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

    private void setUnit() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
        this.unitX = (float)Math.pow(size.x * 0.01, UNIT_NTH_ROOT);
        this.unitY = (float)Math.pow(size.y * 0.01, UNIT_NTH_ROOT);
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public float getUnitX() {
        return unitX;
    }

    public float getUnitY() {
        return unitY;
    }
}
