package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class FacebookLoginSignupServerRequest extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog pleaseWaitDialog;
    private boolean shouldShowDialogs;

    public FacebookLoginSignupServerRequest(Context context, boolean shouldShowDialogs) {
        super();
        this.context = context;
        this.shouldShowDialogs = shouldShowDialogs;
    }

    @Override
    protected String doInBackground(String... args) {
        String accountType = Integer.toString(UserAccount.ACCOUNT_TYPE_FACEBOOK_ACCOUNT);
        String name = args[0];
        String facebookAccountNumber = args[1];
        String facebookProfileLinkURI = args[2];
        String facebookProfilePicURI = args[3];
        String signup_request_url = context.getString(R.string.facebook_account_request_url);

        try {
            String accountParameters = "&accountType=" + accountType;
            accountParameters += "&name=" + URLEncoder.encode(name, "UTF-8");
            accountParameters += "&facebookAccountNumber=" + facebookAccountNumber;
            accountParameters += "&facebookProfileLinkURI=" + URLEncoder.encode(facebookProfileLinkURI, "UTF-8");
            accountParameters += "&facebookProfilePicURI=" + URLEncoder.encode(facebookProfilePicURI, "UTF-8");

            URL url = new URL(signup_request_url);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(accountParameters);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String result = "";
            String line = bufferedReader.readLine();

            while (line != null) {
                result += line;
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            inputStreamReader.close();

            return result;
        } catch (IOException e) {
            // MalformedURLException | UnsupportedEncodingException are subclasses of IOException
            // todo: display a popup saying sign in failed
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        pleaseWaitDialog = new ProgressDialog(context);
        if (shouldShowDialogs) {
            pleaseWaitDialog.setTitle("Checking Credentials");
            pleaseWaitDialog.setMessage("Please wait...");
            pleaseWaitDialog.setCanceledOnTouchOutside(false);
            pleaseWaitDialog.show();
        }
        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Create Ride Response");
    }

    @Override
    protected void onPostExecute(String result) {
        if (pleaseWaitDialog.isShowing()) {
            pleaseWaitDialog.dismiss();
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if (result == null || result.equals("")) {
            // for offline mode
            SharedPreferences prefs =  context.getSharedPreferences(context.getString(R.string.saved_data_preferences_name), Context.MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean(context.getString(R.string.saved_data_is_logged_in_facebook_preference_name), false);
            if (shouldShowDialogs) {
                if (isLoggedIn) {
                    ((LoginSignupActivity)context).changeToAppActivity(getNewOfflineUserAccount());
                } else {
                    alertDialog.setTitle("Something Went Wrong");
                    alertDialog.setMessage("Could not Log In");
                    alertDialog.show();
                }
            } else {
                // for offline mode, show app with only upcoming rides info
                if (isLoggedIn) {
                    ((LoginSignupActivity) context).changeToAppActivity(getNewOfflineUserAccount());
                } else {
                    ((LoginSignupActivity)context).initializeActivity();
                }
            }
        } else if (result.equals("failed")) {
            if (shouldShowDialogs) {
                alertDialog.setTitle("Something Went Wrong");
                alertDialog.setMessage("Could not Log In");
                alertDialog.show();
            } else {
                ((LoginSignupActivity)context).initializeActivity();
            }
        } else {
            UserAccount userAccount = null;
            try {
                JSONObject jsonResponse = new JSONObject(result);
                JSONArray rideList = jsonResponse.getJSONArray("FacebookUserAccount");
                for (int i = 0; i < rideList.length(); ++i) {
                    try {
                        JSONObject jsonAccount = rideList.getJSONObject(i);
                        userAccount = new UserAccount(jsonAccount.getInt("UserId"), jsonAccount.getInt("AccountType"), jsonAccount.getString("Name"), jsonAccount.getInt("PhoneNumber"));
                        userAccount.setFacebookUserSpecificDetails(jsonAccount.getInt("FacebookAccountNumber"), jsonAccount.getString("FacebookProfileLinkURI"), jsonAccount.getString("FacebookProfilePicURI"));
                        userAccount.setAcceptedPaymentMethods(jsonAccount.getInt("AcceptsCash"), jsonAccount.getInt("AcceptsInAppPayments"));
                        userAccount.setPreferences(jsonAccount.getInt("PrefersMusic"), jsonAccount.getInt("PrefersDrinks"), jsonAccount.getInt("PrefersExtraLuggage"), jsonAccount.getInt("PrefersPets"));

                        //if (shouldShowDialogs) {
                        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.saved_data_preferences_name), Context.MODE_PRIVATE).edit();
                        editor.putBoolean(context.getString(R.string.saved_data_is_logged_in_facebook_preference_name), true);
                        editor.putString(context.getString(R.string.saved_data_name_preference_name), jsonAccount.getString("Name"));
                        editor.putString(context.getString(R.string.saved_data_password_preference_name), jsonAccount.getString("Password"));
                        editor.putBoolean(context.getString(R.string.saved_data_accepts_cash_name), jsonAccount.getInt("AcceptsCash") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_accepts_inapp_payments_name), jsonAccount.getInt("AcceptsInAppPayments") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_prefers_music_name), jsonAccount.getInt("PrefersMusic") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_prefers_drinks_food_name), jsonAccount.getInt("PrefersDrinks") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_prefers_extra_luggage_name), jsonAccount.getInt("PrefersExtraLuggage") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_prefers_pets_name), jsonAccount.getInt("PrefersPets") != 0);
                        editor.apply();
                        //}

                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        Log.e("HiRide", "exception", e);
                        if (shouldShowDialogs) {
                            alertDialog.setTitle("Start-up Failed");
                            alertDialog.setMessage("Could not start the application");
                            alertDialog.show();
                        } else {
                            ((LoginSignupActivity)context).initializeActivity();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("HiRide", "exception", e);
                if (shouldShowDialogs) {
                    alertDialog.setTitle("Start-up Failed");
                    alertDialog.setMessage("Could not start the application");
                    alertDialog.show();
                } else {
                    ((LoginSignupActivity)context).initializeActivity();
                }
            }
            ((LoginSignupActivity)context).changeToAppActivity(userAccount);
        }
    }

    private UserAccount getNewOfflineUserAccount() {
        SharedPreferences prefs =  context.getSharedPreferences(context.getString(R.string.saved_data_preferences_name), Context.MODE_PRIVATE);
        String name = prefs.getString(context.getString(R.string.saved_data_name_preference_name), "Guest");
        UserAccount userAccount = new UserAccount(name);
        int acceptsCash = (prefs.getBoolean(context.getString(R.string.saved_data_accepts_cash_name), true)) ? 1 : 0;
        int acceptsInappPayments = (prefs.getBoolean(context.getString(R.string.saved_data_accepts_cash_name), true)) ? 1 : 0;
        int prefersMusic = (prefs.getBoolean(context.getString(R.string.saved_data_accepts_cash_name), true)) ? 1 : 0;
        int prefersDrinks = (prefs.getBoolean(context.getString(R.string.saved_data_accepts_cash_name), false)) ? 1 : 0;
        int prefersLuggage = (prefs.getBoolean(context.getString(R.string.saved_data_accepts_cash_name), false)) ? 1 : 0;
        int prefersPets = (prefs.getBoolean(context.getString(R.string.saved_data_accepts_cash_name), false)) ? 1 : 0;
        userAccount.setAcceptedPaymentMethods(acceptsCash, acceptsInappPayments);
        userAccount.setPreferences(prefersMusic, prefersDrinks, prefersLuggage, prefersPets);
        return userAccount;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}