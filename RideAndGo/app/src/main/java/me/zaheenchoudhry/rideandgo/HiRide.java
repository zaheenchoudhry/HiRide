package me.zaheenchoudhry.rideandgo;

import android.app.Application;
import android.content.res.Configuration;

import com.hypertrack.lib.HyperTrack;
import com.onesignal.OneSignal;

/**
 * Created by lorsk on 2018-03-16.
 */

public class HiRide extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        HyperTrack.initialize(this, "pk_test_5799c7ab4f653bfbc475d180e4128306e16b9301");

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
