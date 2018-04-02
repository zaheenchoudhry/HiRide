package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class IntroFragment extends Fragment {

    private float screenX, screenY;

    private TextView buttonsDividerText, termsMessage, termsText;
    private ImageView[] buttonsDivider;
    private Button facebookLoginButton, loginButton, signupButton;
    private RelativeLayout buttonsDividerHolder, termsTextHolder;
    private RelativeLayout intoLogoHolder, introButtonHolder;
    private LinearLayout loginSignupButtonsContainer;
    private ImageView hirideLogo, facebookLoginImage;

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment, container, false);
        setUnit();

        buttonsDivider = new ImageView[2];

        intoLogoHolder = (RelativeLayout)view.findViewById(R.id.intro_logo_holder);
        introButtonHolder = (RelativeLayout)view.findViewById(R.id.intro_buttons_holder);
        loginSignupButtonsContainer = (LinearLayout)view.findViewById(R.id.login_signup_buttons_container);
        loginButton = (Button)view.findViewById(R.id.intro_login_button);
        signupButton = (Button)view.findViewById(R.id.intro_signup_button);
        buttonsDividerText = (TextView)view.findViewById(R.id.intro_buttons_divider_text);
        buttonsDivider[0] = (ImageView)view.findViewById(R.id.intro_buttons_divider_1);
        buttonsDivider[1] = (ImageView)view.findViewById(R.id.intro_buttons_divider_2);
        buttonsDividerHolder = (RelativeLayout)view.findViewById(R.id.intro_buttons_divider_holder);
        facebookLoginButton = (Button)view.findViewById(R.id.intro_facebook_login_button);
        facebookLoginImage = (ImageView)view.findViewById(R.id.intro_facebook_login_image);
        termsMessage = (TextView)view.findViewById(R.id.terms_message);
        termsText = (TextView)view.findViewById(R.id.terms_text);
        termsTextHolder = (RelativeLayout)view.findViewById(R.id.terms_text_holder);
        hirideLogo = (ImageView) view.findViewById(R.id.intro_hiride_logo);

        initializeLogoHolder();
        initializeContentHolder();
        initializeLogo();
        initializeLoginSignupButtonsHolder();
        initializeButtonsDivider();
        initializeFacebookLoginButton();
        initializeTermsText();

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

    private void initializeLogoHolder() {
        ViewGroup.LayoutParams intoLogoHolderParams = intoLogoHolder.getLayoutParams();
        intoLogoHolderParams.height = (int)(screenY * 0.65f);
    }

    private void initializeContentHolder() {
        ViewGroup.LayoutParams introButtonHolderParams = introButtonHolder.getLayoutParams();
        introButtonHolderParams.height = (int)(screenY * 0.35f);
    }

    private void initializeLogo() {
        RelativeLayout.LayoutParams hirideLogoParams = (RelativeLayout.LayoutParams)hirideLogo.getLayoutParams();
        hirideLogoParams.topMargin = (int)(screenY * 0.17f);
        hirideLogoParams.width = (int)(screenX * 0.5f);
    }

    private void initializeLoginSignupButtonsHolder() {
        RelativeLayout.LayoutParams loginSignupButtonsContainerParams = (RelativeLayout.LayoutParams)loginSignupButtonsContainer.getLayoutParams();
        LinearLayout.LayoutParams loginButtonParams = (LinearLayout.LayoutParams)loginButton.getLayoutParams();
        LinearLayout.LayoutParams signupButtonParams = (LinearLayout.LayoutParams)signupButton.getLayoutParams();

        loginSignupButtonsContainerParams.topMargin = (int)(screenY * 0.03f);
        loginButtonParams.height = (int)(screenY * 0.08f);
        loginButtonParams.width = (int)(screenX * 0.39f);
        signupButtonParams.height = (int)(screenY * 0.08f);
        signupButtonParams.width = (int)(screenX * 0.39f);
        signupButtonParams.leftMargin = (int)(screenX * 0.02f);
        loginButton.setTextSize(screenX * 0.008f);
        signupButton.setTextSize(screenX * 0.008f);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.login_signup_fragment_container, loginFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpFragment signupFragment = new SignUpFragment();
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                transaction.replace(R.id.login_signup_fragment_container, signupFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initializeButtonsDivider() {
        RelativeLayout.LayoutParams buttonsDividerHolderParams = (RelativeLayout.LayoutParams)buttonsDividerHolder.getLayoutParams();
        RelativeLayout.LayoutParams buttonsDividerParams1 = (RelativeLayout.LayoutParams)buttonsDivider[0].getLayoutParams();
        RelativeLayout.LayoutParams buttonsDividerParams2 = (RelativeLayout.LayoutParams)buttonsDivider[1].getLayoutParams();
        buttonsDividerHolderParams.topMargin = (int)(screenY * 0.12f);
        buttonsDividerParams1.width = (int)(screenX * 0.32f);
        buttonsDividerParams2.width = (int)(screenX * 0.32f);
        buttonsDividerParams1.height = (int)(screenY * 0.05f);
        buttonsDividerParams2.height = (int)(screenY * 0.05f);
        buttonsDividerParams2.setMarginStart((int)(screenX * 0.45f));
        buttonsDividerText.setTextSize((int)(screenX * 0.012f));
    }

    private void initializeFacebookLoginButton() {
        RelativeLayout.LayoutParams facebookLoginButtonParams = (RelativeLayout.LayoutParams)facebookLoginButton.getLayoutParams();
        RelativeLayout.LayoutParams facebookLoginImageParams = (RelativeLayout.LayoutParams)facebookLoginImage.getLayoutParams();
        facebookLoginButtonParams.height = (int)(screenY * 0.08f);
        facebookLoginButtonParams.width = (int)(screenX * 0.8f);
        facebookLoginButtonParams.topMargin = (int)(screenY * 0.185f);
        facebookLoginButton.setTextSize(screenX * 0.008f);
        facebookLoginImageParams.width = (int)(screenY * 0.05f);
        facebookLoginImageParams.height = (int)(screenY * 0.05f);
        facebookLoginImageParams.topMargin = (int)(screenY * 0.2f);
        facebookLoginImageParams.setMarginStart((int)(screenX * 0.1f + screenY * 0.02f));

        //facebookLoginButton.setReadPermissions("email");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                attemptContinueWithFacebook();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.setTitle("Something Went Wrong");
                alertDialog.setMessage("Could not Log In");
                alertDialog.show();
            }
        });

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    LoginManager.getInstance().logInWithReadPermissions(IntroFragment.this, Arrays.asList("public_profile", "email"));
                } else {
                    attemptContinueWithFacebook();
                }
            }
        });
    }

    private void initializeTermsText() {
        RelativeLayout.LayoutParams termsTextHolderParams = (RelativeLayout.LayoutParams)termsTextHolder.getLayoutParams();
        RelativeLayout.LayoutParams termsTextParams = (RelativeLayout.LayoutParams)termsText.getLayoutParams();
        termsMessage.setTextSize(screenX * 0.008f);
        termsText.setTextSize(screenX * 0.008f);
        termsTextHolderParams.topMargin = (int)(screenY * 0.285f);
        termsTextParams.topMargin = (int)(screenY * 0.025f);

        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/e/2PACX-1vQby43JG6qh3OV5jMC8m7iBh96hI68e5quLX-AqGXLgbBr4264Ice1EaBUIN4JgnDdC3aXWCF4Oim7k/pub"));
                startActivity(browser);
            }
        });

    }

    private void attemptContinueWithFacebook() {
        if (Profile.getCurrentProfile() == null) {
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    attemptFacebookServerRequest();
                }
            };
        } else {
            attemptFacebookServerRequest();
        }
    }

    private void attemptFacebookServerRequest() {
        //AccessToken accessToken = loginResult.getAccessToken();
        Profile profile = Profile.getCurrentProfile();
        String name = profile.getName();
        String facebookAccountNumber = profile.getId();
        String facebookProfileLinkURI = profile.getLinkUri().toString();
        String facebookProfilePicURI= profile.getProfilePictureUri(128, 128).toString();

        FacebookLoginSignupServerRequest facebookLoginSignupServerRequest = new FacebookLoginSignupServerRequest(getActivity(), true);
        facebookLoginSignupServerRequest.execute(name, facebookAccountNumber, facebookProfileLinkURI, facebookProfilePicURI);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (profileTracker != null && profileTracker.isTracking()) {
            profileTracker.stopTracking();
        }
    }
}
