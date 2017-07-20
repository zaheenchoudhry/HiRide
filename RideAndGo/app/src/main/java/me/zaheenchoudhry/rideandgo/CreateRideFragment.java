package me.zaheenchoudhry.rideandgo;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.DatePicker;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class CreateRideFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    public static int day, date, month, year, hour, minute, seats;
    public static double price, pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude;
    public static String pickupAddressFull, dropoffAddressFull, pickupAddressDisplay, dropoffAddressDisplay, pickupCity, dropoffCity;

    private boolean isTitleVisible, isTitleContainerVisible;

    private String[] dayOfMonth = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private String[] dayOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    private TextView title, expandedToolbarText1;
    private TextView dateDayText, dateDateText, dateMonthText, timeText, timeAmPmText, seatsText;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private LinearLayout titleContainer;
    private RelativeLayout dateContainer, timeContainer, createRideButton;
    private EditText pickupInput, dropoffInput, priceInput;
    private EditText searchTextPickup, searchTextDropoff;
    private ImageView addSeatButton, subtractSeatButton, menuButton;
    private PlaceAutocompleteFragment autocompleteFragmentPickup, autocompleteFragmentDropoff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isTitleVisible = false;
        isTitleContainerVisible = true;

        View view = inflater.inflate(R.layout.create_ride_fragment, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        title = (TextView) view.findViewById(R.id.main_textview_title);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        titleContainer = (LinearLayout) view.findViewById(R.id.title_container);
        pickupInput = (EditText) view.findViewById(R.id.edittext_pickup);
        dropoffInput = (EditText) view.findViewById(R.id.edittext_dropoff);
        priceInput = (EditText) view.findViewById(R.id.price_edittext);
        dateDayText = (TextView) view.findViewById(R.id.date_day);
        dateDateText = (TextView) view.findViewById(R.id.date_date);
        dateMonthText = (TextView) view.findViewById(R.id.date_month);
        timeText = (TextView) view.findViewById(R.id.time);
        timeAmPmText = (TextView) view.findViewById(R.id.time_am_pm);
        seatsText = (TextView) view.findViewById(R.id.seats);
        dateContainer = (RelativeLayout) view.findViewById(R.id.date_container);
        timeContainer = (RelativeLayout) view.findViewById(R.id.time_container);
        addSeatButton = (ImageView) view.findViewById(R.id.add_seats);
        subtractSeatButton = (ImageView) view.findViewById(R.id.subtract_seats);
        createRideButton = (RelativeLayout) view.findViewById(R.id.create_ride_button);
        menuButton = (ImageView) view.findViewById(R.id.menu_button);

        expandedToolbarText1 = (TextView)view.findViewById(R.id.create_ride_text);
        expandedToolbarText1.setText("Create a Ride");

        appBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(title, 0, View.INVISIBLE);

        final Calendar calendar = Calendar.getInstance();
        date = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        hour = 12;
        minute = 0;
        seats = 2;
        price = 10.00;
        pickupAddressFull = "";
        dropoffAddressFull = "";
        pickupAddressDisplay = "";
        dropoffAddressDisplay = "";
        pickupCity = "";
        dropoffCity = "";
        pickupLatitude = 0;
        pickupLongitude = 0;
        dropoffLatitude = 0;
        dropoffLongitude = 0;

        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month - 1, date - 1);
        int dayOfWeekNum = gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK);
        day = dayOfWeekNum;
        dateDayText.setText(dayOfWeek[dayOfWeekNum - 1]);
        dateMonthText.setText(dayOfMonth[month - 1]);
        dateDateText.setText(Integer.toString(date));
        timeText.setText("12:00");
        timeAmPmText.setText("PM");
        seatsText.setText("2");
        priceInput.setText("10.00");

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create new fragment and transaction
                RideListingFragment rideListingFragment = new RideListingFragment();
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, rideListingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        dateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int date) {
                        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, date - 1);

                        int dayOfWeekNum = gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK);
                        dateDayText.setText(dayOfWeek[dayOfWeekNum - 1]);
                        dateMonthText.setText(dayOfMonth[month]);
                        dateDateText.setText((date < 10) ? ("0" + Integer.toString(date)) : Integer.toString(date));
                        CreateRideFragment.date = date;
                        CreateRideFragment.month = month + 1;
                        CreateRideFragment.year = year;
                        CreateRideFragment.day = dayOfWeekNum;
                    }
                }, year, month - 1, date);
                datePickerDialog.show();
            }
        });

        timeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        int hourFormat12 = (hour > 12) ? (hour - 12) : hour;
                        hourFormat12 = (hourFormat12 == 0) ? 12 : hourFormat12;
                        String timeString = (hourFormat12 < 10) ? ("0" + Integer.toString(hourFormat12)) : Integer.toString(hourFormat12);
                        timeString += ":";
                        timeString += (minute < 10) ? ("0" + Integer.toString(minute)) : Integer.toString(minute);
                        timeText.setText(timeString);
                        timeAmPmText.setText((hour >= 12) ? "PM" : "AM");
                        CreateRideFragment.hour = hour;
                        CreateRideFragment.minute = minute;
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        addSeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRideFragment.seats += 1;
                CreateRideFragment.seats = (CreateRideFragment.seats > 99) ? 99 : CreateRideFragment.seats;
                seatsText.setText(Integer.toString(CreateRideFragment.seats));
                int leftMarginDp = (CreateRideFragment.seats < 10) ? -18 : -26;
                ViewGroup.MarginLayoutParams parameter = (ViewGroup.MarginLayoutParams) seatsText.getLayoutParams();
                parameter.setMargins(getPixel(leftMarginDp), parameter.topMargin, parameter.rightMargin, parameter.bottomMargin); // left, top, right, bottom
            }
        });

        subtractSeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRideFragment.seats -= 1;
                CreateRideFragment.seats = (CreateRideFragment.seats < 0) ? 0 : CreateRideFragment.seats;
                seatsText.setText(Integer.toString(CreateRideFragment.seats));
                int leftMarginDp = (CreateRideFragment.seats < 10) ? -18 : -26;
                ViewGroup.MarginLayoutParams parameter = (ViewGroup.MarginLayoutParams) seatsText.getLayoutParams();
                parameter.setMargins(getPixel(leftMarginDp), parameter.topMargin, parameter.rightMargin, parameter.bottomMargin); // left, top, right, bottom
            }
        });

        priceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceInput.setCursorVisible(true);
            }
        });

        priceInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                double priceNum = 0;
                try {
                    priceNum = Double.parseDouble(priceInput.getText().toString());
                } catch (Exception e) {
                    priceNum = 0;
                }

                priceNum = (priceNum > 999.99) ? 999.99 : priceNum;
                CreateRideFragment.price = priceNum;
                DecimalFormat df = new DecimalFormat("#.00");
                String priceString = (priceNum != 0) ? df.format(priceNum) : "0.00";
                priceInput.setText(priceString);
                priceInput.setCursorVisible(false);
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(priceInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("CA")
                .build();

        autocompleteFragmentPickup = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_pickup);
        autocompleteFragmentDropoff = (PlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_dropoff);
        autocompleteFragmentPickup.setFilter(autocompleteFilter);
        autocompleteFragmentDropoff.setFilter(autocompleteFilter);
        searchTextPickup = ((EditText)autocompleteFragmentPickup.getView().findViewById(R.id.place_autocomplete_search_input));
        searchTextDropoff = ((EditText)autocompleteFragmentDropoff.getView().findViewById(R.id.place_autocomplete_search_input));

        //ImageButton searchButtonPickup = ((ImageButton)autocompleteFragmentPickup.getView().findViewById(R.id.place_autocomplete_search_button));
        //ImageButton searchButtonDropoff = ((ImageButton)autocompleteFragmentDropoff.getView().findViewById(R.id.place_autocomplete_search_button));

        pickupInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTextPickup.callOnClick();
            }
        });

        dropoffInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTextDropoff.callOnClick();
            }
        });

        autocompleteFragmentPickup.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                pickupInput.setText(place.getAddress());
                CreateRideFragment.pickupAddressFull = place.getAddress().toString();
                CreateRideFragment.pickupAddressDisplay = place.getName().toString();
                CreateRideFragment.pickupLatitude = place.getLatLng().latitude;
                CreateRideFragment.pickupLongitude = place.getLatLng().longitude;
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        CreateRideFragment.pickupCity = addresses.get(0).getAddressLine(1).split(",")[0];
                    } else {
                        CreateRideFragment.pickupCity = "";
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Can't find city : " + e.getMessage());
                    CreateRideFragment.pickupCity = "";
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                pickupInput.setText("");
            }
        });

        autocompleteFragmentDropoff.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                dropoffInput.setText(place.getAddress());
                CreateRideFragment.dropoffAddressFull = place.getAddress().toString();
                CreateRideFragment.dropoffAddressDisplay = place.getName().toString();
                CreateRideFragment.dropoffLatitude = place.getLatLng().latitude;
                CreateRideFragment.dropoffLongitude = place.getLatLng().longitude;
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        CreateRideFragment.dropoffCity = addresses.get(0).getAddressLine(1).split(",")[0];
                    } else {
                        CreateRideFragment.dropoffCity = "";
                    }
                } catch (Exception e) {
                    Log.i(TAG, "Can't find city : " + e.getMessage());
                    CreateRideFragment.dropoffCity = "";
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                dropoffInput.setText("");
            }
        });

        createRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRideServerRequest createRideServerRequest = new CreateRideServerRequest(getActivity());
                createRideServerRequest.execute(Integer.toString(CreateRideFragment.day), Integer.toString(CreateRideFragment.date),
                        Integer.toString(CreateRideFragment.month), Integer.toString(CreateRideFragment.year),
                        Integer.toString(CreateRideFragment.hour), Integer.toString(CreateRideFragment.minute),
                        Integer.toString(CreateRideFragment.seats), Double.toString(CreateRideFragment.price),
                        pickupAddressFull, dropoffAddressFull, pickupAddressDisplay, dropoffAddressDisplay, pickupCity, dropoffCity,
                        Double.toString(CreateRideFragment.pickupLatitude), Double.toString(CreateRideFragment.pickupLongitude),
                        Double.toString(CreateRideFragment.dropoffLatitude), Double.toString(CreateRideFragment.dropoffLongitude));
            }
        });

        return view;
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
}
