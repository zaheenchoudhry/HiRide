package me.zaheenchoudhry.rideandgo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.R.attr.button;

public class WelcomeFragment extends Fragment {

    private RelativeLayout layout;
    private RelativeLayout.LayoutParams relativeLayoutParams;
    private RelativeLayout.LayoutParams loginButtonParams, signUpButtonParams;
    private Button loginButton, signUpButton;
    private float screenX, screenY, unitX, unitY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        //View view =  inflater.inflate(R.layout.welcomeFragment, container, false);
        layout = new RelativeLayout(getActivity());
        layout.setBackgroundResource(R.drawable.city4);
        setUnit();

        relativeLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        layout.setLayoutParams(relativeLayoutParams);


        /*RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);*/

        /*ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        circle.getPaint().setColor(Color.WHITE);
        circle.setIntrinsicWidth((int)(screenX * 5));
        circle.setIntrinsicHeight((int)(screenY));*/

        /*Bitmap bitmap = Bitmap.createBitmap((int) screenX, (int) screenY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        float x = 50;
        float y = 50;
        float radius = 20;
        canvas.drawCircle(x, y, radius, paint);*/

        /*GradientDrawable buttonContainer = new GradientDrawable();
        buttonContainer.setCornerRadius(20);
        buttonContainer.setColor(Color.WHITE);
        layout.setBackground(buttonContainer);/*


        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        ImageView image = new ImageView(getActivity());
        image.setImageResource(R.drawable.amc);
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageParams.height = (int)(unitX * 400);
        imageParams.width = (int)(unitX * 400);
        image.setLayoutParams(imageParams);
        image.setY(-(screenY * 0.055f));


        /*TextView text = new TextView(getActivity());
        //text.setId(1);
        text.setText("Ride");
        text.setTextColor(Color.parseColor("#ffffff"));
        text.setTextSize(screenX * 0.05f);
        //text.setLetterSpacing(0.1f);
        textParams.addRule(RelativeLayout.CENTER_VERTICAL);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        text.setY(-200f);
        //textParams.setMargins(500, 0, 0, 500);
        text.setLayoutParams(textParams);*/

        //Display display = ((android.view.WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        RelativeLayout.LayoutParams blackBackgroundParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        ImageView blackBackground = new ImageView(getActivity());
        blackBackground.setImageResource(R.drawable.black);
        blackBackgroundParams.width = (int)(screenX);
        blackBackgroundParams.height = (int)screenY;
        blackBackgroundParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        blackBackgroundParams.addRule(RelativeLayout.CENTER_VERTICAL);
        blackBackground.setMinimumWidth((int)(screenX));
        blackBackground.setMinimumHeight((int)(screenY));
        blackBackground.setLayoutParams(blackBackgroundParams);
        blackBackground.setScaleType(ImageView.ScaleType.FIT_XY);
        blackBackground.setAlpha(0.2f);

        RelativeLayout.LayoutParams whiteButtonContainerParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        ImageView whiteButtonContainer = new ImageView(getActivity());
        whiteButtonContainer.setImageResource(R.drawable.shape2);
        whiteButtonContainerParams.width = (int)(screenX);
        whiteButtonContainerParams.height = (int)(screenY * 0.32f);
        whiteButtonContainerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        whiteButtonContainerParams.addRule(RelativeLayout.CENTER_VERTICAL);
        whiteButtonContainer.setMinimumWidth((int)(screenX));
        whiteButtonContainer.setMinimumHeight((int)(screenY * 0.32f));
        whiteButtonContainer.setLayoutParams(whiteButtonContainerParams);
        whiteButtonContainer.setScaleType(ImageView.ScaleType.FIT_XY);
        whiteButtonContainer.setY(-whiteButtonContainerParams.height / 2f + screenY/2f);
        //whiteBottonContainer.setAlpha(0.96f);


        RelativeLayout.LayoutParams companyNameTextParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView companyNameText = new TextView(getActivity());
        companyNameText.setLayoutParams(companyNameTextParams);
        companyNameTextParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //text.setId(1);
        companyNameText.setText("RIDE");
        companyNameText.setTextColor(Color.parseColor("#FFFFFF"));
        //303030
        companyNameText.setTextSize(screenX * 0.025f);
        companyNameText.setY(screenY * 0.18f);



        initializeLoginButton();
        initializeSignUpButton();


        //layout.addView(image);
        layout.addView(blackBackground);
        layout.addView(whiteButtonContainer);
        //layout.addView(companyNameText);
        layout.addView(loginButton);
        layout.addView(signUpButton);

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

    private void initializeLoginButton() {
        loginButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        loginButton = new Button(getActivity());
        loginButton.setId(R.id.welcomeFragmentLoginButton);
        loginButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        loginButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        loginButton.setLayoutParams(loginButtonParams);
        //loginButtonParams.height = (int)(unitX * 150);
        //loginButtonParams.width = (int)(unitX * 380);
        //loginButton.setWidth((int)(unitX * 380));
        //loginButton.setHeight((int)(unitX * 150));
        loginButtonParams.width = (int)(screenX * 0.6f);
        loginButtonParams.height = (int)(screenY * 0.085f);
        loginButton.setWidth((int)(screenX * 0.6f));
        loginButton.setHeight((int)(screenY * 0.085f));
        loginButton.setY(screenY * 0.3f);
        //loginButton.setX(-loginButtonParams.width / 2f - (screenX * 0.015f));
        loginButton.setText("Log In");
        loginButton.setTextSize(screenX * 0.008f);
        loginButton.setTextColor(Color.WHITE);
        loginButton.setBackgroundColor(Color.parseColor("#22409A"));

        /*ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setColor(Color.parseColor("#f6364d"));
        border.getPaint().setStrokeWidth(10f);
        border.getPaint().setStyle(Paint.Style.STROKE);
        loginButton.setBackground(border);*/

        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        loginButton.setBackgroundColor(Color.parseColor("#294ebc"));
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

    private void initializeSignUpButton() {
        signUpButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        /*GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#f6364d"));
        gd.setStroke(12, Color.parseColor("#f6364d"));
        //button.setBackground(gd);*/

        signUpButton = new Button(getActivity());
        signUpButton.setId(R.id.welcomeFragmentSignUpButton);
        signUpButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        signUpButtonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        signUpButton.setLayoutParams(signUpButtonParams);
        //signUpButtonParams.height = (int)(unitX * 150);
        //signUpButtonParams.width = (int)(unitX * 380);
        //signUpButton.setWidth((int)(unitX * 380));
        //signUpButton.setHeight((int)(unitX * 150));
        //signUpButton.setY(screenY * 0.38f);
        signUpButtonParams.width = (int)(screenX * 0.6f);
        signUpButtonParams.height = (int)(screenY * 0.085f);
        signUpButton.setWidth((int)(screenX * 0.6f));
        signUpButton.setHeight((int)(screenY * 0.085f));
        signUpButton.setY(screenY * 0.385f);
        //signUpButton.setX(signUpButtonParams.width / 2f + (screenX * 0.015f));
        signUpButton.setText("Sign Up");
        signUpButton.setTextSize(screenX * 0.008f);
        signUpButton.setTextColor(Color.parseColor("#22409A"));
        signUpButton.setBackgroundColor(Color.WHITE);
        //22409A f6364d
        //signUpButton.setBackground(gd);

        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setColor(Color.parseColor("#22409A"));
        border.getPaint().setStrokeWidth(10f);
        border.getPaint().setStyle(Paint.Style.STROKE);
        //signUpButton.setBackground(border);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#f0f0f0"));
        gd.setStroke((int)(screenX * 0.006f), Color.parseColor("#22409A"));
        signUpButton.setBackground(gd);


        signUpButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        signUpButton.setTextColor(Color.WHITE);
                        signUpButton.setBackgroundColor(Color.parseColor("#294ebc"));
                        return true;
                    case MotionEvent.ACTION_UP:
                        SignUpFragment signUpFragment = new SignUpFragment();
                        if (signUpFragment != null) {
                            FragmentManager fragmentManager = getActivity().getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.WelcomeActivity, signUpFragment);
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
