package me.zaheenchoudhry.rideandgo;

import android.content.Context;
import android.os.AsyncTask;

import static android.R.attr.id;

public class ServerRequest extends AsyncTask<String, Void, Void> {

    private Context context;

    public ServerRequest(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String login_url = "http://zaheenchoudhry.me/";
        return null;
    }
}
