package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Driver;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RideDetailFragment extends Fragment {

    public static final int ACCESSOR_DRIVER = 0;
    public static final int ACCESSOR_VIEWER = 1;
    public static final int ACCESSOR_PASSENGER = 2;

    private float screenX, screenY;
    private int accessor;

    private String[] dayOfMonth = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private String[] dayOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    private String phoneNumber;

    private RidePost ridePost;
    private float underlineWidth, underlineHeight;
    private RelativeLayout rideDetailContainer, menuButton, borderBottom, rideSeparator, seatsBookedContainer, seatsRemainingContainer;
    private RelativeLayout dateContainer, timeContainer, rideCitiesContainer, driverImageHolder;
    private RelativeLayout priceContainer, rideButtonsContainer, pickupContainer, dropoffContainer;
    private RelativeLayout seatsPriceContainer, seatsContainer, pricePerSeatContainer, seatsUnderline, priceUnderline;
    private RelativeLayout seatsTextButtonsContainer, priceTextContainer;
    private RelativeLayout ratingContainer, messageButtonsContainer, messengerButtonContainer;
    private RelativeLayout profileAndMessageContainer, paymentMethodsContainer, paymentMethodsTop, paymentMethodsBottom;
    private RelativeLayout cashMethodHolder, inappMethodHolder, paymentMethodsDivider, preferenceHolder;
    private RelativeLayout[] preferences, preferencesIconsHolders;
    private TextView dateDayText, dateDateText, dateMonthText, timeText, timeAmPmText, seatsBookedTitleText, seatsBookedText, seatsTitle;
    private TextView startCityText, endCityText, priceText, driverNameText, ratingText, seatsRemainingTitleText, seatsText, seatsRemainingText;
    private TextView pickupTitleText, dropoffTitleText, pickupAddressText, dropoffAddressText;
    private TextView paymentMethodsTitleText, cashMethodText, inappMethodText, driverPreferencesTitle;
    private TextView[] preferencesTexts, preferencesNoTexts;
    private TextView priceTitle, priceSignText, priceInput, pricePerSeatText;
    private ImageView addSeatButton, subtractSeatButton;
    private ImageView cityArrow, driverImage, ratingIcon, messengerIcon, cashMethodIcon, inappMethodIcon;
    private ImageView[] preferencesIcons;
    private Button shortlistButton, bookRideButton, shareToFacebookButton, editRideButton, messageButton, messengerButton;
    private String jsonResultString;
    private List<Booking> userBookingList;
    private Booking isBooked;
    private UserAccount userAccount;
    private UserAccount driverAccount;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    Boolean bookedSuccessful;

    public RideDetailFragment(int accessor, RidePost ridePost) {
        this.accessor = accessor;
        this.ridePost = ridePost;
    }

    public RideDetailFragment(int accessor, RidePost ridePost, Boolean bookedSuccessful) {
        this.accessor = accessor;
        this.ridePost = ridePost;
        this.bookedSuccessful = bookedSuccessful;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppActivity)getActivity()).setCurrentPageNumber(AppActivity.RIDE_DETAIL_PAGE);
        userAccount = ((AppActivity)getActivity()).getUserAccount();
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        userBookingList = new ArrayList<Booking>();
        String[] filters = {"passengerUserId=" + userAccount.getUserId()};
        fetchBookedListFromServer(filters);

        GetUserServerRequest getUserServerRequest = new GetUserServerRequest();
        getUserServerRequest.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_detail_fragment, container, false);
        setUnit();

        underlineWidth = 0;
        underlineHeight = 0;

        preferences = new RelativeLayout[UserAccount.NUM_OF_PREFERENCES];
        preferencesIcons = new ImageView[UserAccount.NUM_OF_PREFERENCES];
        preferencesTexts = new TextView[UserAccount.NUM_OF_PREFERENCES];
        preferencesIconsHolders = new RelativeLayout[UserAccount.NUM_OF_PREFERENCES];
        preferencesNoTexts = new TextView[UserAccount.NUM_OF_PREFERENCES];

        rideDetailContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_container);
        menuButton = (RelativeLayout)view.findViewById(R.id.ride_detail_menu_button);
        borderBottom = (RelativeLayout)view.findViewById(R.id.ride_detail_border_bottom);
        dateContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_date_container);
        dateDayText = (TextView)view.findViewById(R.id.ride_detail_date_day);
        dateDateText = (TextView)view.findViewById(R.id.ride_detail_date_date);
        dateMonthText = (TextView)view.findViewById(R.id.ride_detail_date_month);
        timeContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_time_container);
        timeText = (TextView)view.findViewById(R.id.ride_detail_time);
        timeAmPmText = (TextView)view.findViewById(R.id.ride_detail_am_pm);
        rideCitiesContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_city_container);
        startCityText = (TextView)view.findViewById(R.id.ride_detail_start_city);
        endCityText = (TextView)view.findViewById(R.id.ride_detail_end_city);
        cityArrow = (ImageView) view.findViewById(R.id.ride_detail_city_arrow);
        priceContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_price_container);
        priceText = (TextView)view.findViewById(R.id.ride_detail_price);
        rideButtonsContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_ride_button_container);
        shortlistButton = (Button)view.findViewById(R.id.ride_detail_shortlist_button);
        bookRideButton = (Button)view.findViewById(R.id.ride_detail_book_button);
        shareToFacebookButton = (Button)view.findViewById(R.id.ride_detail_share_facebook_button);
        editRideButton = (Button)view.findViewById(R.id.ride_detail_edit_ride_button);
        pickupContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_pickup_container);
        dropoffContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_dropoff_container);
        pickupTitleText = (TextView)view.findViewById(R.id.ride_detail_pickup_title);
        dropoffTitleText = (TextView)view.findViewById(R.id.ride_detail_dropoff_title);
        pickupAddressText = (TextView)view.findViewById(R.id.ride_detail_pickup_address);
        dropoffAddressText = (TextView)view.findViewById(R.id.ride_detail_dropoff_address);
        seatsBookedContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_seats_booked_container);
        seatsBookedTitleText = (TextView)view.findViewById(R.id.ride_detail_seats_booked_title);
        seatsBookedText = (TextView)view.findViewById(R.id.ride_detail_seats_booked);
        seatsRemainingContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_seats_remaining_container);
        seatsRemainingTitleText = (TextView)view.findViewById(R.id.ride_detail_seats_remaining_title);
        seatsRemainingText = (TextView)view.findViewById(R.id.ride_detail_seats_remaining);

        seatsPriceContainer = (RelativeLayout)view.findViewById(R.id.seats_price_container);
        seatsContainer = (RelativeLayout)view.findViewById(R.id.seats_container);
        seatsTitle = (TextView)view.findViewById(R.id.seats_title);
        seatsUnderline = (RelativeLayout)view.findViewById(R.id.seats_underline);
        seatsTextButtonsContainer = (RelativeLayout)view.findViewById(R.id.seats_text_buttons_container);
        seatsText = (TextView)view.findViewById(R.id.seats);
        addSeatButton = (ImageView)view.findViewById(R.id.add_seats);
        subtractSeatButton = (ImageView)view.findViewById(R.id.subtract_seats);

        pricePerSeatContainer = (RelativeLayout)view.findViewById(R.id.price_per_seat_container);
        priceTitle = (TextView)view.findViewById(R.id.price_title);
        priceUnderline = (RelativeLayout)view.findViewById(R.id.price_underline);
        priceTextContainer = (RelativeLayout)view.findViewById(R.id.price_text_container);
        priceSignText = (TextView)view.findViewById(R.id.price_sign_text);
        priceInput = (TextView)view.findViewById(R.id.price_edittext);
        pricePerSeatText = (TextView)view.findViewById(R.id.price_per_seat_text);

        rideSeparator = (RelativeLayout)view.findViewById(R.id.ride_detail_ride_separator);
        driverImageHolder = (RelativeLayout)view.findViewById(R.id.ride_detail_driver_image_holder);
        driverImage = (ImageView)view.findViewById(R.id.ride_detail_driver_image);
        driverNameText = (TextView)view.findViewById(R.id.ride_detail_driver_name);
        ratingContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_rating_container);
        ratingIcon = (ImageView)view.findViewById(R.id.ride_detail_rating_icon);
        ratingText = (TextView)view.findViewById(R.id.ride_detail_rating);
        messageButtonsContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_message_button_container);
        messageButton = (Button)view.findViewById(R.id.ride_detail_message_button);
        messengerButton = (Button)view.findViewById(R.id.ride_detail_messenger_button);
        messengerIcon = (ImageView)view.findViewById(R.id.ride_detail_messenger_icon);
        profileAndMessageContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_profile_message_container);
        messengerButtonContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_messenger_button_container);
        paymentMethodsContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_payment_methods_container);
        paymentMethodsTop = (RelativeLayout)view.findViewById(R.id.ride_detail_payment_methods_top);
        paymentMethodsBottom = (RelativeLayout)view.findViewById(R.id.ride_detail_payment_methods_bottom);
        paymentMethodsDivider = (RelativeLayout)view.findViewById(R.id.ride_detail_payment_methods_divider);
        cashMethodHolder = (RelativeLayout)view.findViewById(R.id.ride_detail_cash_method_holder);
        inappMethodHolder = (RelativeLayout)view.findViewById(R.id.ride_detail_inapp_method_holder);
        paymentMethodsTitleText = (TextView)view.findViewById(R.id.ride_detail_payment_methods_text);
        cashMethodText = (TextView)view.findViewById(R.id.ride_detail_cash_method_text);
        inappMethodText = (TextView)view.findViewById(R.id.ride_detail_inapp_method_text);
        cashMethodIcon = (ImageView)view.findViewById(R.id.ride_detail_cash_method_icon);
        inappMethodIcon = (ImageView)view.findViewById(R.id.ride_detail_inapp_method_icon);
        driverPreferencesTitle = (TextView)view.findViewById(R.id.ride_details_driver_prefs_title);
        preferenceHolder = (RelativeLayout)view.findViewById(R.id.ride_details_driver_prefs_holder);
        preferences[0] = (RelativeLayout)view.findViewById(R.id.ride_details_prefs_music_holder);
        preferences[1] = (RelativeLayout)view.findViewById(R.id.ride_details_prefs_drinks_holder);
        preferences[2] = (RelativeLayout)view.findViewById(R.id.ride_details_prefs_luggage_holder);
        preferences[3] = (RelativeLayout)view.findViewById(R.id.ride_details_prefs_pets_holder);
        preferencesIcons[0] = (ImageView)view.findViewById(R.id.ride_details_prefs_music_icon);
        preferencesIcons[1] = (ImageView)view.findViewById(R.id.ride_details_prefs_drinks_icon);
        preferencesIcons[2] = (ImageView)view.findViewById(R.id.ride_details_prefs_luggage_icon);
        preferencesIcons[3] = (ImageView)view.findViewById(R.id.ride_details_prefs_pets_icon);
        preferencesTexts[0] = (TextView)view.findViewById(R.id.ride_details_prefs_music_text);
        preferencesTexts[1] = (TextView)view.findViewById(R.id.ride_details_prefs_drinks_text);
        preferencesTexts[2] = (TextView)view.findViewById(R.id.ride_details_prefs_luggage_text);
        preferencesTexts[3] = (TextView)view.findViewById(R.id.ride_details_prefs_pets_text);
        preferencesIconsHolders[0] = (RelativeLayout)view.findViewById(R.id.ride_details_prefs_music_icon_holder);
        preferencesIconsHolders[1] = (RelativeLayout)view.findViewById(R.id.ride_details_prefs_drinks_icon_holder);
        preferencesIconsHolders[2] = (RelativeLayout)view.findViewById(R.id.ride_details_prefs_luggage_icon_holder);
        preferencesIconsHolders[3] = (RelativeLayout)view.findViewById(R.id.ride_details_prefs_pets_icon_holder);
        preferencesNoTexts[0] = (TextView)view.findViewById(R.id.ride_details_prefs_music_no_text);
        preferencesNoTexts[1] = (TextView)view.findViewById(R.id.ride_details_prefs_drinks_no_text);
        preferencesNoTexts[2] = (TextView)view.findViewById(R.id.ride_details_prefs_luggage_no_text);
        preferencesNoTexts[3] = (TextView)view.findViewById(R.id.ride_details_prefs_pets_no_text);

        dateDayText.setText(dayOfWeek[ridePost.getDay() - 1]);
        dateMonthText.setText(dayOfMonth[ridePost.getMonth() - 1]);
        dateDateText.setText((ridePost.getDate() < 10) ? ("0" + Integer.toString(ridePost.getDate())) : Integer.toString(ridePost.getDate()));

        int hourFormat12 = (ridePost.getHour() > 12) ? (ridePost.getHour() - 12) : ridePost.getHour();
        hourFormat12 = (hourFormat12 == 0) ? 12 : hourFormat12;
        String timeString = Integer.toString(hourFormat12) + ":";
        timeString += (ridePost.getMinute() < 10) ? ("0" + Integer.toString(ridePost.getMinute())) : Integer.toString(ridePost.getMinute());
        timeText.setText(timeString);
        timeAmPmText.setText((ridePost.getHour() >= 12) ? "PM" : "AM");

        DecimalFormat df = new DecimalFormat("#.00");
        String priceString = (ridePost.getPrice() != 0) ? df.format(ridePost.getPrice()) : "0.00";
        priceText.setText("$ " + priceString);

        pickupAddressText.setText(ridePost.getPickupAddressDisplay());
        dropoffAddressText.setText(ridePost.getDropoffAddressDisplay());
        startCityText.setText(ridePost.getPickupCity());
        endCityText.setText(ridePost.getDropoffCity());

        /*if (accessor == ACCESSOR_DRIVER) {
            UserAccount userAccount = ((AppActivity)getActivity()).getUserAccount();
            driverNameText.setText(userAccount.getName());
            if (userAccount.isLoggedIn() && userAccount.getAccountType() == UserAccount.ACCOUNT_TYPE_FACEBOOK_ACCOUNT) {
                driverImage.setImageURI(Uri.parse(userAccount.getFacebookProfilePicURI()));
                System.out.println("HERE IS STUFF !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(userAccount.getFacebookProfilePicURI());
                try {
                    driverImage.setImageDrawable(Drawable.createFromStream(
                            getActivity().getContentResolver().openInputStream(Uri.parse(userAccount.getFacebookProfilePicURI())),
                            null));
                } catch (FileNotFoundException e) {

                }


            }
        }*/


        initializeToolbar();
        initializeRideDetailContainer();
        initializeDateDisplay();
        initializeTimeDisplay();
        initializeCitiesDisplay();
        initializePriceDisplay();
        initializeRideButtons();
        initializeAddressDisplay();
        initializeSeatsDisplay();
        initializeRideSeparator();
        initializeRatingDisplay();
        initializeMessageButtons();
        initializeSeatsPriceContainers();
        initializeSeatsContainer();
        initializePriceContainer();
        initializeDriverPreferencesDisplay();

        return view;
    }

    private void setUnit() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
    }

    private void initializeToolbar() {
        RelativeLayout.LayoutParams menuButtonParams = (RelativeLayout.LayoutParams)menuButton.getLayoutParams();
        menuButtonParams.setMarginStart((int)(screenX * 0.03f - getPixel(8)));
    }

    private void initializeRideDetailContainer() {
        RelativeLayout.LayoutParams rideDetailContainerParams = (RelativeLayout.LayoutParams)rideDetailContainer.getLayoutParams();
        RelativeLayout.LayoutParams borderBottomParams = (RelativeLayout.LayoutParams)borderBottom.getLayoutParams();
        rideDetailContainerParams.width = (int)(screenX * 0.94f);
        borderBottomParams.height = (int)(screenX * 0.02f);
    }



    private void initializeSeatsPriceContainers() {
        RelativeLayout.LayoutParams seatsPriceContainerParams = (RelativeLayout.LayoutParams)seatsPriceContainer.getLayoutParams();
        seatsPriceContainerParams.topMargin = (int)(screenX * 0.015f);
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
        RelativeLayout.LayoutParams pricePerSeatContainerParams = (RelativeLayout.LayoutParams)pricePerSeatContainer.getLayoutParams();
        pricePerSeatContainerParams.width = (int)(screenX * 0.4725f);
        pricePerSeatContainerParams.height = dateContainer.getMeasuredHeight();
        pricePerSeatContainerParams.leftMargin = (int)(screenX * 0.015f);

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

    private void initializeDateDisplay() {
        RelativeLayout.LayoutParams dateContainerParams = (RelativeLayout.LayoutParams)dateContainer.getLayoutParams();
        RelativeLayout.LayoutParams dateDateTextParams = (RelativeLayout.LayoutParams)dateDateText.getLayoutParams();
        RelativeLayout.LayoutParams dateMonthTextParams = (RelativeLayout.LayoutParams)dateMonthText.getLayoutParams();
        dateContainerParams.topMargin = (int)(screenY * 0.015f);
        dateDayText.setTextSize(screenX * 0.008f);
        dateDateText.setTextSize(screenX * 0.018f);
        dateMonthText.setTextSize(screenX * 0.008f);

        Rect dateDayTextBounds = new Rect();
        Paint dateDayTextPaint = dateDayText.getPaint();
        dateDayTextPaint.getTextBounds(dateDayText.getText().toString(), 0, dateDayText.getText().toString().length(), dateDayTextBounds);

        Rect dateDateTextBounds = new Rect();
        Paint dateDateTextPaint = dateDateText.getPaint();
        dateDateTextPaint.getTextBounds(dateDateText.getText().toString(), 0, dateDateText.getText().toString().length(), dateDateTextBounds);

        //dateDayText.measure(0, 0); dateDayText.getMeasuredHeight();
        //dateDateText.measure(0, 0);

        dateDateTextParams.topMargin = (int)(dateDayTextBounds.height());
        dateMonthTextParams.topMargin = (int)(dateDateTextBounds.height() + dateDayTextBounds.height() + dateDateTextBounds.height() * 0.5f);
    }

    private void initializeTimeDisplay() {
        RelativeLayout.LayoutParams timeContainerParams = (RelativeLayout.LayoutParams)timeContainer.getLayoutParams();
        RelativeLayout.LayoutParams timeAmPmTextParams = (RelativeLayout.LayoutParams)timeAmPmText.getLayoutParams();
        timeContainerParams.topMargin = (int)(screenY * 0.002f);
        timeText.setTextSize(screenX * 0.013f);
        timeAmPmText.setTextSize(screenX * 0.013f);
        timeAmPmTextParams.setMarginStart((int)(screenX * 0.01f));
    }

    private void initializeCitiesDisplay() {
        RelativeLayout.LayoutParams rideCitiesContainerParams = (RelativeLayout.LayoutParams)rideCitiesContainer.getLayoutParams();
        RelativeLayout.LayoutParams cityArrowParams = (RelativeLayout.LayoutParams)cityArrow.getLayoutParams();
        RelativeLayout.LayoutParams endCityTextParams = (RelativeLayout.LayoutParams)endCityText.getLayoutParams();
        rideCitiesContainerParams.topMargin = (int)(screenY * 0.003f);
        startCityText.setTextSize(screenX * 0.02f);
        endCityText.setTextSize(screenX * 0.02f);
        cityArrowParams.width = (int)(screenX * 0.05f);
        cityArrowParams.setMarginStart((int)(screenX * 0.02f));
        endCityTextParams.setMarginStart((int)(screenX * 0.02f));
    }

    private void initializePriceDisplay() {
        RelativeLayout.LayoutParams priceContainerParams = (RelativeLayout.LayoutParams)priceContainer.getLayoutParams();
        priceContainerParams.topMargin = (int)(screenY * 0.001f);
        priceText.setTextSize(screenX * 0.0128f);
    }

    private void initializeRideButtons() {
        RelativeLayout.LayoutParams rideButtonsContainerParams = (RelativeLayout.LayoutParams)rideButtonsContainer.getLayoutParams();
        RelativeLayout.LayoutParams shortlistButtonParams = (RelativeLayout.LayoutParams)shortlistButton.getLayoutParams();
        RelativeLayout.LayoutParams bookRideButtonParams = (RelativeLayout.LayoutParams)bookRideButton.getLayoutParams();
        rideButtonsContainerParams.topMargin = (int)(screenY * 0.01f);
        shortlistButtonParams.width = (int)(screenX * 0.42f);
        bookRideButtonParams.width = (int)(screenX * 0.5f);
        shortlistButtonParams.height = (int)(screenY * 0.06f);
        bookRideButtonParams.height = (int)(screenY * 0.08f);
        bookRideButtonParams.setMarginStart((int)(screenX * 0.02f));
        shortlistButton.setTextSize(screenX * 0.013f);
        bookRideButton.setTextSize(screenX * 0.013f);



        RelativeLayout.LayoutParams shareToFacebookButtonParams = (RelativeLayout.LayoutParams)shareToFacebookButton.getLayoutParams();
        RelativeLayout.LayoutParams editRideButtonParams = (RelativeLayout.LayoutParams)editRideButton.getLayoutParams();
        shareToFacebookButtonParams.width = (int)(screenX * 0.42f);
        editRideButtonParams.width = (int)(screenX * 0.42f);
        shareToFacebookButtonParams.height = (int)(screenY * 0.06f);
        editRideButtonParams.height = (int)(screenY * 0.06f);
        editRideButtonParams.setMarginStart((int)(screenX * 0.02f));
        shareToFacebookButton.setTextSize(screenX * 0.013f);
        editRideButton.setTextSize(screenX * 0.013f);

        if (accessor == ACCESSOR_DRIVER) {
            shareToFacebookButton.setVisibility(View.VISIBLE);
            editRideButton.setVisibility(View.VISIBLE);

            shareToFacebookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        String day = dayOfWeek[ridePost.getDay() - 1];
                        String Month = dayOfMonth[ridePost.getMonth() - 1];
                        String dateText = (ridePost.getDate() < 10) ? ("0" + Integer.toString(ridePost.getDate())) : Integer.toString(ridePost.getDate());


                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                                .setQuote("I am driving from" + ridePost.getPickupCity() + " to " + ridePost.getDropoffCity() + " on " + dateText + ", " + Month + " " + day)
//                                .setShareHashtag(new ShareHashtag.Builder()
//                                        .setHashtag("#ConnectTheWorld")
//                                        .build())
                                .build();

                        shareDialog.show(content);
                    }
                }
            });

            editRideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    CreateRideFragment createRideFragment = new CreateRideFragment(ridePost);
                    transaction.replace(R.id.fragment_container, createRideFragment);
                    transaction.commit();
                }
            });
        } else if (accessor == ACCESSOR_VIEWER){
            bookRideButton.setVisibility(View.VISIBLE);
            bookRideButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.phone_number_layout, null);
                    final EditText etPhoneNumber = alertLayout.findViewById(R.id.et_phoneNumber);
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Please verify/add your Phone Number");
                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
//                alert.setCancelable(false);
                    if (userAccount.getPhoneNumber() != "0") {
                        etPhoneNumber.setText(String.valueOf(userAccount.getPhoneNumber()));
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
                                runBookRide();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Add Phone Number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();




                }
            });
        } else if (accessor == ACCESSOR_PASSENGER){
            bookRideButton.setVisibility(View.VISIBLE);
            bookRideButton.setBackground(getActivity().getDrawable(R.drawable.ride_detail_button_border));
            bookRideButton.setTextColor(Color.parseColor("#1777CD"));

            if (isBooked != null) {
                if (isBooked.getIsAccepted() == 0) {
                    bookRideButton.setText("Booking Requested");
                }
                else if (isBooked.getIsAccepted() == 1) {
                    bookRideButton.setText("Booked!");
                } if (isBooked.getIsAccepted() == 2) {
                    bookRideButton.setText("Request Rejected");
                }
            }


            bookRideButton.setClickable(false);


        }
    }

    private void runBookRide() {
        Booking booking = new Booking();
        booking.setOwnerUserId(ridePost.getOwnerUserId());
        booking.setPassengerUserId(userAccount.getUserId());
        booking.setRideId(ridePost.getRideId());
        booking.setSeatsBookedByPassenger(1);

        CreateBookingServerRequest createBookingServerRequest = new CreateBookingServerRequest(getActivity());
        createBookingServerRequest.setRidePost(ridePost);
        createBookingServerRequest.execute(
                Integer.toString(booking.getOwnerUserId()),
                Integer.toString(booking.getRideId()),
                Integer.toString(booking.getPassengerUserId()),
                Integer.toString(booking.getSeatsBookedByPassenger()),
                Integer.toString(0),
                Integer.toString(0),
                phoneNumber
        );
    }

    private void initializeAddressDisplay() {
        RelativeLayout.LayoutParams pickupContainerParams = (RelativeLayout.LayoutParams)pickupContainer.getLayoutParams();
        RelativeLayout.LayoutParams dropoffContainerParams = (RelativeLayout.LayoutParams)dropoffContainer.getLayoutParams();
        RelativeLayout.LayoutParams pickupAddressTextParams = (RelativeLayout.LayoutParams)pickupAddressText.getLayoutParams();
        RelativeLayout.LayoutParams dropoffAddressTextParams = (RelativeLayout.LayoutParams)dropoffAddressText.getLayoutParams();
        pickupContainerParams.topMargin = (int)(screenY * 0.01f);
        dropoffContainerParams.topMargin = (int)(screenY * 0.001f);
        pickupTitleText.setTextSize(screenX * 0.0095f);
        pickupAddressText.setTextSize(screenX * 0.0095f);
        dropoffTitleText.setTextSize(screenX * 0.0095f);
        dropoffAddressText.setTextSize(screenX * 0.0095f);
        pickupAddressTextParams.setMarginStart((int)(screenX * 0.015f));
        dropoffAddressTextParams.setMarginStart((int)(screenX * 0.015f));
    }

    private void fetchBookedListFromServer(String[] params) {
        if (params != null) {
            GetBookingListServerRequest getBookingListServerRequest = new GetBookingListServerRequest(params);
            getBookingListServerRequest.execute();
        }
        else {
            GetBookingListServerRequest getBookingListServerRequest = new GetBookingListServerRequest();
            getBookingListServerRequest.execute();
        }
    }

    private void sendNotificationIfBookedSuccessful() {


        if (bookedSuccessful == true) {
            OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
            String userId = status.getSubscriptionStatus().getUserId();
            System.out.println("onesignal userId:" + userId);
            boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();

            if (!isSubscribed)
                return;

            try {
                JSONObject notificationContent = new JSONObject(
                        "{'contents': {'en': '" + userAccount.getName() + " has requested to book your ride'}," +
                        "'include_player_ids': ['" + userId + "'], " +
                        "'headings': {'en': 'Booking Requested'}, " +
                        "'big_picture': ''}");
                OneSignal.postNotification(notificationContent, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private void initializeUserBookingList() {
        System.out.println("INITIALIZING BOOKING LIST");
        userBookingList.clear();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResultString);
            JSONArray rideList = jsonResponse.getJSONArray("bookedRidesList");
            for (int i = 0; i < rideList.length(); ++i) {
                try {
                    JSONObject ride = rideList.getJSONObject(i);
                    // Pulling items from the array
                    Booking booking = new Booking();
                    booking.setPassengerUserId(ride.getInt("PassengerUserId"));
                    booking.setRideId(ride.getInt("RideId"));
                    booking.setBookingId(ride.getInt("BookingId"));

                    if (ride.getInt("RideId") == ridePost.getRideId()) {
                        accessor = 2;
                        isBooked = new Booking();
                        isBooked.setPassengerUserId(ride.getInt("PassengerUserId"));
                        isBooked.setRideId(ride.getInt("RideId"));
                        isBooked.setBookingId(ride.getInt("BookingId"));
                        isBooked.setIsAccepted(ride.getInt("IsAccepted"));
                        initializeRideButtons();
                    }

                    System.out.println(booking);
                    userBookingList.add(booking);
                } catch (JSONException e) {
                    System.out.println(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//
//
//        for(Fragment fragment: getActivity().getSupportFragmentManager().getFragments()){
//            if (currentFragment instanceof RideDetailFragment) {
//                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//            }
//        }
//
//        if (currentFragment instanceof RideListingFragment) {
//            FragmentTransaction fragTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//            fragTransaction.detach(currentFragment);
//            fragTransaction.attach(currentFragment);
//            fragTransaction.commit();
//        }
    }

    private void initializeSeatsDisplay() {
        RelativeLayout.LayoutParams seatsBookedContainerParams = (RelativeLayout.LayoutParams)seatsBookedContainer.getLayoutParams();
        RelativeLayout.LayoutParams seatsBookedTextParams = (RelativeLayout.LayoutParams)seatsBookedText.getLayoutParams();
        RelativeLayout.LayoutParams seatsRemainingContainerParams = (RelativeLayout.LayoutParams)seatsRemainingContainer.getLayoutParams();
        RelativeLayout.LayoutParams seatsRemainingTextParams = (RelativeLayout.LayoutParams)seatsRemainingText.getLayoutParams();
        seatsBookedContainerParams.topMargin = (int)(screenY * 0.01f);
        seatsBookedTitleText.setTextSize(screenX * 0.0095f);
        seatsBookedText.setTextSize(screenX * 0.0095f);
        seatsBookedTextParams.setMarginStart((int)(screenX * 0.015f));
        seatsRemainingContainerParams.topMargin = (int)(screenY * 0.01f);
        seatsBookedTitleText.setTextSize(screenX * 0.0095f);
        seatsRemainingText.setTextSize(screenX * 0.0095f);
        seatsRemainingTextParams.setMarginStart((int)(screenX * 0.015f));

        if (accessor == ACCESSOR_DRIVER) {
            seatsBookedContainer.setVisibility(View.VISIBLE);
            seatsBookedText.setText(ridePost.getSeatsBooked() + " / " + ridePost.getSeatsTotal());
        } else if (accessor == ACCESSOR_VIEWER) {
            seatsRemainingContainer.setVisibility(View.VISIBLE);
            seatsRemainingText.setText((ridePost.getSeatsTotal() - ridePost.getSeatsBooked()) + " / " + ridePost.getSeatsTotal());
        } else if (accessor == ACCESSOR_PASSENGER) {
            seatsBookedContainer.setVisibility(View.VISIBLE);
            seatsRemainingContainer.setVisibility(View.VISIBLE);
            seatsRemainingContainerParams.topMargin = (int)(screenY * 0.01f);
            seatsBookedTitleText.setText("Seats Booked By You:");
            seatsRemainingText.setText((ridePost.getSeatsTotal() - ridePost.getSeatsBooked()) + " / " + ridePost.getSeatsTotal());
            // set seats booked by you text
        }
    }

    private void initializeRideSeparator() {
        RelativeLayout.LayoutParams rideSeparatorParams = (RelativeLayout.LayoutParams)rideSeparator.getLayoutParams();
        rideSeparatorParams.height = (int)(screenY * 0.01f);
        rideSeparatorParams.topMargin = (int)(screenY * 0.01f);
    }

    private void initializeDriverProfile() {
        UserAccount userAccount = ((AppActivity)getActivity()).getUserAccount();
        if (accessor == ACCESSOR_DRIVER) {
            if (userAccount.isLoggedIn() && userAccount.getAccountType() == UserAccount.ACCOUNT_TYPE_FACEBOOK_ACCOUNT) {
                SetProfileImageAsyncTask setProfileImageAsyncTask = new SetProfileImageAsyncTask(driverImage);
                setProfileImageAsyncTask.execute(userAccount.getFacebookProfilePicURI());
            } else {
                driverImage.setImageResource(R.drawable.user_icon_white);
            }
            driverNameText.setText(userAccount.getName());
        }
        else if (accessor == ACCESSOR_VIEWER || accessor == ACCESSOR_PASSENGER) {
            SetProfileImageAsyncTask setProfileImageAsyncTask = new SetProfileImageAsyncTask(driverImage);
            setProfileImageAsyncTask.execute(driverAccount.getFacebookProfilePicURI());

            driverNameText.setText(driverAccount.getName());
        }

        RelativeLayout.LayoutParams profileAndMessageContainerParams = (RelativeLayout.LayoutParams)profileAndMessageContainer.getLayoutParams();
        RelativeLayout.LayoutParams driverImageHolderParams = (RelativeLayout.LayoutParams)driverImageHolder.getLayoutParams();
        RelativeLayout.LayoutParams driverImageParams = (RelativeLayout.LayoutParams)driverImage.getLayoutParams();
        RelativeLayout.LayoutParams driverNameTextParams = (RelativeLayout.LayoutParams)driverNameText.getLayoutParams();
        profileAndMessageContainerParams.topMargin = (int)(screenY * 0.02f);
        driverImageHolderParams.width = (int)(screenY * 0.075f);
        driverImageHolderParams.height = (int)(screenY * 0.075f);
        driverImageParams.width = (int)(screenY * 0.075f);
        driverImageParams.height = (int)(screenY * 0.075f);
        driverNameText.setTextSize((int)(screenX * 0.009f));
        driverNameTextParams.topMargin = (int)(screenY * 0.002f);
    }

    private void initializeRatingDisplay() {
        RelativeLayout.LayoutParams ratingContainerParams = (RelativeLayout.LayoutParams)ratingContainer.getLayoutParams();
        RelativeLayout.LayoutParams ratingIconParams = (RelativeLayout.LayoutParams)ratingIcon.getLayoutParams();
        RelativeLayout.LayoutParams ratingTextParams = (RelativeLayout.LayoutParams)ratingText.getLayoutParams();
        ratingContainerParams.topMargin = (int)(screenY * 0.002f);
        ratingIconParams.width = (int)(screenY * 0.025f);
        ratingIconParams.height = (int)(screenY * 0.025f);
        ratingText.setTextSize(screenX * 0.008f);
        ratingIconParams.setMarginStart((int)(screenX * 0.01f));
        ratingTextParams.setMarginStart((int)(screenX * 0.01f));
        ratingTextParams.setMarginEnd((int)(screenX * 0.015f));
        //ratingContainer.setPadding(0, (int)(screenY * 0.004f), 0, (int)(screenY * 0.004f));
    }

    private void initializeMessageButtons() {
        RelativeLayout.LayoutParams messageButtonsContainerParams = (RelativeLayout.LayoutParams)messageButtonsContainer.getLayoutParams();
        RelativeLayout.LayoutParams messageButtonParams = (RelativeLayout.LayoutParams)messageButton.getLayoutParams();
        RelativeLayout.LayoutParams messengerButtonContainerParams = (RelativeLayout.LayoutParams)messengerButtonContainer.getLayoutParams();
        RelativeLayout.LayoutParams messengerButtonParams = (RelativeLayout.LayoutParams)messengerButton.getLayoutParams();
        RelativeLayout.LayoutParams messengerIconParams = (RelativeLayout.LayoutParams)messengerIcon.getLayoutParams();
        messageButtonsContainerParams.setMarginStart((int)(screenX * 0.05f));
        messageButtonParams.width = (int)(screenX * 0.4f);
        messengerButtonParams.width = (int)(screenX * 0.4f);
        messageButtonParams.height = (int)(screenY * 0.06f);
        messengerButtonParams.height = (int)(screenY * 0.06f);
        messengerButtonContainerParams.topMargin = (int)(screenY * 0.01f);
        messageButton.setTextSize(screenX * 0.013f);
        messengerButton.setTextSize(screenX * 0.007f);
        messengerIconParams.height = (int)(screenY * 0.035f);
        messengerIconParams.width = (int)(screenY * 0.035f);
        messengerIconParams.setMarginStart((int)(screenX * 0.03f));
        messengerButtonContainer.setVisibility(View.GONE);

        if (accessor == ACCESSOR_DRIVER) {
            messageButtonsContainer.setVisibility(View.GONE);
        }

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Phone Number", driverAccount.getPhoneNumber());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied " + driverAccount.getPhoneNumber() + " Clip Board", Toast.LENGTH_SHORT).show();

                messageButton.setText(driverAccount.getPhoneNumber());
            }
            });
    }

    private void initializePaymentMethodsDisplay() {
        boolean acceptsCash = false;
        boolean acceptsInAppPayments = false;

        if (accessor == ACCESSOR_DRIVER) {
            UserAccount userAccount = ((AppActivity)getActivity()).getUserAccount();
            acceptsCash = userAccount.doesAcceptCash();
            acceptsInAppPayments = userAccount.doesAcceptInAppPayments();
        }

        cashMethodIcon.setImageResource((driverAccount.doesAcceptCash()) ? R.drawable.check_mark_icon_2 : R.drawable.cross_icon_2);
        inappMethodIcon.setImageResource((driverAccount.doesAcceptInAppPayments()) ? R.drawable.check_mark_icon_2 : R.drawable.cross_icon_2);

        RelativeLayout.LayoutParams paymentMethodsContainerParams = (RelativeLayout.LayoutParams)paymentMethodsContainer.getLayoutParams();
        RelativeLayout.LayoutParams paymentMethodsTopParams = (RelativeLayout.LayoutParams)paymentMethodsTop.getLayoutParams();
        RelativeLayout.LayoutParams paymentMethodsBottomParams = (RelativeLayout.LayoutParams)paymentMethodsBottom.getLayoutParams();
        RelativeLayout.LayoutParams paymentMethodsDividerParams = (RelativeLayout.LayoutParams)paymentMethodsDivider.getLayoutParams();
        RelativeLayout.LayoutParams cashMethodIconParams = (RelativeLayout.LayoutParams)cashMethodIcon.getLayoutParams();
        RelativeLayout.LayoutParams inappMethodIconParams = (RelativeLayout.LayoutParams)inappMethodIcon.getLayoutParams();
        RelativeLayout.LayoutParams cashMethodHolderParams = (RelativeLayout.LayoutParams)cashMethodHolder.getLayoutParams();
        RelativeLayout.LayoutParams inappMethodHolderParams = (RelativeLayout.LayoutParams)inappMethodHolder.getLayoutParams();
        RelativeLayout.LayoutParams cashMethodTextParams = (RelativeLayout.LayoutParams)cashMethodText.getLayoutParams();
        RelativeLayout.LayoutParams inappMethodTextParams = (RelativeLayout.LayoutParams)inappMethodText.getLayoutParams();
        paymentMethodsContainerParams.topMargin = (int)(screenY * 0.02f);
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
    }

    private void initializeDriverPreferencesDisplay() {
        RelativeLayout.LayoutParams driverPreferencesTitleParams = (RelativeLayout.LayoutParams)driverPreferencesTitle.getLayoutParams();
        RelativeLayout.LayoutParams preferenceHolderParams = (RelativeLayout.LayoutParams)preferenceHolder.getLayoutParams();
        driverPreferencesTitleParams.topMargin = (int)(screenY * 0.02f);
        driverPreferencesTitle.setTextSize(screenX * 0.01f);
        preferenceHolderParams.topMargin = (int)(screenY * 0.015f);

        boolean[] prefersPreference = new boolean[UserAccount.NUM_OF_PREFERENCES];
        if (accessor == ACCESSOR_DRIVER) {
            UserAccount userAccount = ((AppActivity)getActivity()).getUserAccount();
            prefersPreference[0] = userAccount.doesPreferMusic();
            prefersPreference[1] = userAccount.doesPreferDrinks();
            prefersPreference[2] = userAccount.doesPreferExtraLuggage();
            prefersPreference[3] = userAccount.doesPreferPets();
        } else {
            prefersPreference[0] = true;
            prefersPreference[1] = false;
            prefersPreference[2] = false;
            prefersPreference[3] = false;
        }

        int[] preferenceIconResourceWhite = new int[UserAccount.NUM_OF_PREFERENCES];
        int[] preferenceIconResourceBlue = new int[UserAccount.NUM_OF_PREFERENCES];
        preferenceIconResourceWhite[0] = R.drawable.music_icon_white;
        preferenceIconResourceWhite[1] = R.drawable.drinks_icon_white;
        preferenceIconResourceWhite[2] = R.drawable.luggage_icon_white;
        preferenceIconResourceWhite[3] = R.drawable.pet_icon_white;
        preferenceIconResourceBlue[0] = R.drawable.music_icon_blue;
        preferenceIconResourceBlue[1] = R.drawable.drinks_icon_blue;
        preferenceIconResourceBlue[2] = R.drawable.luggage_icon_blue;
        preferenceIconResourceBlue[3] = R.drawable.pet_icon_blue;

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
            preferencesParams.setMarginStart((int)(screenX * 0.028f));
            preferencesIconsParams.width = (int)(screenX * 0.07f);
            preferencesIconsParams.height = (int)(screenX * 0.07f);
            //preferencesIconsParams.topMargin = (int)(screenY * 0.015f);
            preferencesTexts[i].setTextSize(screenX * 0.0065f);
            preferencesTexts[i].setPadding(0, 0, 0, (int)(screenY * 0.002f));
            preferencesIconsHoldersParams.height = (int)(screenX * 0.09f);

            Rect prefTextBounds = new Rect();
            Paint prefTextPaint = preferencesTexts[i].getPaint();
            prefTextPaint.getTextBounds(preferencesTexts[i].getText().toString(), 0, preferencesTexts[i].getText().toString().length(), prefTextBounds);

            preferencesNoTexts[i].setTextSize(screenX * 0.0065f);
            preferencesNoTexts[i].setPadding(0, 0, 0, -(int)(prefTextBounds.height() * 0.2f));
        }
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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    class GetBookingListServerRequest extends AsyncTask<Void, Void, String> {
        String[] filters;

        public GetBookingListServerRequest() {
            filters = null;
        }

        public GetBookingListServerRequest(String[] inputFilters) {
            filters = inputFilters;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            String string_url = getActivity().getString(R.string.get_booking_list_request_url);

            if (filters != null) {
                string_url += "?";

                for (String filter: filters) {
                    string_url += filter + "&";
                }
            }

            string_url += "&driverId=" + String.valueOf(ridePost.getOwnerUserId());

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
            initializeUserBookingList();
        }
    }

    class GetUserServerRequest extends AsyncTask<Void, Void, String> {

        public GetUserServerRequest() {}

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            String string_url = getActivity().getString(R.string.get_user_request_url);


            string_url += "?UserId=" + String.valueOf(ridePost.getOwnerUserId());

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
                driverAccount = new UserAccount(driverArray.getInt("UserId"), driverArray.getInt("AccountType"), driverArray.getString("Name"), driverArray.getString("PhoneNumber"));
                driverAccount.setFacebookAccountNumber(driverArray.getString("FacebookAccountNumber"));
                driverAccount.setFacebookProfileLinkURI(driverArray.getString("FacebookProfileLinkURI"));
                driverAccount.setFacebookProfilePicURI(driverArray.getString("FacebookProfilePicURI"));
                driverAccount.setAcceptsCash(driverArray.getInt("AcceptsCash"));
                driverAccount.setAcceptsInAppPayments(driverArray.getInt("AcceptsInAppPayments"));

                sendNotificationIfBookedSuccessful();
                initializeDriverProfile();
                initializePaymentMethodsDisplay();

            } catch (JSONException e) {
                System.out.println(e);
            }

        }
    }
}
