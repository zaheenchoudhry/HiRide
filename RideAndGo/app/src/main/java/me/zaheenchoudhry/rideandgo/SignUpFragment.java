package me.zaheenchoudhry.rideandgo;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
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

    private final int NUMBER_OF_INPUTS = 4;
    private final int NAME_INPUT = 0;
    private final int EMAIL_INPIT = 1;
    private final int PASSWORD_INPUT = 2;
    private final int PASSWORD_CONFIRM_INPUT = 3;

    private float screenX, screenY;

    private TextView signupTitleText, signupTermsMessage, signupTermsText;
    private EditText[] signupInputs;
    private ImageView[] signupInputsIcons;
    private RelativeLayout[] signupInputsHolders;
    private Button signupButton;
    private LinearLayout signupInputsHolder;
    private RelativeLayout signupTermsTextHolder;

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
        signupInputs[0] = (EditText)view.findViewById(R.id.signup_name_input);
        signupInputs[1] = (EditText)view.findViewById(R.id.signup_email_input);
        signupInputs[2] = (EditText)view.findViewById(R.id.signup_password_input);
        signupInputs[3] = (EditText)view.findViewById(R.id.signup_confirm_password_input);
        signupInputsIcons[0] = (ImageView)view.findViewById(R.id.signup_name_icon);
        signupInputsIcons[1] = (ImageView)view.findViewById(R.id.signup_email_icon);
        signupInputsIcons[2] = (ImageView)view.findViewById(R.id.signup_password_icon);
        signupInputsIcons[3] = (ImageView)view.findViewById(R.id.signup_confirm_password_icon);
        signupInputsHolders[0] = (RelativeLayout)view.findViewById(R.id.signup_name_holder);
        signupInputsHolders[1] = (RelativeLayout)view.findViewById(R.id.signup_email_holder);
        signupInputsHolders[2] = (RelativeLayout)view.findViewById(R.id.signup_password_holder);
        signupInputsHolders[3] = (RelativeLayout)view.findViewById(R.id.signup_confirm_password_holder);
        signupInputsHolder = (LinearLayout)view.findViewById(R.id.signup_inputs_holder);

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

    private void initializeTitleText() {
        RelativeLayout.LayoutParams signupTitleTextParams = (RelativeLayout.LayoutParams)signupTitleText.getLayoutParams();
        signupTitleText.setTextSize(screenX * 0.018f);
        signupTitleTextParams.topMargin = (int)(screenY * 0.18f);
    }

    private void initializeInputs() {
        RelativeLayout.LayoutParams signupInputsHolderParams = (RelativeLayout.LayoutParams)signupInputsHolder.getLayoutParams();
        signupInputsHolderParams.topMargin = (int)(screenY * 0.30f);

        for (int i = 0; i < NUMBER_OF_INPUTS; ++i) {
            RelativeLayout.LayoutParams signupInputIconsParams = (RelativeLayout.LayoutParams)signupInputsIcons[i].getLayoutParams();
            signupInputIconsParams.width = (int)(screenX * 0.08f);
            signupInputIconsParams.height = (int)(screenX * 0.08f);
            signupInputsIcons[i].setPadding((int)(screenX * 0.035f), 0, 0, 0);

            LinearLayout.LayoutParams signupInputsHoldersParams = (LinearLayout.LayoutParams)signupInputsHolders[i].getLayoutParams();
            signupInputsHoldersParams.topMargin = (int)(screenY * 0.02f);
            signupInputs[i].setWidth((int)(screenX * 0.8f));
            signupInputs[i].setPadding((int)(screenX * 0.125f), (int)(screenX * 0.035f), (int)(screenX * 0.0325f), (int)(screenX * 0.035f));
        }
    }

    private void initializeSignUpButton() {
        RelativeLayout.LayoutParams signUpButtonParams = (RelativeLayout.LayoutParams)signupButton.getLayoutParams();
        signUpButtonParams.height = (int)(screenY * 0.08f);
        signUpButtonParams.width = (int)(screenX * 0.6f);
        signUpButtonParams.topMargin = (int)(screenY * 0.78f);
        signupButton.setTextSize(screenX * 0.008f);
    }

    private void initializeTermsText() {
        RelativeLayout.LayoutParams signupTermsTextHolderParams = (RelativeLayout.LayoutParams)signupTermsTextHolder.getLayoutParams();
        RelativeLayout.LayoutParams signupTermsTextParams = (RelativeLayout.LayoutParams)signupTermsText.getLayoutParams();
        signupTermsMessage.setTextSize(screenX * 0.008f);
        signupTermsText.setTextSize(screenX * 0.008f);
        signupTermsTextHolderParams.topMargin = (int)(screenY * 0.88f);
        signupTermsTextParams.topMargin = (int)(screenY * 0.03f);
    }
}
