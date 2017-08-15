package me.zaheenchoudhry.rideandgo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

public class LoginFragmentOld extends Fragment {

    private RelativeLayout layout;
    private RelativeLayout.LayoutParams relativeLayoutParams;
    private RelativeLayout.LayoutParams emailIdInputParams, passwordInputParams, loginButtonParams;
    private Button loginButton;
    private EditText emailIdInput, passwordInput;
    private float screenX, screenY, unitX, unitY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //View view =  inflater.inflate(R.layout.welcomeFragment, container, false);
        layout = new RelativeLayout(getActivity());
        //layout.setBackgroundResource(R.drawable.white);
        //layout.setBackgroundColor(Color.parseColor("#e74c3c"));
        setUnit();

        relativeLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        layout.setLayoutParams(relativeLayoutParams);



        /*RelativeLayout.LayoutParams blackBackgroundParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        ImageView blackBackground = new ImageView(getActivity());
        blackBackgroundParams.width = (int)(screenX);
        blackBackgroundParams.height = (int)screenY;
        blackBackgroundParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        blackBackgroundParams.addRule(RelativeLayout.CENTER_VERTICAL);
        blackBackground.setMinimumWidth((int)(screenX));
        blackBackground.setMinimumHeight((int)(screenY));
        blackBackground.setImageResource(R.drawable.black);
        blackBackground.setLayoutParams(blackBackgroundParams);
        blackBackground.setScaleType(ImageView.ScaleType.FIT_XY);
        blackBackground.setAlpha(0.05f);*/
        //blackBackground.setX(-100f);


        /*RelativeLayout.LayoutParams blueButtonContainerParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        ImageView blueButtonContainer = new ImageView(getActivity());
        blueButtonContainer.setImageResource(R.drawable.blue);
        blueButtonContainerParams.width = (int)(screenX);
        blueButtonContainerParams.height = (int)(screenY * 0.2f);
        blueButtonContainerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        blueButtonContainerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        blueButtonContainer.setMinimumWidth((int)(screenX));
        blueButtonContainer.setMinimumHeight((int)(screenY * 0.2f));
        blueButtonContainer.setLayoutParams(blueButtonContainerParams);
        blueButtonContainer.setScaleType(ImageView.ScaleType.FIT_XY);
        blueButtonContainer.setY(-blueButtonContainerParams.height / 2f + screenY/2f);*/



        /*Toolbar toolbar = new Toolbar(getActivity());
        RelativeLayout.LayoutParams toolbarParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        toolbarParams.height = (int)(screenY * 0.14f);
        toolbarParams.width = (int)(screenX);
        toolbar.setLayoutParams(toolbarParams);
        toolbar.setBackgroundColor(Color.parseColor("#303030"));*/
        //toolbar.setVisibility(View.VISIBLE);



        /*RelativeLayout.LayoutParams backTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView backText = new TextView(getActivity());
        backText.setLayoutParams(backTextParams);
        //text.setId(1);
        backText.setText("<  Back");
        backText.setTextColor(Color.parseColor("#FFFFFF"));
        backText.setTextSize(screenX * 0.014f);
        backText.setY(screenY * 0.065f);
        backText.setX(screenX * 0.06f);*/



        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView text = new TextView(getActivity());
        text.setLayoutParams(textParams);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //text.setId(1);
        text.setText("Log In");
        text.setTextColor(Color.parseColor("#303030"));
        //303030
        text.setTextSize(screenX * 0.018f);
        text.setY(screenY * 0.18f);
        //text.setLetterSpacing(0.1f);
        //textParams.setMargins(500, 0, 0, 500);


        RelativeLayout.LayoutParams forgotPasswordTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView forgotPasswordText = new TextView(getActivity());
        forgotPasswordText.setLayoutParams(forgotPasswordTextParams);
        forgotPasswordTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        forgotPasswordTextParams.addRule(RelativeLayout.CENTER_VERTICAL);
        //text.setId(1);
        forgotPasswordText.setText("Forgot Password?");
        forgotPasswordText.setTextColor(Color.parseColor("#22409A"));
        forgotPasswordText.setTextSize(screenX * 0.01f);
        forgotPasswordText.setY(screenY * 0.1f);




        initializeEmailIdInput();
        initializePasswordInput();
        initializeLoginButton();

        //layout.addView(toolbar);
        //layout.addView(backText);
        //layout.addView(blackBackground);
        //layout.addView(blueButtonContainer);
        layout.addView(text);
        layout.addView(emailIdInput);
        layout.addView(passwordInput);
        layout.addView(forgotPasswordText);
        layout.addView(loginButton);

        return layout;
    }

    private void setUnit() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
        this.unitX = (float)Math.pow(size.x * 0.01, MainActivity.UNIT_NTH_ROOT);
        this.unitY = (float)Math.pow(size.y * 0.01, MainActivity.UNIT_NTH_ROOT);
    }

    private void initializeEmailIdInput() {
        emailIdInputParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        emailIdInput = new EditText(getActivity());
        emailIdInputParams.addRule(RelativeLayout.CENTER_VERTICAL);
        emailIdInputParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        emailIdInput.setLayoutParams(emailIdInputParams);
        emailIdInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailIdInput.setWidth((int)(screenX * 0.8f));

        Drawable drawable = getResources().getDrawable(R.drawable.email_icon, null);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        float oldWidth = ((BitmapDrawable) drawable).getBitmap().getWidth();
        float oldHeight = ((BitmapDrawable) drawable).getBitmap().getHeight();
        int newWidth = (int)(screenX * 0.06f);
        int newHeight = (int)((oldHeight / oldWidth) * newWidth);
        Drawable drawable2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true));
        emailIdInput.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null);
        emailIdInput.setCompoundDrawablePadding((int)(screenX * 0.02f));
        emailIdInput.setPadding((int)(screenX * 0.03f), (int)(screenX * 0.035f), (int)(screenX * 0.0325f), (int)(screenX * 0.035f));
        emailIdInput.setBackground(null);
        emailIdInput.setY(emailIdInput.getHeight() / 2f - screenY * 0.13f);

        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setColor(Color.parseColor("#c0c0c0"));
        border.getPaint().setStrokeWidth(screenX * 0.006f);
        border.getPaint().setStyle(Paint.Style.STROKE);
        emailIdInput.setBackground(border);
        emailIdInput.setHint("Email address");
        emailIdInput.setHintTextColor(Color.parseColor("#c0c0c0"));
    }

    private void initializePasswordInput() {
        passwordInputParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        passwordInput = new EditText(getActivity());
        passwordInputParams.addRule(RelativeLayout.CENTER_VERTICAL);
        passwordInputParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        passwordInput.setLayoutParams(passwordInputParams);
        passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        passwordInput.setWidth((int)(screenX * 0.8f));

        Drawable drawable = getResources().getDrawable(R.drawable.password_icon, null);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        float oldWidth = ((BitmapDrawable) drawable).getBitmap().getWidth();
        float oldHeight = ((BitmapDrawable) drawable).getBitmap().getHeight();
        int newWidth = (int)(screenX * 0.06f);
        int newHeight = (int)((oldHeight / oldWidth) * newWidth);
        Drawable drawable2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true));
        passwordInput.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null);
        passwordInput.setCompoundDrawablePadding((int)(screenX * 0.02f));
        passwordInput.setPadding((int)(screenX * 0.03f), (int)(screenX * 0.035f), (int)(screenX * 0.0325f), (int)(screenX * 0.035f));
        passwordInput.setBackground(null);
        passwordInput.setY(passwordInput.getHeight() / 2f - screenY * 0.02f);

        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setColor(Color.parseColor("#c0c0c0"));
        border.getPaint().setStrokeWidth(screenX * 0.006f);
        border.getPaint().setStyle(Paint.Style.STROKE);
        passwordInput.setBackground(border);
        passwordInput.setHint("Password");
        passwordInput.setHintTextColor(Color.parseColor("#c0c0c0"));
    }

    private void initializeLoginButton() {
        loginButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        loginButton = new Button(getActivity());
        loginButton.setId(R.id.welcomeFragmentLoginButton);
        loginButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        loginButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        loginButton.setLayoutParams(loginButtonParams);
        loginButtonParams.height = (int)(screenY * 0.08f);
        loginButtonParams.width = (int)(screenX * 0.6f);
        loginButton.setWidth((int)(screenX * 0.08f));
        loginButton.setHeight((int)(screenY * 0.6f));
        loginButton.setY(screenY * 0.32f);
        loginButton.setText("Log In");
        loginButton.setTextSize(screenX * 0.008f);
        loginButton.setTextColor(Color.parseColor("#ffffff"));
        //303030

        /*ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setColor(Color.parseColor("#303030"));
        border.getPaint().setStrokeWidth(10f);
        border.getPaint().setStyle(Paint.Style.STROKE);
        loginButton.setBackground(border);*/

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#22409A"));
        //gd.setStroke((int)(screenX * 0.006f), Color.parseColor("#22409A"));
        loginButton.setBackground(gd);


        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        loginButton.setTextColor(Color.WHITE);
                        loginButton.setBackgroundColor(Color.parseColor("#404040"));
                        return true;
                    case MotionEvent.ACTION_UP:
                        LoginFragment loginFragment = new LoginFragment();
                        if (loginFragment != null) {
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.WelcomeActivity, loginFragment);
                            //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                }
                return false;
            }
        });
    }
}