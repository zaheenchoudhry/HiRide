package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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

public class CreateAccountServerRequest extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog pleaseWaitDialog;

    public CreateAccountServerRequest(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        String accountType = Integer.toString(UserAccount.ACCOUNT_TYPE_APP);
        String name = args[0];
        String phoneNumber = args[1];
        String email = args[2];
        String password = args[3];
        String signup_request_url = context.getString(R.string.facebook_account_request_url);

        try {
            String accountParameters = "&accountType=" + accountType;
            accountParameters += "&name=" + URLEncoder.encode(name, "UTF-8");
            accountParameters += "&phoneNumber=" + phoneNumber;
            accountParameters += "&email=" + URLEncoder.encode(email, "UTF-8");
            accountParameters += "&password=" + URLEncoder.encode(password, "UTF-8");

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
        pleaseWaitDialog.setTitle("Creating Account");
        pleaseWaitDialog.setMessage("Please wait...");
        pleaseWaitDialog.setCanceledOnTouchOutside(false);
        pleaseWaitDialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        pleaseWaitDialog.dismiss();

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if (result == null || result.equals("") || result.equals("failed")) {
            alertDialog.setTitle("Signup Failed");
            alertDialog.setMessage("Could not create account");
            alertDialog.show();
        } else if (result.equals("exists")) {
            alertDialog.setTitle("Email ID Exists");
            alertDialog.setMessage("The Email ID you entered already exists");
            alertDialog.show();
        } else {
            UserAccount userAccount = null;
            try {
                JSONObject jsonResponse = new JSONObject(result);
                JSONArray rideList = jsonResponse.getJSONArray("AppUserAccount");
                for (int i = 0; i < rideList.length(); ++i) {
                    try {
                        JSONObject jsonAccount = rideList.getJSONObject(i);
                        userAccount = new UserAccount(jsonAccount.getInt("UserId"), jsonAccount.getInt("AccountType"), jsonAccount.getString("Name"), jsonAccount.getString("PhoneNumber"));
                        userAccount.setEmailId(jsonAccount.getString("EmailId"));
                        userAccount.setAcceptedPaymentMethods(jsonAccount.getInt("AcceptsCash"), jsonAccount.getInt("AcceptsInAppPayments"));
                        userAccount.setPreferences(jsonAccount.getInt("PrefersMusic"), jsonAccount.getInt("PrefersDrinks"), jsonAccount.getInt("PrefersExtraLuggage"), jsonAccount.getInt("PrefersPets"), jsonAccount.getInt("PrefersGender"));

                        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.saved_data_preferences_name), Context.MODE_PRIVATE).edit();
                        editor.putBoolean(context.getString(R.string.saved_data_is_logged_in_email_preference_name), true);
                        editor.putString(context.getString(R.string.saved_data_name_preference_name), jsonAccount.getString("Name"));
                        editor.putString(context.getString(R.string.saved_data_email_id_preference_name), jsonAccount.getString("EmailId"));
                        editor.putString(context.getString(R.string.saved_data_password_preference_name), jsonAccount.getString("Password"));
                        editor.putBoolean(context.getString(R.string.saved_data_accepts_cash_name), jsonAccount.getInt("AcceptsCash") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_accepts_inapp_payments_name), jsonAccount.getInt("AcceptsInAppPayments") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_prefers_music_name), jsonAccount.getInt("PrefersMusic") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_prefers_drinks_food_name), jsonAccount.getInt("PrefersDrinks") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_prefers_extra_luggage_name), jsonAccount.getInt("PrefersExtraLuggage") != 0);
                        editor.putBoolean(context.getString(R.string.saved_data_prefers_pets_name), jsonAccount.getInt("PrefersPets") != 0);
                        editor.putBoolean("prefersGender", jsonAccount.getInt("PrefersGender") != 0);
                        editor.apply();

                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        alertDialog.setTitle("Start-up Failed");
                        alertDialog.setMessage("Could not start the application");
                        alertDialog.show();
                    }
                }
            } catch (Exception e) {
                alertDialog.setTitle("Start-up Failed");
                alertDialog.setMessage("Could not start the application");
                alertDialog.show();
            }
            ((LoginSignupActivity)context).changeToAppActivity(userAccount);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
