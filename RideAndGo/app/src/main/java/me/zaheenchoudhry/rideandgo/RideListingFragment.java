package me.zaheenchoudhry.rideandgo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RideListingFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 400;

    private boolean isTitleVisible, isTitleContainerVisible;
    private String jsonResultString;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;
    private RideListingsAdapter rideListingsAdapter;
    private List<RidePost> ridePostList;

    private Toolbar toolbar;
    private TextView expandedToolbarText1, expandedToolbarText2;
    private TabLayout listingTabLayout;
    private TextView title;
    private LinearLayout titleContainer;
    private AppBarLayout appBarLayout;
    private RelativeLayout toolbarDivider;
    private ImageView menuButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ridePostList = new ArrayList<RidePost>();
        fetchRideListFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isTitleVisible = false;
        isTitleContainerVisible = true;

        View view = inflater.inflate(R.layout.ride_listing_fragment, container, false);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        title = (TextView) view.findViewById(R.id.main_textview_title);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        titleContainer = (LinearLayout) view.findViewById(R.id.title_container);
        listingTabLayout = (TabLayout) view.findViewById(R.id.listing_tabs);
        toolbarDivider = (RelativeLayout) view.findViewById(R.id.toolbar_divider);
        menuButton = (ImageView) view.findViewById(R.id.menu_button);

        expandedToolbarText1 = (TextView)view.findViewById(R.id.heading_text);
        expandedToolbarText1.setText("RIDE");
        expandedToolbarText2 = (TextView)view.findViewById(R.id.slogan_text);
        expandedToolbarText2.setText("Rideshare made easier");

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_ride_listing);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        rideListingsAdapter = new RideListingsAdapter(this, ridePostList);
        recyclerView.setAdapter(rideListingsAdapter);

        appBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(title, 0, View.INVISIBLE);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                CreateRideFragment createRideFragment = new CreateRideFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, createRideFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void fetchRideListFromServer() {
        GetRideListServerRequest getRideListServerRequest = new GetRideListServerRequest();
        getRideListServerRequest.execute();
    }

    private void initializeRidePostList() {
        System.out.println("INITIALIZING RIDE POST LIST");

        try {
            JSONObject jsonResponse = new JSONObject(jsonResultString);
            JSONArray rideList = jsonResponse.getJSONArray("rideList");
            for (int i = 0; i < rideList.length(); ++i) {
                try {
                    JSONObject ride = rideList.getJSONObject(i);
                    // Pulling items from the array
                    RidePost ridePost = new RidePost();
                    ridePost.setDate(ride.getInt("Day"), ride.getInt("Date"), ride.getInt("Month"), ride.getInt("Year"));
                    ridePost.setTime(ride.getInt("Hour"), ride.getInt("Minute"));
                    ridePost.setSeats(ride.getInt("SeatsTotal"), ride.getInt("SeatsBooked"));
                    ridePost.setPrice(ride.getDouble("Price"));
                    ridePost.setPickupAddress(ride.getString("PickupAddressFull"), ride.getString("PickupAddressDisplay"), ride.getString("PickupCity"));
                    ridePost.setDropoffAddress(ride.getString("DropoffAddressFull"), ride.getString("DropoffAddressDisplay"), ride.getString("DropoffCity"));
                    ridePost.setPickupAddressCoordinates(ride.getDouble("PickupLatitude"), ride.getDouble("PickupLongitude"));
                    ridePost.setDropoffAddressCoordinates(ride.getDouble("DropoffLatitude"), ride.getDouble("DropoffLongitude"));
                    System.out.println(ridePost);
                    ridePostList.add(ridePost);
                } catch (JSONException e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof RideListingFragment) {
            FragmentTransaction fragTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float)(Math.abs(verticalOffset) / maxScroll);
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!isTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(toolbarDivider, 1000, View.VISIBLE);
                isTitleVisible = true;
            }
        } else {
            if (isTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(toolbarDivider, 200, View.INVISIBLE);
                isTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                isTitleContainerVisible = false;
            }
        } else {
            if (!isTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                isTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View view, int duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE) ? new AlphaAnimation(0f, 1f) : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }

    class GetRideListServerRequest extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            String string_url = getActivity().getString(R.string.get_all_rides_list_request_url);

            try {
                URL url = new URL(string_url);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("User-Agent", "");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();

                System.out.println(result);

                return result;
            } catch (IOException e) {
                // Writing exception to log
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            jsonResultString = result;
            initializeRidePostList();
        }
    }
}
