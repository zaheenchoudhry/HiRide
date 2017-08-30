package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignUpFragment extends Fragment {

    public static final int VALID_PHONE_NUMBER_DIGITS = 10;
    public static final int NUMBER_OF_INPUTS = 5;
    public static final int FULL_NAME_INPUT = 0;
    public static final int PHONE_NUMBER_INPUT = 1;
    public static final int EMAIL_INPUT = 2;
    public static final int PASSWORD_INPUT = 3;
    public static final int PASSWORD_CONFIRM_INPUT = 4;

    private float screenX, screenY;

    private TextView signupTitleText, signupTermsMessage, signupTermsText;
    private EditText[] signupInputs;
    private ImageView[] signupInputsIcons;
    private RelativeLayout[] signupInputsHolders;
    private Button signupButton;
    private LinearLayout signupInputsHolder;
    private RelativeLayout signupTermsTextHolder;
    private ImageView backPageArrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);
        setUnit();

        signupInputs = new EditText[NUMBER_OF_INPUTS];
        signupInputsIcons = new ImageView[NUMBER_OF_INPUTS];
        signupInputsHolders = new RelativeLayout[NUMBER_OF_INPUTS];

        signupTitleText = (TextView)view.findViewById(R.id.signup_title_text);
        signupButton = (Button)view.findViewById(R.id.signup_button);
        signupTermsMessage = (TextView)view.findViewById(R.id.signup_terms_message);
        signupTermsText = (TextView)view.findViewById(R.id.signup_terms_text);
        signupTermsTextHolder = (RelativeLayout)view.findViewById(R.id.signup_terms_text_holder);
        signupInputs[0] = (EditText)view.findViewById(R.id.signup_full_name_input);
        signupInputs[1] = (EditText)view.findViewById(R.id.signup_phone_number_input);
        signupInputs[2] = (EditText)view.findViewById(R.id.signup_email_input);
        signupInputs[3] = (EditText)view.findViewById(R.id.signup_password_input);
        signupInputs[4] = (EditText)view.findViewById(R.id.signup_confirm_password_input);
        signupInputsIcons[0] = (ImageView)view.findViewById(R.id.signup_full_name_icon);
        signupInputsIcons[1] = (ImageView)view.findViewById(R.id.signup_phone_number_icon);
        signupInputsIcons[2] = (ImageView)view.findViewById(R.id.signup_email_icon);
        signupInputsIcons[3] = (ImageView)view.findViewById(R.id.signup_password_icon);
        signupInputsIcons[4] = (ImageView)view.findViewById(R.id.signup_confirm_password_icon);
        signupInputsHolders[0] = (RelativeLayout)view.findViewById(R.id.signup_full_name_holder);
        signupInputsHolders[1] = (RelativeLayout)view.findViewById(R.id.signup_phone_number_holder);
        signupInputsHolders[2] = (RelativeLayout)view.findViewById(R.id.signup_email_holder);
        signupInputsHolders[3] = (RelativeLayout)view.findViewById(R.id.signup_password_holder);
        signupInputsHolders[4] = (RelativeLayout)view.findViewById(R.id.signup_confirm_password_holder);
        signupInputsHolder = (LinearLayout)view.findViewById(R.id.signup_inputs_holder);
        backPageArrow = (ImageView)view.findViewById(R.id.signup_back_arrow);

        initializeBackPageArrow();
        initializeTitleText();
        initializeInputs();
        initializeSignUpButton();
        initializeTermsText();

        return view;
    }

    private void setUnit() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
    }

    private void initializeBackPageArrow() {
        backPageArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                if (fragmentManager.getBackStackEntryCount() > 0 ){
                    fragmentManager.beginTransaction().commit();
                    fragmentManager.popBackStack();
                } else {
                    IntroFragment introFragment = new IntroFragment();
                    FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                    transaction.replace(R.id.login_signup_fragment_container, introFragment);
                    transaction.commit();
                }
            }
        });
    }

    private void initializeTitleText() {
        RelativeLayout.LayoutParams signupTitleTextParams = (RelativeLayout.LayoutParams)signupTitleText.getLayoutParams();
        signupTitleText.setTextSize(screenX * 0.018f);
        signupTitleTextParams.topMargin = (int)(screenY * 0.15f);
    }

    private void initializeInputs() {
        RelativeLayout.LayoutParams signupInputsHolderParams = (RelativeLayout.LayoutParams)signupInputsHolder.getLayoutParams();
        signupInputsHolderParams.topMargin = (int)(screenY * 0.25f);

        for (int i = 0; i < NUMBER_OF_INPUTS; ++i) {
            RelativeLayout.LayoutParams signupInputIconsParams = (RelativeLayout.LayoutParams)signupInputsIcons[i].getLayoutParams();
            signupInputIconsParams.width = (int)(screenX * 0.08f);
            signupInputIconsParams.height = (int)(screenX * 0.08f);
            signupInputsIcons[i].setPadding((int)(screenX * 0.035f), 0, 0, 0);

            LinearLayout.LayoutParams signupInputsHoldersParams = (LinearLayout.LayoutParams)signupInputsHolders[i].getLayoutParams();
            signupInputsHoldersParams.topMargin = (int)(screenY * 0.02f);
            signupInputs[i].setWidth((int)(screenX * 0.8f));
            signupInputs[i].setPadding((int)(screenX * 0.125f), (int)(screenX * 0.025f), (int)(screenX * 0.0325f), (int)(screenX * 0.025f));
        }

        signupInputs[SignUpFragment.PHONE_NUMBER_INPUT].setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    signupInputs[SignUpFragment.PHONE_NUMBER_INPUT].setText(getNumericPhoneNumber(signupInputs[SignUpFragment.PHONE_NUMBER_INPUT].getText().toString()));
                } else {
                    signupInputs[SignUpFragment.PHONE_NUMBER_INPUT].setText(formatPhoneNumber(signupInputs[SignUpFragment.PHONE_NUMBER_INPUT].getText().toString()));
                }
            }
        });
    }

    private void initializeSignUpButton() {
        RelativeLayout.LayoutParams signUpButtonParams = (RelativeLayout.LayoutParams)signupButton.getLayoutParams();
        signUpButtonParams.height = (int)(screenY * 0.08f);
        signUpButtonParams.width = (int)(screenX * 0.6f);
        signUpButtonParams.topMargin = (int)(screenY * 0.80f);
        signupButton.setTextSize(screenX * 0.008f);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                if (signupInputs[SignUpFragment.FULL_NAME_INPUT].getText().toString().equals("")) {
                    alertDialog.setTitle("Invalid Name Input");
                    alertDialog.setMessage("The name field cannot be blank");
                    alertDialog.show();
                } else if (getNumericPhoneNumber(signupInputs[SignUpFragment.PHONE_NUMBER_INPUT].getText().toString()).length() != VALID_PHONE_NUMBER_DIGITS) {
                    alertDialog.setTitle("Invalid Phone Number");
                    alertDialog.setMessage("The phone number entered is invalid");
                    alertDialog.show();
                } else if (!isEmailValid(signupInputs[SignUpFragment.EMAIL_INPUT].getText())) {
                    alertDialog.setTitle("Invalid Email ID");
                    alertDialog.setMessage("The Email ID entered is invalid");
                    alertDialog.show();
                } else if (signupInputs[SignUpFragment.PASSWORD_INPUT].getText().toString().length() < 8) {
                    alertDialog.setTitle("Invalid Password");
                    alertDialog.setMessage("Password must be at least 8 characters long");
                    alertDialog.show();
                } else if (!signupInputs[SignUpFragment.PASSWORD_INPUT].getText().toString().equals(signupInputs[SignUpFragment.PASSWORD_CONFIRM_INPUT].getText().toString())) {
                    alertDialog.setTitle("Invalid Password");
                    alertDialog.setMessage("Passwords do not match");
                    alertDialog.show();
                } else {
                    String accountType = "0";
                    String name = signupInputs[SignUpFragment.FULL_NAME_INPUT].getText().toString();
                    String phoneNumber = getNumericPhoneNumber(signupInputs[SignUpFragment.PHONE_NUMBER_INPUT].getText().toString());
                    String email = signupInputs[SignUpFragment.EMAIL_INPUT].getText().toString();
                    String password = signupInputs[SignUpFragment.PASSWORD_INPUT].getText().toString();
                    String facebookAccountNumber = "";
                    String facebookProfilePicURI = "";

                    CreateAccountServerRequest createAccountServerRequest = new CreateAccountServerRequest(getActivity());
                    createAccountServerRequest.execute(accountType, name, phoneNumber, email, password, facebookAccountNumber, facebookProfilePicURI);
                }
            }
        });
    }

    private void initializeTermsText() {
        RelativeLayout.LayoutParams signupTermsTextHolderParams = (RelativeLayout.LayoutParams)signupTermsTextHolder.getLayoutParams();
        RelativeLayout.LayoutParams signupTermsTextParams = (RelativeLayout.LayoutParams)signupTermsText.getLayoutParams();
        signupTermsMessage.setTextSize(screenX * 0.008f);
        signupTermsText.setTextSize(screenX * 0.008f);
        signupTermsTextHolderParams.topMargin = (int)(screenY * 0.90f);
        signupTermsTextParams.topMargin = (int)(screenY * 0.03f);
    }

    public static boolean isEmailValid(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static String getNumericPhoneNumber(String phoneNumber) {
        String numericPhoneNumber = "";
        for (int i = 0; i < phoneNumber.length(); ++i) {
            if (Character.isDigit(phoneNumber.charAt(i))) {
                numericPhoneNumber += phoneNumber.substring(i, i + 1);
            }
        }
        return numericPhoneNumber;
    }

    public static String formatPhoneNumber(String phoneNumber) {
        String numericPhoneNumber = getNumericPhoneNumber(phoneNumber);

        if (numericPhoneNumber.length() == VALID_PHONE_NUMBER_DIGITS) {
            String formattedPhoneNumber = "(" + numericPhoneNumber.substring(0, 3) + ") " + numericPhoneNumber.substring(3, 6) + "-" + numericPhoneNumber.substring(6);
            return formattedPhoneNumber;
        }

        return numericPhoneNumber;
    }
}
