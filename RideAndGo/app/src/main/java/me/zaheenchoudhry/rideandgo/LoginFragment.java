package me.zaheenchoudhry.rideandgo;

import android.app.Fragment;
import android.content.Intent;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

public class LoginFragment extends Fragment {

    private final int NUMBER_OF_INPUTS = 2;
    private final int EMAIL_INPIT = 0;
    private final int PASSWORD_INPUT = 1;

    private float screenX, screenY;

    private TextView loginTitleText, forgotPasswordText, facebookLoginText, loginButtonsDividerText;
    private EditText[] loginInputs;
    private ImageView[] loginInputsIcons, loginButtonsDivider;
    private RelativeLayout[] loginInputsHolders;
    private Button loginButton, facebookLoginButton;
    private LinearLayout loginInputsHolder;
    private RelativeLayout facebookLoginButtonHolder, loginButtonsDividerHolder;
    private ImageView facebookLoginImage;

    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        setUnit();

        loginInputs = new EditText[NUMBER_OF_INPUTS];
        loginInputsIcons = new ImageView[NUMBER_OF_INPUTS];
        loginInputsHolders = new RelativeLayout[NUMBER_OF_INPUTS];
        loginButtonsDivider = new ImageView[2];

        loginTitleText = (TextView)view.findViewById(R.id.login_title_text);
        loginButton = (Button)view.findViewById(R.id.login_button);
        forgotPasswordText = (TextView)view.findViewById(R.id.login_forgot_password_text);
        loginInputs[0] = (EditText)view.findViewById(R.id.login_email_input);
        loginInputs[1] = (EditText)view.findViewById(R.id.login_password_input);
        loginInputsIcons[0] = (ImageView)view.findViewById(R.id.login_email_icon);
        loginInputsIcons[1] = (ImageView)view.findViewById(R.id.login_password_icon);
        loginInputsHolders[0] = (RelativeLayout)view.findViewById(R.id.login_email_holder);
        loginInputsHolders[1] = (RelativeLayout)view.findViewById(R.id.login_password_holder);
        loginInputsHolder = (LinearLayout)view.findViewById(R.id.login_inputs_holder);
        facebookLoginButton = (Button)view.findViewById(R.id.facebook_login_button);
        facebookLoginText = (TextView)view.findViewById(R.id.facebook_login_text);
        facebookLoginImage = (ImageView)view.findViewById(R.id.facebook_login_image);
        facebookLoginButtonHolder = (RelativeLayout)view.findViewById(R.id.facebook_login_button_holder);
        loginButtonsDividerText = (TextView)view.findViewById(R.id.login_buttons_divider_text);
        loginButtonsDivider[0] = (ImageView)view.findViewById(R.id.login_buttons_divider_1);
        loginButtonsDivider[1] = (ImageView)view.findViewById(R.id.login_buttons_divider_2);
        loginButtonsDividerHolder = (RelativeLayout)view.findViewById(R.id.login_buttons_divider_holder);

        initializeTitleText();
        initializeInputs();
        initializeForgotPasswordText();
        initializeLoginButton();
        initializeButtonsDivider();
        initializeFacebookLoginButton();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setUnit() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
    }

    private void initializeTitleText() {
        RelativeLayout.LayoutParams loginTitleTextParams = (RelativeLayout.LayoutParams)loginTitleText.getLayoutParams();
        loginTitleText.setTextSize(screenX * 0.018f);
        loginTitleTextParams.topMargin = (int)(screenY * 0.15f);
    }

    private void initializeInputs() {
        RelativeLayout.LayoutParams loginInputsHolderParams = (RelativeLayout.LayoutParams)loginInputsHolder.getLayoutParams();
        loginInputsHolderParams.topMargin = (int)(screenY * 0.25f);

        for (int i = 0; i < NUMBER_OF_INPUTS; ++i) {
            RelativeLayout.LayoutParams loginInputIconsParams = (RelativeLayout.LayoutParams)loginInputsIcons[i].getLayoutParams();
            loginInputIconsParams.width = (int)(screenX * 0.08f);
            loginInputIconsParams.height = (int)(screenX * 0.08f);
            loginInputsIcons[i].setPadding((int)(screenX * 0.035f), 0, 0, 0);

            LinearLayout.LayoutParams loginInputsHoldersParams = (LinearLayout.LayoutParams)loginInputsHolders[i].getLayoutParams();
            loginInputsHoldersParams.topMargin = (int)(screenY * 0.02f);
            loginInputs[i].setWidth((int)(screenX * 0.8f));
            loginInputs[i].setPadding((int)(screenX * 0.125f), (int)(screenX * 0.035f), (int)(screenX * 0.0325f), (int)(screenX * 0.035f));
        }
    }

    private void initializeForgotPasswordText() {
        RelativeLayout.LayoutParams forgotPasswordTextParams = (RelativeLayout.LayoutParams)forgotPasswordText.getLayoutParams();
        forgotPasswordText.setTextSize(screenX * 0.01f);
        forgotPasswordTextParams.topMargin = (int)(screenY * 0.50f);
    }

    private void initializeLoginButton() {
        RelativeLayout.LayoutParams loginButtonParams = (RelativeLayout.LayoutParams)loginButton.getLayoutParams();
        loginButtonParams.height = (int)(screenY * 0.08f);
        loginButtonParams.width = (int)(screenX * 0.6f);
        loginButtonParams.topMargin = (int)(screenY * 0.6f);
        loginButton.setTextSize(screenX * 0.008f);
    }

    private void initializeButtonsDivider() {
        RelativeLayout.LayoutParams loginButtonsDividerHolderParams = (RelativeLayout.LayoutParams)loginButtonsDividerHolder.getLayoutParams();
        loginButtonsDividerHolderParams.topMargin = (int)(screenY * 0.715f);
        RelativeLayout.LayoutParams loginButtonsDividerParams1 = (RelativeLayout.LayoutParams)loginButtonsDivider[0].getLayoutParams();
        RelativeLayout.LayoutParams loginButtonsDividerParams2 = (RelativeLayout.LayoutParams)loginButtonsDivider[1].getLayoutParams();
        loginButtonsDividerParams1.width = (int)(screenX * 0.25f);
        loginButtonsDividerParams2.width = (int)(screenX * 0.25f);
        loginButtonsDividerParams1.height = (int)(screenY * 0.05f);
        loginButtonsDividerParams2.height = (int)(screenY * 0.05f);
        loginButtonsDividerParams2.setMarginStart((int)(screenX * 0.35f));
        loginButtonsDividerText.setTextSize((int)(screenX * 0.012f));
    }

    private void initializeFacebookLoginButton() {
        RelativeLayout.LayoutParams facebookLoginButtonParams = (RelativeLayout.LayoutParams)facebookLoginButton.getLayoutParams();
        RelativeLayout.LayoutParams facebookLoginButtonHolderParams = (RelativeLayout.LayoutParams)facebookLoginButtonHolder.getLayoutParams();
        RelativeLayout.LayoutParams facebookLoginImageParams = (RelativeLayout.LayoutParams)facebookLoginImage.getLayoutParams();
        RelativeLayout.LayoutParams facebookLoginTextParams = (RelativeLayout.LayoutParams)facebookLoginText.getLayoutParams();
        facebookLoginButtonParams.height = (int)(screenY * 0.08f);
        facebookLoginButtonParams.width = (int)(screenX * 0.6f);
        facebookLoginButtonHolderParams.topMargin = (int)(screenY * 0.8f);
        facebookLoginImageParams.width = (int)(screenY * 0.05f);
        facebookLoginImageParams.height = (int)(screenY * 0.05f);
        facebookLoginText.setTextSize(screenX * 0.008f);
        facebookLoginTextParams.setMarginStart((int)(screenY * 0.07f));

        //facebookLoginButton.setReadPermissions("email");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, Arrays.asList("public_profile", "email"));
            }
        });
    }
}
