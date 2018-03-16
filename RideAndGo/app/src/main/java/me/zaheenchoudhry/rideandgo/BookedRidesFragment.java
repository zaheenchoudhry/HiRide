package me.zaheenchoudhry.rideandgo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class BookedRidesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;
    private BookedRidesAdapter bookedRidesAdapter;
    private List<RidePost> ridePostList;
    private UpcomingRidesFragment upcomingRidesFragment;
    private String jsonResultString;

    public BookedRidesFragment(UpcomingRidesFragment upcomingRidesFragment) {
        this.upcomingRidesFragment = upcomingRidesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ridePostList = new ArrayList<RidePost>();

        if (((AppActivity)getActivity()).getUserAccount().isLoggedIn()) {
            fetchDriverRidesFromServer();
        } else {
            fetchDriverRidesFromFile();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offered_rides_fragment, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_offered_rides);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bookedRidesAdapter = new BookedRidesAdapter(ridePostList);
        recyclerView.setAdapter(bookedRidesAdapter);

        return view;
    }

    private void fetchDriverRidesFromFile() {

    }

    private void fetchDriverRidesFromServer() {
        System.out.println("FETCHING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String passengerUserId = Integer.toString(((AppActivity)getActivity()).getUserAccount().getUserId());

        GetDriverRidesServerRequest getDriverRidesServerRequest = new GetDriverRidesServerRequest();
        getDriverRidesServerRequest.execute(passengerUserId);
    }

    private void initializeRidePostList(String jsonResultString) {
        System.out.println("INITIALIZING !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        try {
            JSONObject jsonResponse = new JSONObject(jsonResultString);
            JSONArray rideList = jsonResponse.getJSONArray("bookedRidesList");
            for (int i = 0; i < rideList.length(); ++i) {
                try {
                    JSONObject ride = rideList.getJSONObject(i);
                    // Pulling items from the array
                    RidePost ridePost = new RidePost();
                    ridePost.setRideAndOwnerId(ride.getInt("RideId"), ((AppActivity)getActivity()).getUserAccount().getUserId());
                    ridePost.setDate(ride.getInt("Day"), ride.getInt("Date"), ride.getInt("Month"), ride.getInt("Year"));
                    ridePost.setTime(ride.getInt("Hour"), ride.getInt("Minute"));
                    ridePost.setSeats(ride.getInt("SeatsTotal"), ride.getInt("SeatsBooked"));
                    ridePost.setPrice(ride.getDouble("Price"));
                    ridePost.setPickupAddress(ride.getString("PickupAddressFull"), ride.getString("PickupAddressDisplay"), ride.getString("PickupCity"));
                    ridePost.setDropoffAddress(ride.getString("DropoffAddressFull"), ride.getString("DropoffAddressDisplay"), ride.getString("DropoffCity"));
                    ridePost.setPickupAddressCoordinates(ride.getDouble("PickupLatitude"), ride.getDouble("PickupLongitude"));
                    ridePost.setDropoffAddressCoordinates(ride.getDouble("DropoffLatitude"), ride.getDouble("DropoffLongitude"));
                    ridePostList.add(ridePost);
                } catch (JSONException e) {
                    // Oops
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getActivity().getSupportFragmentManager().beginTransaction().detach(upcomingRidesFragment).commitNowAllowingStateLoss();
        getActivity().getSupportFragmentManager().beginTransaction().attach(upcomingRidesFragment).commitAllowingStateLoss();

        /*Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.upcoming_rides_viewpager);
        if (currentFragment instanceof OfferedRidesFragment) {
            System.out.println("IT IS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            FragmentTransaction fragTransaction = getChildFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }*/
    }

    class GetDriverRidesServerRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... args) {
            String passengerUserId = args[0];
            String result = "";
            String string_url = getActivity().getString(R.string.get_booking_list_request_url);


            string_url += "?passengerUserId=" + passengerUserId;

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
            if (result == null) {
                // display something went wrong
            } else if (result.equals("")) {
                // display nothing found
            } else {
                initializeRidePostList(result);
            }
        }
    }
}