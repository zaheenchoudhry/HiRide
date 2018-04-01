package me.zaheenchoudhry.rideandgo;

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
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

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

    private float screenX, screenY;
    private float underlineWidth, underlineHeight;
    private boolean acceptsCash, acceptsInAppPayments;
    private boolean[] prefersPreference;
    private int[] preferenceIconResourceWhite;
    private int[] preferenceIconResourceBlue;

    private ImageView menuButton;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private LinearLayout titleContainer;
    private TextView title, pickupDropoffTitleText;
    private RelativeLayout locationsContainer, pickupEdittextHolder, pickupEdittextButton, autocompleteFragmentPickupContainer;
    private RelativeLayout dropoffEdittextHolder, dropoffEdittextButton, autocompleteFragmentDropoffContainer;
    private RelativeLayout dateTimeContainer, dateContainer, timeContainer, dateUnderline, timeUnderline;
    private RelativeLayout seatsPriceContainer, seatsContainer, priceContainer, seatsUnderline, priceUnderline;
    private RelativeLayout seatsTextButtonsContainer, dateTextContainer, timeTextContainer, priceTextContainer;
    private RelativeLayout paymentMethodsContainer, paymentMethodsTop, paymentMethodsBottom, paymentMethodsDivider, cashMethodHolder, inappMethodHolder;
    private RelativeLayout preferenceHolder, driverPrefsContainer, createRideButtonContainer;
    private Button createRideButton;
    private TextView dateDayText, dateDateText, dateMonthText, dateTitle, timeTitle, seatsTitle, priceTitle;
    private TextView timeText, timeAmPmText, seatsText, priceSignText, pricePerSeatText, driverPreferencesTitle;
    private TextView paymentMethodsTitleText, cashMethodText, inappMethodText;
    private ImageView addSeatButton, subtractSeatButton, cashMethodIcon, inappMethodIcon;
    private EditText pickupInput, dropoffInput, searchTextPickup, searchTextDropoff, priceInput;
    private ImageView pickupEdittextIcon, pickupEdittextButtonIcon, dropoffEdittextIcon, dropoffEdittextButtonIcon;
    private SupportPlaceAutocompleteFragment autocompleteFragmentPickup, autocompleteFragmentDropoff;
    private RelativeLayout[] preferences, preferencesIconsHolders;
    private ImageView[] preferencesIcons;
    private TextView[] preferencesTexts, preferencesNoTexts;

    private String phoneNumber;

    private RidePost ridePost;

    public CreateRideFragment() {
        this(null);
    }

    public CreateRideFragment(RidePost ridePost) {
        this.ridePost = ridePost;
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

        prefersPreference = new boolean[UserAccount.NUM_OF_PREFERENCES];
        UserAccount userAccount = ((AppActivity)getActivity()).getUserAccount();
        if (userAccount.isLoggedIn()) {
            this.acceptsCash = userAccount.doesAcceptCash();
            this.acceptsInAppPayments = userAccount.doesAcceptInAppPayments();
            this.prefersPreference[0] = userAccount.doesPreferMusic();
            this.prefersPreference[1] = userAccount.doesPreferDrinks();
            this.prefersPreference[2] = userAccount.doesPreferExtraLuggage();
            this.prefersPreference[3] = userAccount.doesPreferPets();
            this.prefersPreference[4] = userAccount.doesPreferGender();
        } else {
            this.acceptsCash = true;
            this.acceptsInAppPayments = true;
            this.prefersPreference[0] = true;
            this.prefersPreference[1] = false;
            this.prefersPreference[2] = false;
            this.prefersPreference[3] = false;
            this.prefersPreference[4] = true;
        }

        preferenceIconResourceWhite = new int[UserAccount.NUM_OF_PREFERENCES];
        preferenceIconResourceBlue = new int[UserAccount.NUM_OF_PREFERENCES];
        preferenceIconResourceWhite[0] = R.drawable.music_icon_white;
        preferenceIconResourceWhite[1] = R.drawable.drinks_icon_white;
        preferenceIconResourceWhite[2] = R.drawable.luggage_icon_white;
        preferenceIconResourceWhite[3] = R.drawable.pet_icon_white;
        preferenceIconResourceWhite[4] = R.drawable.male_icon_white;
        preferenceIconResourceBlue[0] = R.drawable.music_icon_blue;
        preferenceIconResourceBlue[1] = R.drawable.drinks_icon_blue;
        preferenceIconResourceBlue[2] = R.drawable.luggage_icon_blue;
        preferenceIconResourceBlue[3] = R.drawable.pet_icon_blue;
        preferenceIconResourceBlue[4] = R.drawable.male_icon_blue;

        View view = inflater.inflate(R.layout.create_ride_fragment, container, false);
        setUnit();

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        title = (TextView)view.findViewById(R.id.main_textview_title);
        appBarLayout = (AppBarLayout)view.findViewById(R.id.appbar);
        titleContainer = (LinearLayout)view.findViewById(R.id.title_container);
        menuButton = (ImageView) view.findViewById(R.id.create_ride_menu_button);

        locationsContainer = (RelativeLayout)view.findViewById(R.id.pickup_dropoff_locations_container);
        pickupDropoffTitleText = (TextView)view.findViewById(R.id.pickup_dropoff_title_text);

        pickupInput = (EditText)view.findViewById(R.id.edittext_pickup);
        pickupEdittextHolder = (RelativeLayout)view.findViewById(R.id.edittext_pickup_holder);
        pickupEdittextButton = (RelativeLayout)view.findViewById(R.id.edittext_pickup_button);
        pickupEdittextIcon = (ImageView)view.findViewById(R.id.edittext_pickup_icon);
        pickupEdittextButtonIcon = (ImageView)view.findViewById(R.id.edittext_pickup_button_icon);
        autocompleteFragmentPickupContainer = (RelativeLayout)view.findViewById(R.id.place_autocomplete_fragment_pickup_container);
        autocompleteFragmentPickup = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_pickup);
        searchTextPickup = ((EditText)autocompleteFragmentPickup.getView().findViewById(R.id.place_autocomplete_search_input));

        dropoffInput = (EditText)view.findViewById(R.id.edittext_dropoff);
        dropoffEdittextHolder = (RelativeLayout)view.findViewById(R.id.edittext_dropoff_holder);
        dropoffEdittextButton = (RelativeLayout)view.findViewById(R.id.edittext_dropoff_button);
        dropoffEdittextIcon = (ImageView)view.findViewById(R.id.edittext_dropoff_icon);
        dropoffEdittextButtonIcon = (ImageView)view.findViewById(R.id.edittext_dropoff_button_icon);
        autocompleteFragmentDropoffContainer = (RelativeLayout)view.findViewById(R.id.place_autocomplete_fragment_dropoff_container);
        autocompleteFragmentDropoff = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_dropoff);
        searchTextDropoff = ((EditText)autocompleteFragmentDropoff.getView().findViewById(R.id.place_autocomplete_search_input));

        dateTimeContainer = (RelativeLayout)view.findViewById(R.id.date_time_container);
        seatsPriceContainer = (RelativeLayout)view.findViewById(R.id.seats_price_container);

        dateContainer = (RelativeLayout)view.findViewById(R.id.date_container);
        dateDayText = (TextView)view.findViewById(R.id.create_ride_date_day);
        dateDateText = (TextView)view.findViewById(R.id.create_ride_date_date);
        dateMonthText = (TextView)view.findViewById(R.id.create_ride_date_month);
        dateTitle = (TextView)view.findViewById(R.id.date_title);
        dateUnderline = (RelativeLayout)view.findViewById(R.id.date_underline);
        dateTextContainer = (RelativeLayout)view.findViewById(R.id.create_ride_date_text_container);

        timeContainer = (RelativeLayout)view.findViewById(R.id.time_container);
        timeTitle = (TextView)view.findViewById(R.id.time_title);
        timeUnderline = (RelativeLayout)view.findViewById(R.id.time_underline);
        timeTextContainer = (RelativeLayout)view.findViewById(R.id.time_text_container);
        timeText = (TextView)view.findViewById(R.id.time);
        timeAmPmText = (TextView)view.findViewById(R.id.time_am_pm);

        seatsContainer = (RelativeLayout)view.findViewById(R.id.seats_container);
        seatsTitle = (TextView)view.findViewById(R.id.seats_title);
        seatsUnderline = (RelativeLayout)view.findViewById(R.id.seats_underline);
        seatsTextButtonsContainer = (RelativeLayout)view.findViewById(R.id.seats_text_buttons_container);
        seatsText = (TextView)view.findViewById(R.id.seats);
        addSeatButton = (ImageView)view.findViewById(R.id.add_seats);
        subtractSeatButton = (ImageView)view.findViewById(R.id.subtract_seats);

        priceContainer = (RelativeLayout)view.findViewById(R.id.price_container);
        priceTitle = (TextView)view.findViewById(R.id.price_title);
        priceUnderline = (RelativeLayout)view.findViewById(R.id.price_underline);
        priceTextContainer = (RelativeLayout)view.findViewById(R.id.price_text_container);
        priceSignText = (TextView)view.findViewById(R.id.price_sign_text);
        priceInput = (EditText)view.findViewById(R.id.price_edittext);
        pricePerSeatText = (TextView)view.findViewById(R.id.price_per_seat_text);

        paymentMethodsContainer = (RelativeLayout)view.findViewById(R.id.create_ride_payment_methods_container);
        paymentMethodsTop = (RelativeLayout)view.findViewById(R.id.create_ride_payment_methods_top);
        paymentMethodsBottom = (RelativeLayout)view.findViewById(R.id.create_ride_payment_methods_bottom);
        paymentMethodsDivider = (RelativeLayout)view.findViewById(R.id.create_ride_payment_methods_divider);
        cashMethodHolder = (RelativeLayout)view.findViewById(R.id.create_ride_cash_method_holder);
        inappMethodHolder = (RelativeLayout)view.findViewById(R.id.create_ride_inapp_method_holder);
        paymentMethodsTitleText = (TextView)view.findViewById(R.id.create_ride_payment_methods_text);
        cashMethodText = (TextView)view.findViewById(R.id.create_ride_cash_method_text);
        inappMethodText = (TextView)view.findViewById(R.id.create_ride_inapp_method_text);
        cashMethodIcon = (ImageView)view.findViewById(R.id.create_ride_cash_method_icon);
        inappMethodIcon = (ImageView)view.findViewById(R.id.create_ride_inapp_method_icon);

        preferences = new RelativeLayout[UserAccount.NUM_OF_PREFERENCES];
        preferencesIcons = new ImageView[UserAccount.NUM_OF_PREFERENCES];
        preferencesTexts = new TextView[UserAccount.NUM_OF_PREFERENCES];
        preferencesIconsHolders = new RelativeLayout[UserAccount.NUM_OF_PREFERENCES];
        preferencesNoTexts = new TextView[UserAccount.NUM_OF_PREFERENCES];

        driverPrefsContainer = (RelativeLayout)view.findViewById(R.id.create_ride_driver_prefs_container);
        driverPreferencesTitle = (TextView)view.findViewById(R.id.create_ride_driver_prefs_title);
        preferenceHolder = (RelativeLayout)view.findViewById(R.id.create_ride_driver_prefs_holder);
        preferences[0] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_music_holder);
        preferences[1] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_drinks_holder);
        preferences[2] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_luggage_holder);
        preferences[3] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_pets_holder);
        preferences[4] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_gender_holder);
        preferencesIcons[0] = (ImageView)view.findViewById(R.id.create_ride_prefs_music_icon);
        preferencesIcons[1] = (ImageView)view.findViewById(R.id.create_ride_prefs_drinks_icon);
        preferencesIcons[2] = (ImageView)view.findViewById(R.id.create_ride_prefs_luggage_icon);
        preferencesIcons[3] = (ImageView)view.findViewById(R.id.create_ride_prefs_pets_icon);
        preferencesIcons[4] = (ImageView)view.findViewById(R.id.create_ride_prefs_gender_icon);
        preferencesTexts[0] = (TextView)view.findViewById(R.id.create_ride_prefs_music_text);
        preferencesTexts[1] = (TextView)view.findViewById(R.id.create_ride_prefs_drinks_text);
        preferencesTexts[2] = (TextView)view.findViewById(R.id.create_ride_prefs_luggage_text);
        preferencesTexts[3] = (TextView)view.findViewById(R.id.create_ride_prefs_pets_text);
        preferencesTexts[4] = (TextView)view.findViewById(R.id.create_ride_prefs_gender_text);
        preferencesIconsHolders[0] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_music_icon_holder);
        preferencesIconsHolders[1] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_drinks_icon_holder);
        preferencesIconsHolders[2] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_luggage_icon_holder);
        preferencesIconsHolders[3] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_pets_icon_holder);
        preferencesIconsHolders[4] = (RelativeLayout)view.findViewById(R.id.create_ride_prefs_gender_icon_holder);
        preferencesNoTexts[0] = (TextView)view.findViewById(R.id.create_ride_prefs_music_no_text);
        preferencesNoTexts[1] = (TextView)view.findViewById(R.id.create_ride_prefs_drinks_no_text);
        preferencesNoTexts[2] = (TextView)view.findViewById(R.id.create_ride_prefs_luggage_no_text);
        preferencesNoTexts[3] = (TextView)view.findViewById(R.id.create_ride_prefs_pets_no_text);
        preferencesNoTexts[4] = (TextView)view.findViewById(R.id.create_ride_prefs_gender_no_text);

        createRideButtonContainer = (RelativeLayout)view.findViewById(R.id.create_ride_button_container);
        createRideButton = (Button)view.findViewById(R.id.create_ride_button);

        initializeValues();
        initializeToolbarAndMenuButton();
        initializeLocationsContainer();
        initializePickupEdittext();
        initializeDropoffEdittext();
        initializeSearchAutocomplete();
        initializeDateTimeSeatsPriceContainers();
        initializeDateContainer();
        initializeTimeContainer();
        initializeSeatsContainer();
        initializePriceContainer();
        initializePaymentMethodsContainer();
        initializeDriverPreferences();
        initializeCreateRideButton();

        return view;
    }

    private void initializeValues() {
        if (ridePost == null) {
            final Calendar calendar = Calendar.getInstance();
            date = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            hour = 12;
            minute = 0;
            seats = 3;
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
            day = gregorianCalendar.get(GregorianCalendar.DAY_OF_WEEK);
            dateDayText.setText(dayOfWeek[day - 1]);
            dateMonthText.setText(dayOfMonth[month - 1]);
            dateDateText.setText(Integer.toString(date));
            timeText.setText("12:00");
            timeAmPmText.setText("PM");
            seatsText.setText("3");
            priceInput.setText("10.00");
        } else {
            date = ridePost.getDate();
            month = ridePost.getMonth();
            year = ridePost.getYear();
            hour = ridePost.getHour();
            minute = ridePost.getMinute();
            seats = ridePost.getSeatsTotal();
            price = ridePost.getPrice();
            pickupAddressFull = ridePost.getPickupAddressFull();
            dropoffAddressFull = ridePost.getDropoffAddressFull();
            pickupAddressDisplay = ridePost.getPickupAddressDisplay();
            dropoffAddressDisplay = ridePost.getDropoffAddressDisplay();
            pickupCity = ridePost.getPickupCity();
            dropoffCity = ridePost.getDropoffCity();
            pickupLatitude = ridePost.getPickupLatitude();
            pickupLongitude = ridePost.getPickupLongitude();
            dropoffLatitude = ridePost.getDropoffLatitude();
            dropoffLongitude = ridePost.getDropoffLongitude();

            int hourFormat12 = (hour > 12) ? (hour - 12) : hour;
            hourFormat12 = (hourFormat12 == 0) ? 12 : hourFormat12;
            String timeString = Integer.toString(hourFormat12) + ":";
            timeString += (minute < 10) ? ("0" + Integer.toString(minute)) : Integer.toString(minute);
            timeText.setText(timeString);
            timeAmPmText.setText((hour >= 12) ? "PM" : "AM");

            DecimalFormat df = new DecimalFormat("#.00");
            String priceString = (price != 0) ? df.format(price) : "0.00";
            priceInput.setText(priceString);

            day = ridePost.getDay();
            dateDayText.setText(dayOfWeek[day - 1]);
            dateMonthText.setText(dayOfMonth[month - 1]);
            dateDateText.setText(Integer.toString(date));
            seatsText.setText(Integer.toString(seats));

            pickupInput.setText(pickupAddressDisplay);
            dropoffInput.setText(dropoffAddressDisplay);
        }
    }

    private void initializeToolbarAndMenuButton() {
        appBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(title, 0, View.INVISIBLE);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppActivity)getActivity()).openMenu();
            }
        });
    }

    private void initializeLocationsContainer() {
        RelativeLayout.LayoutParams locationsContainerParams = (RelativeLayout.LayoutParams)locationsContainer.getLayoutParams();
        locationsContainerParams.width = (int)(screenX * 0.96f);
        locationsContainerParams.topMargin = (int)(screenX * 0.02f);
        locationsContainer.setPadding(0, 0, 0, (int)(screenY * 0.04f));

        RelativeLayout.LayoutParams pickupDropoffTitleTextParams = (RelativeLayout.LayoutParams)pickupDropoffTitleText.getLayoutParams();
        pickupDropoffTitleTextParams.topMargin = (int)(screenY * 0.03f);
        pickupDropoffTitleText.setTextSize((int)(screenX * 0.011f));
    }

    private void initializePickupEdittext() {
        RelativeLayout.LayoutParams pickupEdittextHolderParams = (RelativeLayout.LayoutParams)pickupEdittextHolder.getLayoutParams();
        pickupEdittextHolderParams.topMargin = (int)(screenY * 0.03f);

        RelativeLayout.LayoutParams pickupEdittextIconParams = (RelativeLayout.LayoutParams)pickupEdittextIcon.getLayoutParams();
        pickupEdittextIconParams.width = (int)(screenX * 0.11f);
        pickupEdittextIconParams.height = (int)(screenX * 0.11f);
        pickupEdittextIcon.setPadding((int)(screenX * 0.025f), 0, 0, 0);
        pickupInput.setWidth((int)(screenX * 0.84f));
        pickupInput.setHeight((int)(screenY * 0.08f));
        pickupInput.setPadding((int)(screenX * 0.125f), 0, (int)(screenY * 0.1f), 0);

        RelativeLayout.LayoutParams pickupEdittextButtonParams = (RelativeLayout.LayoutParams)pickupEdittextButton.getLayoutParams();
        pickupEdittextButtonParams.width = (int)(screenY * 0.08f);
        pickupEdittextButtonParams.height = (int)(screenY * 0.08f);
        pickupEdittextButtonParams.setMarginStart(-(int)(screenY * 0.08f));

        RelativeLayout.LayoutParams pickupEdittextButtonIconParams = (RelativeLayout.LayoutParams)pickupEdittextButtonIcon.getLayoutParams();
        pickupEdittextButtonIconParams.width = (int)((screenY * 0.08f) * 0.6f);
        pickupEdittextButtonIconParams.height = (int)((int)(screenY * 0.08f) * 0.6f);

        RelativeLayout.LayoutParams autocompleteFragmentPickupContainerParams = (RelativeLayout.LayoutParams)autocompleteFragmentPickupContainer.getLayoutParams();
        autocompleteFragmentPickupContainerParams.width = (int)(screenX * 0.84f);
        autocompleteFragmentPickupContainerParams.height = (int)(screenY * 0.08f);

        pickupInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTextPickup.callOnClick();
            }
        });
    }

    private void initializeDropoffEdittext() {
        RelativeLayout.LayoutParams dropoffEdittextHolderParams = (RelativeLayout.LayoutParams)dropoffEdittextHolder.getLayoutParams();
        dropoffEdittextHolderParams.topMargin = (int)(screenY * 0.025f);

        RelativeLayout.LayoutParams dropoffEdittextIconParams = (RelativeLayout.LayoutParams)dropoffEdittextIcon.getLayoutParams();
        dropoffEdittextIconParams.width = (int)(screenX * 0.11f);
        dropoffEdittextIconParams.height = (int)(screenX * 0.11f);
        dropoffEdittextIcon.setPadding((int)(screenX * 0.025f), 0, 0, 0);
        dropoffInput.setWidth((int)(screenX * 0.84f));
        dropoffInput.setHeight((int)(screenY * 0.08f));
        dropoffInput.setPadding((int)(screenX * 0.125f), 0, (int)(screenY * 0.1f), 0);

        RelativeLayout.LayoutParams dropoffEdittextButtonParams = (RelativeLayout.LayoutParams)dropoffEdittextButton.getLayoutParams();
        dropoffEdittextButtonParams.width = (int)(screenY * 0.08f);
        dropoffEdittextButtonParams.height = (int)(screenY * 0.08f);
        dropoffEdittextButtonParams.setMarginStart(-(int)(screenY * 0.08f));

        RelativeLayout.LayoutParams dropoffEdittextButtonIconParams = (RelativeLayout.LayoutParams)dropoffEdittextButtonIcon.getLayoutParams();
        dropoffEdittextButtonIconParams.width = (int)((screenY * 0.08f) * 0.6f);
        dropoffEdittextButtonIconParams.height = (int)((int)(screenY * 0.08f) * 0.6f);

        RelativeLayout.LayoutParams autocompleteFragmentDropoffContainerParams = (RelativeLayout.LayoutParams)autocompleteFragmentDropoffContainer.getLayoutParams();
        autocompleteFragmentDropoffContainerParams.width = (int)(screenX * 0.84f);
        autocompleteFragmentDropoffContainerParams.height = (int)(screenY * 0.08f);

        dropoffInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTextDropoff.callOnClick();
            }
        });
    }

    private void initializeSearchAutocomplete() {
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("CA")
                .build();

        autocompleteFragmentPickup.setFilter(autocompleteFilter);
        autocompleteFragmentDropoff.setFilter(autocompleteFilter);

        //ImageButton searchButtonPickup = ((ImageButton)autocompleteFragmentPickup.getView().findViewById(R.id.place_autocomplete_search_button));
        //ImageButton searchButtonDropoff = ((ImageButton)autocompleteFragmentDropoff.getView().findViewById(R.id.place_autocomplete_search_button));

        autocompleteFragmentPickup.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                pickupInput.setText(place.getName());
                CreateRideFragment.pickupAddressFull = place.getAddress().toString();
                CreateRideFragment.pickupAddressDisplay = place.getName().toString();
                CreateRideFragment.pickupLatitude = place.getLatLng().latitude;
                CreateRideFragment.pickupLongitude = place.getLatLng().longitude;
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        String city = addresses.get(0).getLocality();
                        if (city == null || city.equals("")) {
                            city = addresses.get(0).getAddressLine(0).split(", ")[1];
                        }
                        CreateRideFragment.pickupCity = city;
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
                dropoffInput.setText(place.getName());
                CreateRideFragment.dropoffAddressFull = place.getAddress().toString();
                CreateRideFragment.dropoffAddressDisplay = place.getName().toString();
                CreateRideFragment.dropoffLatitude = place.getLatLng().latitude;
                CreateRideFragment.dropoffLongitude = place.getLatLng().longitude;
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        String city = addresses.get(0).getLocality();
                        if (city == null || city.equals("") || city.equals(" ")) {
                            city = addresses.get(0).getAddressLine(0).split(", ")[1];
                        }
                        CreateRideFragment.dropoffCity = city;
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
    }

    private void initializeDateTimeSeatsPriceContainers() {
        RelativeLayout.LayoutParams dateTimeContainerParams = (RelativeLayout.LayoutParams)dateTimeContainer.getLayoutParams();
        dateTimeContainerParams.topMargin = (int)(screenX * 0.015f);

        RelativeLayout.LayoutParams seatsPriceContainerParams = (RelativeLayout.LayoutParams)seatsPriceContainer.getLayoutParams();
        seatsPriceContainerParams.topMargin = (int)(screenX * 0.015f);
    }

    private void initializeDateContainer() {
        RelativeLayout.LayoutParams dateContainerParams = (RelativeLayout.LayoutParams)dateContainer.getLayoutParams();
        dateContainerParams.width = (int)(screenX * 0.4725f);
        dateContainer.setPadding(0, 0, 0, (int)(screenY * 0.045f));

        RelativeLayout.LayoutParams dateTitleParams = (RelativeLayout.LayoutParams)dateTitle.getLayoutParams();
        dateTitleParams.topMargin = (int)(screenY * 0.025f);
        dateTitle.setTextSize((int)(screenX * 0.013f));

        Rect dateTitleBounds = new Rect();
        Paint dateTitlePaint = dateTitle.getPaint();
        dateTitlePaint.getTextBounds(dateTitle.getText().toString(), 0, dateTitle.getText().toString().length(), dateTitleBounds);
        underlineWidth = dateTitleBounds.width();
        underlineHeight = dateTitleBounds.height();

        RelativeLayout.LayoutParams dateUnderlineParams = (RelativeLayout.LayoutParams)dateUnderline.getLayoutParams();
        dateUnderlineParams.width = (int)(underlineWidth * 0.7f);
        dateUnderlineParams.height = (int)(underlineHeight * 0.2f);
        dateUnderlineParams.topMargin = (int)(underlineHeight * 0.2f);


        RelativeLayout.LayoutParams dateTextContainerParams = (RelativeLayout.LayoutParams)dateTextContainer.getLayoutParams();
        RelativeLayout.LayoutParams dateDateTextParams = (RelativeLayout.LayoutParams)dateDateText.getLayoutParams();
        RelativeLayout.LayoutParams dateMonthTextParams = (RelativeLayout.LayoutParams)dateMonthText.getLayoutParams();
        dateTextContainerParams.topMargin = (int)(screenY * 0.03f);
        dateDayText.setTextSize(screenX * 0.009f);
        dateDateText.setTextSize(screenX * 0.0195f);
        dateMonthText.setTextSize(screenX * 0.009f);

        Rect dateDayTextBounds = new Rect();
        Paint dateDayTextPaint = dateDayText.getPaint();
        dateDayTextPaint.getTextBounds(dateDayText.getText().toString(), 0, dateDayText.getText().toString().length(), dateDayTextBounds);

        Rect dateDateTextBounds = new Rect();
        Paint dateDateTextPaint = dateDateText.getPaint();
        dateDateTextPaint.getTextBounds(dateDateText.getText().toString(), 0, dateDateText.getText().toString().length(), dateDateTextBounds);

        dateDateTextParams.topMargin = (int)(dateDayTextBounds.height() * 1.4f);
        dateMonthTextParams.topMargin = (int)(dateDateTextBounds.height() + dateDayTextBounds.height() + dateDateTextBounds.height() * 0.85f);

        dateContainer.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
    }

    private void initializeTimeContainer() {
        RelativeLayout.LayoutParams timeContainerParams = (RelativeLayout.LayoutParams)timeContainer.getLayoutParams();
        timeContainerParams.width = (int)(screenX * 0.4725f);
        timeContainerParams.height = dateContainer.getMeasuredHeight();
        timeContainerParams.leftMargin = (int)(screenX * 0.015f);

        RelativeLayout.LayoutParams timeTitleParams = (RelativeLayout.LayoutParams)timeTitle.getLayoutParams();
        timeTitleParams.topMargin = (int)(screenY * 0.025f);
        timeTitle.setTextSize((int)(screenX * 0.013f));

        RelativeLayout.LayoutParams timeUnderlineParams = (RelativeLayout.LayoutParams)timeUnderline.getLayoutParams();
        timeUnderlineParams.width = (int)(underlineWidth * 0.7f);
        timeUnderlineParams.height = (int)(underlineHeight * 0.2f);
        timeUnderlineParams.topMargin = (int)(underlineHeight * 0.2f);


        RelativeLayout.LayoutParams timeTextContainerParams = (RelativeLayout.LayoutParams)timeTextContainer.getLayoutParams();
        RelativeLayout.LayoutParams timeAmPmTextParams = (RelativeLayout.LayoutParams)timeAmPmText.getLayoutParams();
        timeTextContainerParams.topMargin = (int)(dateContainer.getMeasuredHeight() * 0.52f);
        timeText.setTextSize((int)(screenX * 0.014f));
        timeAmPmText.setTextSize((int)(screenX * 0.014f));
        timeAmPmTextParams.setMarginStart((int)(screenX * 0.015f));

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
    }

    private void initializeSeatsContainer() {
        RelativeLayout.LayoutParams seatsContainerParams = (RelativeLayout.LayoutParams)seatsContainer.getLayoutParams();
        seatsContainerParams.width = (int)(screenX * 0.4725f);
        seatsContainerParams.height = dateContainer.getMeasuredHeight();

        RelativeLayout.LayoutParams seatsTitleParams = (RelativeLayout.LayoutParams)seatsTitle.getLayoutParams();
        seatsTitleParams.topMargin = (int)(screenY * 0.025f);
        seatsTitle.setTextSize((int)(screenX * 0.013f));

        RelativeLayout.LayoutParams seatsUnderlineParams = (RelativeLayout.LayoutParams)seatsUnderline.getLayoutParams();
        seatsUnderlineParams.width = (int)(underlineWidth * 0.7f);
        seatsUnderlineParams.height = (int)(underlineHeight * 0.2f);
        seatsUnderlineParams.topMargin = (int)(underlineHeight * 0.2f);


        RelativeLayout.LayoutParams seatsTextButtonsContainerParams = (RelativeLayout.LayoutParams)seatsTextButtonsContainer.getLayoutParams();
        seatsTextButtonsContainerParams.topMargin = (int)(dateContainer.getMeasuredHeight() * 0.5f);
        seatsTextButtonsContainerParams.width = (int)(screenX * 0.4725f);
        seatsText.setTextSize((int)(screenX * 0.0185f));

        Rect seatsBounds = new Rect();
        Paint seatsPaint = seatsText.getPaint();
        seatsPaint.getTextBounds(seatsText.getText().toString(), 0, seatsText.getText().toString().length(), seatsBounds);

        RelativeLayout.LayoutParams subtractSeatButtonParams = (RelativeLayout.LayoutParams)subtractSeatButton.getLayoutParams();
        RelativeLayout.LayoutParams addSeatButtonParams = (RelativeLayout.LayoutParams)addSeatButton.getLayoutParams();
        subtractSeatButtonParams.width = (int)(seatsBounds.height() * 1.4f);
        subtractSeatButtonParams.height = (int)(seatsBounds.height() * 1.4f);
        subtractSeatButtonParams.setMarginStart((int)((screenX * 0.4725f) * 0.25f - (seatsBounds.height() * 1.4f) / 2));
        addSeatButtonParams.width = (int)(seatsBounds.height() * 1.4f);
        addSeatButtonParams.height = (int)(seatsBounds.height() * 1.4f);
        addSeatButtonParams.setMarginStart((int)((screenX * 0.4725f) * 0.75f - (seatsBounds.height() * 1.4f) / 2));

        addSeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRideFragment.seats += 1;
                CreateRideFragment.seats = (CreateRideFragment.seats > 9) ? 9 : CreateRideFragment.seats;
                seatsText.setText(Integer.toString(CreateRideFragment.seats));
            }
        });

        subtractSeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRideFragment.seats -= 1;
                CreateRideFragment.seats = (CreateRideFragment.seats < 1) ? 1 : CreateRideFragment.seats;
                seatsText.setText(Integer.toString(CreateRideFragment.seats));
            }
        });
    }

    private void initializePriceContainer() {
        RelativeLayout.LayoutParams priceContainerParams = (RelativeLayout.LayoutParams)priceContainer.getLayoutParams();
        priceContainerParams.width = (int)(screenX * 0.4725f);
        priceContainerParams.height = dateContainer.getMeasuredHeight();
        priceContainerParams.leftMargin = (int)(screenX * 0.015f);

        RelativeLayout.LayoutParams priceTitleParams = (RelativeLayout.LayoutParams)priceTitle.getLayoutParams();
        priceTitleParams.topMargin = (int)(screenY * 0.025f);
        priceTitle.setTextSize((int)(screenX * 0.013f));

        RelativeLayout.LayoutParams priceUnderlineParams = (RelativeLayout.LayoutParams)priceUnderline.getLayoutParams();
        priceUnderlineParams.width = (int)(underlineWidth * 0.7f);
        priceUnderlineParams.height = (int)(underlineHeight * 0.2f);
        priceUnderlineParams.topMargin = (int)(underlineHeight * 0.2f);

        RelativeLayout.LayoutParams priceTextContainerParams = (RelativeLayout.LayoutParams)priceTextContainer.getLayoutParams();
        RelativeLayout.LayoutParams priceInputParams = (RelativeLayout.LayoutParams)priceInput.getLayoutParams();
        priceTextContainerParams.topMargin = (int)(dateContainer.getMeasuredHeight() * 0.5f);
        priceInputParams.setMarginStart((int)(screenX * 0.01f));
        priceSignText.setTextSize((int)(screenX * 0.015f));
        priceInput.setTextSize((int)(screenX * 0.015f));

        RelativeLayout.LayoutParams pricePerSeatTextParams = (RelativeLayout.LayoutParams)pricePerSeatText.getLayoutParams();
        pricePerSeatTextParams.topMargin = (int)(dateContainer.getMeasuredHeight() * 0.8f);
        pricePerSeatText.setTextSize((int)(screenX * 0.009f));

        priceInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceInput.setCursorVisible(true);
            }
        });

        priceInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setPriceFromInput();
                    return true;
                }
                return false;
                /*if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(priceInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }*/
            }
        });

        priceInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    priceInput.setCursorVisible(true);
                } else if(!hasFocus) {
                    setPriceFromInput();
                }
            }
        });
    }

    private void setPriceFromInput() {
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
    }

    private void initializePaymentMethodsContainer() {
        cashMethodIcon.setImageResource((acceptsCash) ? R.drawable.check_mark_icon_2 : R.drawable.cross_icon_2);
        inappMethodIcon.setImageResource((acceptsInAppPayments) ? R.drawable.check_mark_icon_2 : R.drawable.cross_icon_2);

        RelativeLayout.LayoutParams paymentMethodsContainerParams = (RelativeLayout.LayoutParams)paymentMethodsContainer.getLayoutParams();
        RelativeLayout.LayoutParams paymentMethodsTitleTextParams = (RelativeLayout.LayoutParams)paymentMethodsTitleText.getLayoutParams();
        RelativeLayout.LayoutParams paymentMethodsTopParams = (RelativeLayout.LayoutParams)paymentMethodsTop.getLayoutParams();
        RelativeLayout.LayoutParams paymentMethodsBottomParams = (RelativeLayout.LayoutParams)paymentMethodsBottom.getLayoutParams();
        RelativeLayout.LayoutParams paymentMethodsDividerParams = (RelativeLayout.LayoutParams)paymentMethodsDivider.getLayoutParams();
        RelativeLayout.LayoutParams cashMethodIconParams = (RelativeLayout.LayoutParams)cashMethodIcon.getLayoutParams();
        RelativeLayout.LayoutParams inappMethodIconParams = (RelativeLayout.LayoutParams)inappMethodIcon.getLayoutParams();
        RelativeLayout.LayoutParams cashMethodHolderParams = (RelativeLayout.LayoutParams)cashMethodHolder.getLayoutParams();
        RelativeLayout.LayoutParams inappMethodHolderParams = (RelativeLayout.LayoutParams)inappMethodHolder.getLayoutParams();
        RelativeLayout.LayoutParams cashMethodTextParams = (RelativeLayout.LayoutParams)cashMethodText.getLayoutParams();
        RelativeLayout.LayoutParams inappMethodTextParams = (RelativeLayout.LayoutParams)inappMethodText.getLayoutParams();
        paymentMethodsContainerParams.topMargin = (int)(screenX * 0.015f);
        paymentMethodsContainerParams.width = (int)(screenX * 0.96f);
        paymentMethodsTitleTextParams.topMargin = (int)(screenY * 0.02f);
        paymentMethodsTitleText.setTextSize(screenX * 0.01f);
        paymentMethodsTopParams.topMargin = (int)(screenY * 0.01f);
        paymentMethodsTopParams.height = (int)(screenY * 0.0025f);
        paymentMethodsBottomParams.height = (int)(screenY * 0.0025f);
        paymentMethodsDividerParams.width = (int)(screenY * 0.0025f);
        cashMethodText.setTextSize(screenX * 0.0085f);
        inappMethodText.setTextSize(screenX * 0.0085f);
        cashMethodIconParams.width = (int)(screenY * 0.025f);
        cashMethodIconParams.height = (int)(screenY * 0.025f);
        inappMethodIconParams.width = (int)(screenY * 0.025f);
        inappMethodIconParams.height = (int)(screenY * 0.025f);
        cashMethodHolderParams.height = (int)(screenY * 0.07f);
        inappMethodHolderParams.height = (int)(screenY * 0.07f);
        cashMethodHolderParams.width = (int)(screenX * 0.4687f);
        inappMethodHolderParams.width = (int)(screenX * 0.4687f);
        paymentMethodsDividerParams.height = (int)(screenY * 0.07f);
        cashMethodTextParams.setMarginStart((int)(screenX * 0.03f));
        inappMethodTextParams.setMarginStart((int)(screenX * 0.03f));

        cashMethodHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptsCash = !acceptsCash;
                cashMethodIcon.setImageResource((acceptsCash) ? R.drawable.check_mark_icon_2 : R.drawable.cross_icon_2);
            }
        });

        inappMethodHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptsInAppPayments = !acceptsInAppPayments;
                inappMethodIcon.setImageResource((acceptsInAppPayments) ? R.drawable.check_mark_icon_2 : R.drawable.cross_icon_2);
            }
        });
    }

    private void initializeDriverPreferences() {
        RelativeLayout.LayoutParams driverPrefsContainerParams = (RelativeLayout.LayoutParams)driverPrefsContainer.getLayoutParams();
        RelativeLayout.LayoutParams driverPreferencesTitleParams = (RelativeLayout.LayoutParams)driverPreferencesTitle.getLayoutParams();
        RelativeLayout.LayoutParams preferenceHolderParams = (RelativeLayout.LayoutParams)preferenceHolder.getLayoutParams();
        driverPrefsContainerParams.width = (int)(screenX * 0.96f);
        driverPrefsContainerParams.height = (int)(screenX * 0.50f);
        driverPrefsContainer.setPadding(0, 0, 0, (int)(screenY * 0.035f));
        driverPreferencesTitleParams.topMargin = (int)(screenY * 0.02f);
        driverPreferencesTitle.setTextSize(screenX * 0.01f);
        preferenceHolderParams.topMargin = (int)(screenY * 0.015f);

        for (int i = 0; i < UserAccount.NUM_OF_PREFERENCES; ++i) {
            if (prefersPreference[i]) {
                preferences[i].setBackgroundColor(Color.parseColor("#1777CD"));
                preferencesIcons[i].setImageResource(preferenceIconResourceWhite[i]);
                preferencesTexts[i].setTextColor(Color.parseColor("#ffffff"));
                preferencesNoTexts[i].setVisibility(View.INVISIBLE);
            } else {
                preferences[i].setBackground(getActivity().getDrawable(R.drawable.driver_preferences_container_border));
                preferencesIcons[i].setImageResource(preferenceIconResourceBlue[i]);
                preferencesTexts[i].setTextColor(Color.parseColor("#1366ae"));
                preferencesNoTexts[i].setVisibility(View.VISIBLE);
            }

            RelativeLayout.LayoutParams preferencesParams = (RelativeLayout.LayoutParams)preferences[i].getLayoutParams();
            RelativeLayout.LayoutParams preferencesIconsParams = (RelativeLayout.LayoutParams)preferencesIcons[i].getLayoutParams();
            RelativeLayout.LayoutParams preferencesIconsHoldersParams = (RelativeLayout.LayoutParams)preferencesIconsHolders[i].getLayoutParams();
            preferencesParams.width = (int)(screenX * 0.2f);
            preferencesParams.height = (int)(screenX * 0.16f);
            if (i == 0) {
                preferencesParams.setMarginStart((int)(screenX * 0.05f));
            } else {
                preferencesParams.setMarginStart((int)(screenX * 0.02f));
            }
            preferencesIconsParams.width = (int)(screenX * 0.07f);
            preferencesIconsParams.height = (int)(screenX * 0.07f);
            preferencesTexts[i].setTextSize(screenX * 0.0065f);
            preferencesTexts[i].setPadding(0, 0, 0, (int)(screenY * 0.002f));
            preferencesIconsHoldersParams.height = (int)(screenX * 0.09f);

            Rect prefTextBounds = new Rect();
            Paint prefTextPaint = preferencesTexts[i].getPaint();
            prefTextPaint.getTextBounds(preferencesTexts[i].getText().toString(), 0, preferencesTexts[i].getText().toString().length(), prefTextBounds);

            preferencesNoTexts[i].setTextSize(screenX * 0.0065f);
            preferencesNoTexts[i].setPadding(0, 0, 0, -(int)(prefTextBounds.height() * 0.2f));

            final int index = i;
            preferences[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prefersPreference[index] = !prefersPreference[index];
                    if (prefersPreference[index]) {
                        preferences[index].setBackgroundColor(Color.parseColor("#1777CD"));
                        preferencesIcons[index].setImageResource(preferenceIconResourceWhite[index]);
                        preferencesTexts[index].setTextColor(Color.parseColor("#ffffff"));
                        preferencesNoTexts[index].setVisibility(View.INVISIBLE);
                    } else {
                        preferences[index].setBackground(getActivity().getDrawable(R.drawable.driver_preferences_container_border));
                        preferencesIcons[index].setImageResource(preferenceIconResourceBlue[index]);
                        preferencesTexts[index].setTextColor(Color.parseColor("#1366ae"));
                        preferencesNoTexts[index].setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void initializeCreateRideButton() {
        RelativeLayout.LayoutParams createRideButtonContainerParams = (RelativeLayout.LayoutParams)createRideButtonContainer.getLayoutParams();
        RelativeLayout.LayoutParams createRideButtonParams = (RelativeLayout.LayoutParams)createRideButton.getLayoutParams();
        createRideButtonContainerParams.width = (int)(screenX * 0.96f);
        createRideButtonContainer.setPadding(0, 0, 0, (int)(screenY * 0.02f));
        createRideButtonParams.width = (int)(screenX * 0.5f);
        createRideButtonParams.height = (int)(screenY * 0.08f);
        createRideButton.setTextSize((int)(screenX * 0.0095f));

        if (ridePost == null) {
            createRideButton.setText("Post Ride");
        } else {
            createRideButton.setText("Save Changes");
        }

        final UserAccount userAccount = ((AppActivity)getActivity()).getUserAccount();
        createRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                if (userAccount.isLoggedIn()) {
                    if (pickupInput.getText() == null || pickupInput.getText().toString().equals("")) {
                        alertDialog.setTitle("Invalid input");
                        alertDialog.setMessage("Pickup location cannot be blank");
                        alertDialog.show();
                    } else if (dropoffInput.getText() == null || dropoffInput.getText().toString().equals("")) {
                        alertDialog.setTitle("Invalid input");
                        alertDialog.setMessage("Dropoff location cannot be blank");
                        alertDialog.show();
                    } else if (!isValidDate()) {
                        alertDialog.setTitle("Invalid date");
                        alertDialog.setMessage("Ride must be within the next 2 months");
                        alertDialog.show();
                    } else if (!isValidTime()) {
                        alertDialog.setTitle("Invalid time");
                        alertDialog.setMessage("Ride must be at least 1 hour from now");
                        alertDialog.show();
                    } else if (!acceptsCash && !acceptsInAppPayments) {
                        alertDialog.setTitle("Invalid Selection");
                        alertDialog.setMessage("At least 1 payment method must be selected");
                        alertDialog.show();
                    } else {
                        LayoutInflater inflater = getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.phone_number_layout, null);
                        final EditText etPhoneNumber = alertLayout.findViewById(R.id.et_phoneNumber);
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("Please verify/add your Phone Number");
                        // this is set the view from XML inside AlertDialogf
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
//                alert.setCancelable(false);
                        if (userAccount.getPhoneNumber() != "0") {
                            etPhoneNumber.setText(userAccount.getPhoneNumber());
                        }

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Cancelled Ride Post", Toast.LENGTH_SHORT).show();
                            }
                        });

                        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!(etPhoneNumber.getText().toString().equals(""))) {
                                   phoneNumber = etPhoneNumber.getText().toString();
                                   runCreateRide();
                                }
                            }
                        });
                        AlertDialog dialog = alert.create();
                        dialog.show();


                    }
                } else {
                    alertDialog.setTitle("You are not logged in");
                    alertDialog.setMessage("Please check your connection and restart the app");
                    alertDialog.show();
                }
            }
        });
    }

    private void runCreateRide() {
        UserAccount userAccount = ((AppActivity)getActivity()).getUserAccount();

        if (phoneNumber == null) {
            phoneNumber = String.valueOf(userAccount.getPhoneNumber());
        }

        int acceptsCashInt = (acceptsCash) ? 1 : 0;
        int acceptsInAppPaymentsInt = (acceptsInAppPayments) ? 1 : 0;
        int prefersMusicInt = (prefersPreference[0]) ? 1 : 0;
        int prefersDrinksInt = (prefersPreference[1]) ? 1 : 0;
        int prefersLuggageInt = (prefersPreference[2]) ? 1 : 0;
        int prefersPetsInt = (prefersPreference[3]) ? 1 : 0;
        int prefersGenderInt = (prefersPreference[4]) ? 1 : 0;

        String acceptsCashStr = (userAccount.doesAcceptCash() == acceptsCash) ? "-1" : Integer.toString(acceptsCashInt);
        String acceptsInAppPaymentsStr = (userAccount.doesAcceptInAppPayments() == acceptsInAppPayments) ? "-1" : Integer.toString(acceptsInAppPaymentsInt);
        String prefersMusicStr = (userAccount.doesPreferMusic() == prefersPreference[0]) ? "-1" : Integer.toString(prefersMusicInt);
        String prefersDrinksStr = (userAccount.doesPreferDrinks() == prefersPreference[1]) ? "-1" : Integer.toString(prefersDrinksInt);
        String prefersLuggageStr = (userAccount.doesPreferExtraLuggage() == prefersPreference[2]) ? "-1" : Integer.toString(prefersLuggageInt);
        String prefersPetsStr = (userAccount.doesPreferPets() == prefersPreference[3]) ? "-1" : Integer.toString(prefersPetsInt);
        String prefersGenderStr = (userAccount.doesPreferGender() == prefersPreference[4]) ? "-1" : Integer.toString(prefersGenderInt);

        String rideId = "-1";
        if (ridePost != null) {
            rideId = Integer.toString(ridePost.getRideId());
        }

        CreateRideServerRequest createRideServerRequest = new CreateRideServerRequest(getActivity());
        createRideServerRequest.execute(
                Integer.toString(userAccount.getUserId()),
                Integer.toString(CreateRideFragment.day),
                Integer.toString(CreateRideFragment.date),
                Integer.toString(CreateRideFragment.month),
                Integer.toString(CreateRideFragment.year),
                Integer.toString(CreateRideFragment.hour),
                Integer.toString(CreateRideFragment.minute),
                Integer.toString(CreateRideFragment.seats),
                Double.toString(CreateRideFragment.price),
                pickupAddressFull,
                dropoffAddressFull,
                pickupAddressDisplay,
                dropoffAddressDisplay,
                pickupCity,
                dropoffCity,
                Double.toString(CreateRideFragment.pickupLatitude),
                Double.toString(CreateRideFragment.pickupLongitude),
                Double.toString(CreateRideFragment.dropoffLatitude),
                Double.toString(CreateRideFragment.dropoffLongitude),
                acceptsCashStr,
                acceptsInAppPaymentsStr,
                prefersMusicStr,
                prefersDrinksStr,
                prefersLuggageStr,
                prefersPetsStr,
                prefersGenderStr,
                rideId,
                phoneNumber);
    }

    private boolean isValidDate() {
        final Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);

        if (currentYear > year) {
            return false;
        } else if (year > currentYear + 1) {
            return false;
        } else if (month - currentMonth > 2) {
            return false;
        } else if (currentMonth > month && (year != currentYear + 1)) {
            return false;
        } else if (year == currentYear + 1 && month + 12 - currentMonth > 2) {
            return false;
        } else if (currentMonth == month && currentDate > date) {
            return false;
        }
        return true;
    }

    private boolean isValidTime() {
        final Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        if (date == currentDate && month == currentMonth && year == currentYear) {
            if (hour <= currentHour) {
                return false;
            } else if (hour == currentHour + 1 && minute < currentMinute) {
                return false;
            }
        }
        return true;
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
}
