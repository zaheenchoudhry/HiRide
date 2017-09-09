package me.zaheenchoudhry.rideandgo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

public class LoginSignupActivity extends Activity {

    private float screenX, screenY;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();

        SharedPreferences prefs =  getApplicationContext().getSharedPreferences(getString(R.string.saved_data_preferences_name), Context.MODE_PRIVATE);

        boolean isLoggedInThroughEmail = prefs.getBoolean(getString(R.string.saved_data_is_logged_in_email_preference_name), false);
        boolean isLoggedInThroughFacebook = prefs.getBoolean(getString(R.string.saved_data_is_logged_in_facebook_preference_name), false);

        System.out.println("IS PROFILE NULL: " + (Profile.getCurrentProfile() == null) + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("IS ACCESS TOKEN NULL: " + (AccessToken.getCurrentAccessToken() == null) + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        if (isLoggedInThroughEmail) {
            String email = prefs.getString(getString(R.string.saved_data_email_id_preference_name), "na");
            String password = prefs.getString(getString(R.string.saved_data_password_preference_name), "na");
            String isPasswordEncrypted = "1";

            LoginServerRequest loginServerRequest = new LoginServerRequest(this, false);
            loginServerRequest.execute(email, password, isPasswordEncrypted);
        } else if (isLoggedInThroughFacebook && AccessToken.getCurrentAccessToken() != null) {
            System.out.println("IS PROFILE NULL: " + (Profile.getCurrentProfile() == null) + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("IS ACCESS TOKEN NULL: " + (AccessToken.getCurrentAccessToken() == null) + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            attemptContinueWithFacebook();
        } else {
            initializeActivity();
        }
    }

    public void initializeActivity() {
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login_signup);
        setUnit();

        IntroFragment introFragment = new IntroFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.login_signup_fragment_container, introFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
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

        FacebookLoginSignupServerRequest facebookLoginSignupServerRequest = new FacebookLoginSignupServerRequest(this, false);
        facebookLoginSignupServerRequest.execute(name, facebookAccountNumber, facebookProfileLinkURI, facebookProfilePicURI);
    }

    public void changeToAppActivity(UserAccount userAccount) {
        Intent intent = new Intent(this, AppActivity.class);
        intent.putExtra(getString(R.string.user_account_serialized_name), userAccount);
        startActivity(intent);
        finish();
    }

    private void setUnit() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0 ){
            fragmentManager.popBackStack();
            //fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (profileTracker != null && profileTracker.isTracking()) {
            profileTracker.stopTracking();
        }
    }
}
