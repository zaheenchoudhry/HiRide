package me.zaheenchoudhry.rideandgo;

import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class RideDetailFragment extends Fragment {

    public static final int ACCESSOR_DRIVER = 0;
    public static final int ACCESSOR_VIEWER = 1;
    public static final int ACCESSOR_PASSENGER = 2;

    private float screenX, screenY;
    private int accessor;

    private String[] dayOfMonth = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private String[] dayOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    private RidePost ridePost;

    private RelativeLayout rideDetailContainer, menuButton, borderBottom, rideSeparator;
    private RelativeLayout dateContainer, timeContainer, rideCitiesContainer;
    private RelativeLayout priceContainer, rideButtonsContainer, pickupContainer, dropoffContainer;
    private RelativeLayout ratingContainer, messageButtonsContainer, messengerButtonContainer;
    private RelativeLayout profileAndMessageContainer, paymentMethodsContainer, paymentMethodsTop, paymentMethodsBottom;
    private RelativeLayout cashMethodHolder, inappMethodHolder, paymentMethodsDivider, preferenceHolder;
    private RelativeLayout[] preferences, preferencesIconsHolders;
    private TextView dateDayText, dateDateText, dateMonthText, timeText, timeAmPmText;
    private TextView startCityText, endCityText, priceText, driverNameText, ratingText;
    private TextView pickupTitleText, dropoffTitleText, pickupAddressText, dropoffAddressText;
    private TextView paymentMethodsTitleText, cashMethodText, inappMethodText, driverPreferencesTitle;
    private TextView[] preferencesTexts, preferencesNoTexts;
    private ImageView cityArrow, driverImage, ratingIcon, messengerIcon, cashMethodIcon, inappMethodIcon;
    private ImageView[] preferencesIcons;
    private Button shortlistButton, bookRideButton, messageButton, messengerButton;

    public RideDetailFragment(int accessor, RidePost ridePost) {
        this.accessor = accessor;
        this.ridePost = ridePost;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppActivity)getActivity()).setCurrentPageNumber(AppActivity.RIDE_DETAIL_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_detail_fragment, container, false);
        setUnit();

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
        pickupContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_pickup_container);
        dropoffContainer = (RelativeLayout)view.findViewById(R.id.ride_detail_dropoff_container);
        pickupTitleText = (TextView)view.findViewById(R.id.ride_detail_pickup_title);
        dropoffTitleText = (TextView)view.findViewById(R.id.ride_detail_dropoff_title);
        pickupAddressText = (TextView)view.findViewById(R.id.ride_detail_pickup_address);
        dropoffAddressText = (TextView)view.findViewById(R.id.ride_detail_dropoff_address);
        rideSeparator = (RelativeLayout)view.findViewById(R.id.ride_detail_ride_separator);
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
        initializeRideSeparator();
        initializeDriverProfile();
        initializeRatingDisplay();
        initializeMessageButtons();
        initializePaymentMethodsDisplay();
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
        timeText.setTextSize(screenX * 0.011f);
        timeAmPmText.setTextSize(screenX * 0.011f);
        timeAmPmTextParams.setMarginStart((int)(screenX * 0.01f));
    }

    private void initializeCitiesDisplay() {
        RelativeLayout.LayoutParams rideCitiesContainerParams = (RelativeLayout.LayoutParams)rideCitiesContainer.getLayoutParams();
        RelativeLayout.LayoutParams cityArrowParams = (RelativeLayout.LayoutParams)cityArrow.getLayoutParams();
        RelativeLayout.LayoutParams endCityTextParams = (RelativeLayout.LayoutParams)endCityText.getLayoutParams();
        rideCitiesContainerParams.topMargin = (int)(screenY * 0.003f);
        startCityText.setTextSize(screenX * 0.0125f);
        endCityText.setTextSize(screenX * 0.0125f);
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
        bookRideButtonParams.width = (int)(screenX * 0.42f);
        shortlistButtonParams.height = (int)(screenY * 0.06f);
        bookRideButtonParams.height = (int)(screenY * 0.06f);
        bookRideButtonParams.setMarginStart((int)(screenX * 0.02f));
        shortlistButton.setTextSize(screenX * 0.007f);
        bookRideButton.setTextSize(screenX * 0.007f);
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

    private void initializeRideSeparator() {
        RelativeLayout.LayoutParams rideSeparatorParams = (RelativeLayout.LayoutParams)rideSeparator.getLayoutParams();
        rideSeparatorParams.height = (int)(screenY * 0.01f);
        rideSeparatorParams.topMargin = (int)(screenY * 0.01f);
    }

    private void initializeDriverProfile() {
        RelativeLayout.LayoutParams profileAndMessageContainerParams = (RelativeLayout.LayoutParams)profileAndMessageContainer.getLayoutParams();
        RelativeLayout.LayoutParams driverImageParams = (RelativeLayout.LayoutParams)driverImage.getLayoutParams();
        RelativeLayout.LayoutParams driverNameTextParams = (RelativeLayout.LayoutParams)driverNameText.getLayoutParams();
        profileAndMessageContainerParams.topMargin = (int)(screenY * 0.02f);
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
        messageButton.setTextSize(screenX * 0.007f);
        messengerButton.setTextSize(screenX * 0.007f);
        messengerIconParams.height = (int)(screenY * 0.035f);
        messengerIconParams.width = (int)(screenY * 0.035f);
        messengerIconParams.setMarginStart((int)(screenX * 0.03f));

        if (accessor == ACCESSOR_DRIVER) {
            messageButtonsContainer.setVisibility(View.GONE);
        }
    }

    private void initializePaymentMethodsDisplay() {
        boolean acceptsCash = false;
        boolean acceptsInAppPayments = false;

        if (accessor == ACCESSOR_DRIVER) {
            UserAccount userAccount = ((AppActivity)getActivity()).getUserAccount();
            acceptsCash = userAccount.doesAcceptCash();
            acceptsInAppPayments = userAccount.doesAcceptInAppPayments();
        }

        cashMethodIcon.setImageResource((acceptsCash) ? R.drawable.check_mark_icon_2 : R.drawable.cross_icon_2);
        inappMethodIcon.setImageResource((acceptsInAppPayments) ? R.drawable.check_mark_icon_2 : R.drawable.cross_icon_2);

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
}
