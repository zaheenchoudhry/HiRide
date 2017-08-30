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
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SignUpFragmentOld extends Fragment {

    private final int NAME_INPUT = 0;
    private final int EMAIL_INPIT = 1;
    private final int PASSWORD_INPUT = 2;
    private final int PASSWORD_CONFIRM_INPUT = 3;
    private RelativeLayout layout;
    private RelativeLayout.LayoutParams relativeLayoutParams;
    private RelativeLayout.LayoutParams inputParams, signUpButtonParams, titleTextParans, termsTextParams, termsParams;
    private float screenX, screenY, unitX, unitY;
    private Button signUpButton;
    private EditText[] inputs;
    private TextView titleText, termsText, termsMessageText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = new RelativeLayout(getActivity());
        //layout.setBackgroundResource(R.drawable.white);
        //layout.setBackgroundColor(Color.parseColor("#e74c3c"));
        setUnit();

        relativeLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        layout.setLayoutParams(relativeLayoutParams);

        initializeTitleText();
        initializeInputs();
        initializeSignUpButton();
        initializeTermsText();

        for (int i = 0; i < inputs.length; ++i) {
            layout.addView(inputs[i]);
        }
        layout.addView(titleText);
        layout.addView(signUpButton);
        layout.addView(termsMessageText);
        layout.addView(termsText);
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

    private void initializeTitleText() {
        titleTextParans = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleText = new TextView(getActivity());
        titleText.setLayoutParams(titleTextParans);
        titleTextParans.addRule(RelativeLayout.CENTER_HORIZONTAL);
        titleText.setText("Sign Up");
        titleText.setTextColor(Color.parseColor("#303030"));
        titleText.setTextSize(screenX * 0.018f);
        titleText.setY(screenY * 0.18f);
    }

    private Drawable getIconResource(int i) {
        switch(i) {
            case 0:
                inputs[NAME_INPUT].setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                inputs[NAME_INPUT].setHint("Name");
                return getResources().getDrawable(R.drawable.login_icon, null);
            case 1:
                inputs[EMAIL_INPIT].setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                inputs[EMAIL_INPIT].setHint("Email Address");
                return getResources().getDrawable(R.drawable.email_icon, null);
            case 2:
                inputs[PASSWORD_INPUT].setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                inputs[PASSWORD_INPUT].setTransformationMethod(PasswordTransformationMethod.getInstance());
                inputs[PASSWORD_INPUT].setHint("Password");
                return getResources().getDrawable(R.drawable.password_icon, null);
            case 3:
                inputs[PASSWORD_CONFIRM_INPUT].setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                inputs[PASSWORD_CONFIRM_INPUT].setTransformationMethod(PasswordTransformationMethod.getInstance());
                inputs[PASSWORD_CONFIRM_INPUT].setHint("Confirm Password");
                return getResources().getDrawable(R.drawable.password_icon, null);
        }
        return getResources().getDrawable(R.drawable.login_icon, null);
    }

    private void initializeInputs() {
        inputs = new EditText[4];

        inputParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        inputParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        for (int i = 0; i < inputs.length; ++i) {
            inputs[i] = new EditText(getActivity());
            inputs[i].setLayoutParams(inputParams);
            inputs[i].setWidth((int)(screenX * 0.8f));

            Drawable drawable = getIconResource(i);
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            float oldWidth = ((BitmapDrawable) drawable).getBitmap().getWidth();
            float oldHeight = ((BitmapDrawable) drawable).getBitmap().getHeight();
            int newWidth = (int)(screenX * 0.06f);
            int newHeight = (int)((oldHeight / oldWidth) * newWidth);
            Drawable drawable2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true));
            inputs[i].setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null);
            inputs[i].setCompoundDrawablePadding((int)(screenX * 0.02f));
            inputs[i].setPadding((int)(screenX * 0.03f), (int)(screenX * 0.035f), (int)(screenX * 0.0325f), (int)(screenX * 0.035f));
            inputs[i].setBackground(null);
            inputs[i].setY(screenY * 0.3f + screenX * 0.17f * i);

            ShapeDrawable border = new ShapeDrawable(new RectShape());
            border.getPaint().setColor(Color.parseColor("#c0c0c0"));
            //c0c0c0
            border.getPaint().setStrokeWidth(screenX * 0.006f);
            border.getPaint().setStyle(Paint.Style.STROKE);
            inputs[i].setBackground(border);
            inputs[i].setHintTextColor(Color.parseColor("#c0c0c0"));
        }
    }

    private void initializeTermsText() {
        termsTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        termsTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        termsTextParams.addRule(RelativeLayout.CENTER_VERTICAL);
        termsParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        termsParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        termsParams.addRule(RelativeLayout.CENTER_VERTICAL);

        termsText = new TextView(getActivity());
        termsText.setLayoutParams(termsParams);
        termsMessageText = new TextView(getActivity());
        termsMessageText.setLayoutParams(termsTextParams);
        termsMessageText.setText("By signing up, you agree to our");
        termsMessageText.setTextColor(Color.parseColor("#303030"));
        termsMessageText.setTextSize(screenX * 0.008f);
        termsMessageText.setY(screenY * 0.42f);
        termsText.setText("Terms of Use");
        termsText.setTextColor(Color.parseColor("#22409A"));
        termsText.setTextSize(screenX * 0.008f);
        termsText.setY(screenY * 0.45f);
    }

    private void initializeSignUpButton() {
        signUpButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        signUpButton = new Button(getActivity());
        //signUpButton.setId(R.id.welcomeFragmentLoginButton);
        signUpButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        signUpButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        signUpButton.setLayoutParams(signUpButtonParams);
        signUpButtonParams.height = (int)(screenY * 0.08f);
        signUpButtonParams.width = (int)(screenX * 0.6f);
        signUpButton.setWidth((int)(screenX * 0.08f));
        signUpButton.setHeight((int)(screenY * 0.6f));
        signUpButton.setY(screenY * 0.32f);
        signUpButton.setText("Sign Up");
        signUpButton.setTextSize(screenX * 0.008f);
        signUpButton.setTextColor(Color.parseColor("#ffffff"));
        //303030

        /*ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setColor(Color.parseColor("#303030"));
        border.getPaint().setStrokeWidth(10f);
        border.getPaint().setStyle(Paint.Style.STROKE);
        loginButton.setBackground(border);*/

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#22409A"));
        //gd.setStroke(8, Color.parseColor("#22409A"));
        signUpButton.setBackground(gd);

        signUpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        signUpButton.setTextColor(Color.WHITE);
                        signUpButton.setBackgroundColor(Color.parseColor("#404040"));
                        return true;
                    case MotionEvent.ACTION_UP:
                        SignUpFragment signUpFragment = new SignUpFragment();
                        if (signUpFragment != null) {
                            /*FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.WelcomeActivity, signUpFragment);
                            //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();*/
                            String name = inputs[0].getText().toString();
                            String email = inputs[1].getText().toString();
                            String password = inputs[2].getText().toString();

                            SignUpServerRequest signUpServerRequest = new SignUpServerRequest(getActivity());
                            signUpServerRequest.execute(name, email, password);
                        }
                        return true;
                }
                return false;
            }
        });
    }
}
