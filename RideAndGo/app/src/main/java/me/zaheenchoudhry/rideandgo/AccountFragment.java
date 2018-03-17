package me.zaheenchoudhry.rideandgo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.DatePicker;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.lang.String;

import static android.content.ContentValues.TAG;

public class AccountFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean isTitleVisible, isTitleContainerVisible;

    private float screenX, screenY;
    private float underlineWidth, underlineHeight;

    private ImageView menuButton;
    private ImageView profilePicture;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private LinearLayout titleContainer;
    private TextView title, trips, km, riders, checkoutfb;

    private Connection conn;
    private Statement stmt;
    private String query;
    private ResultSet rs;

    private UserAccount requestedUserAccount;
    private String jsonResultString;

    public AccountFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isTitleVisible = false;
        isTitleContainerVisible = true;
        underlineWidth = 0;
        underlineHeight = 0;

        View view = inflater.inflate(R.layout.account_fragment, container, false);
        setUnit();

        profilePicture = (ImageView)view.findViewById(R.id.profile_picture);

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        title = (TextView)view.findViewById(R.id.main_textview_title);

        appBarLayout = (AppBarLayout)view.findViewById(R.id.appbar);
        titleContainer = (LinearLayout)view.findViewById(R.id.title_container);

        trips = (TextView)view.findViewById(R.id.num_of_trips);
        km = (TextView)view.findViewById(R.id.num_of_km);
        riders = (TextView)view.findViewById(R.id.num_of_riders);

        checkoutfb = (TextView)view.findViewById(R.id.fbbutton);

        requestedUserAccount = ((AppActivity)getActivity()).getUserAccount();
        GetUserServerRequest getUserServerRequest = new GetUserServerRequest(requestedUserAccount.getUserId());
        getUserServerRequest.execute();


        return view;
    }

    public void initializeProfileView() {
        SetProfileImageAsyncTask setProfileImageAsyncTask = new SetProfileImageAsyncTask(profilePicture);
        setProfileImageAsyncTask.execute(requestedUserAccount.getFacebookProfilePicURI());

        title.setText(requestedUserAccount.getName());


        System.out.println(requestedUserAccount.getTrips());
        trips.setText(Integer.toString(requestedUserAccount.getTrips()));
        km.setText(Integer.toString(requestedUserAccount.getKm()));
        riders.setText(Integer.toString(requestedUserAccount.getRiders()));

        checkoutfb.setText("Checkout "+requestedUserAccount.getName()+"'s Facebook Profile");
    }

    private void initializeToolbarAndMenuButton() {
        appBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(title, 0, View.INVISIBLE);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {((AppActivity)getActivity()).openMenu();}
        });
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
                isTitleVisible = true;
            }
        } else {
            if (isTitleVisible) {
                startAlphaAnimation(title, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
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

    private int getPixel(int dp) {
        Resources resources = getActivity().getResources();
        int pixel = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
        return pixel;
    }

    private void setUnit() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
    }

    class GetUserServerRequest extends AsyncTask<Void, Void, String> {
        int userId;

        public GetUserServerRequest(int userId) {
            this.userId = userId;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            String string_url = "http://35.185.15.124/getUser.php";


            string_url += "?UserId=" + String.valueOf(userId);

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
            try {
                JSONObject jsonResponse = new JSONObject(result);
                JSONArray user = jsonResponse.getJSONArray("user");

                JSONObject driverArray = user.getJSONObject(0);
                // requestedUserAccount = new UserAccount(driverArray.getInt("UserId"), driverArray.getInt("AccountType"), driverArray.getString("Name"), driverArray.getString("PhoneNumber"));
                requestedUserAccount.setFacebookAccountNumber(driverArray.getString("FacebookAccountNumber"));
                requestedUserAccount.setFacebookProfileLinkURI(driverArray.getString("FacebookProfileLinkURI"));
                requestedUserAccount.setFacebookProfilePicURI(driverArray.getString("FacebookProfilePicURI"));
                requestedUserAccount.setAcceptsCash(driverArray.getInt("AcceptsCash"));
                requestedUserAccount.setAcceptsInAppPayments(driverArray.getInt("AcceptsInAppPayments"));
                requestedUserAccount.setTrips(driverArray.getInt("NumOfTrips"));
                driverArray.getInt("NumOfTrips");
                requestedUserAccount.setKm(driverArray.getInt("NumOfKm"));
                requestedUserAccount.setRiders(driverArray.getInt("NumOfRiders"));
                // CALL ANY FUNCTIONS THAT USE requestedUserAccount HERE

                initializeProfileView();

            } catch (JSONException e) {
                System.out.println(e);
            }
        }
    }

}
