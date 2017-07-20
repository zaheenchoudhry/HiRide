package me.zaheenchoudhry.rideandgo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Button;
import android.widget.TextView;

import static android.R.attr.button;

public class MainActivity extends Activity {

    public static final double UNIT_NTH_ROOT = 0.1;
    private float screenX, screenY;
    private float unitX, unitY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        RelativeLayout layout = new RelativeLayout(this);
        setContentView(layout);
        layout.setId(R.id.WelcomeActivity);

        setUnit();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WelcomeFragment welcomeFragment = new WelcomeFragment();
        fragmentTransaction.add(layout.getId(), welcomeFragment);
        fragmentTransaction.commit();
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

    private void setUnit() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y;
        this.unitX = (float)Math.pow(size.x * 0.01, UNIT_NTH_ROOT);
        this.unitY = (float)Math.pow(size.y * 0.01, UNIT_NTH_ROOT);
    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    public float getUnitX() {
        return unitX;
    }

    public float getUnitY() {
        return unitY;
    }
}
