package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class CreateRideServerRequest extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog pleaseWaitDialog;
    private String ownerUserId, day, date, month, year, hour, minute, seats, price;
    private String pickupAddressFull, dropoffAddressFull, pickupAddressDisplay, dropoffAddressDisplay;
    private String pickupCity, dropoffCity, pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude;
    private String acceptsCash, acceptsInappPayments, prefersMusic, prefersDrinks, prefersLuggage, prefersPets;

    public CreateRideServerRequest(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        ownerUserId = args[0];
        day = args[1];
        date = args[2];
        month = args[3];
        year = args[4];
        hour = args[5];
        minute = args[6];
        seats = args[7];
        price = args[8];
        pickupAddressFull = args[9];
        dropoffAddressFull = args[10];
        pickupAddressDisplay = args[11];
        dropoffAddressDisplay = args[12];
        pickupCity = args[13];
        dropoffCity = args[14];
        pickupLatitude = args[15];
        pickupLongitude = args[16];
        dropoffLatitude = args[17];
        dropoffLongitude = args[18];
        acceptsCash = args[19];
        acceptsInappPayments = args[20];
        prefersMusic = args[21];
        prefersDrinks = args[22];
        prefersLuggage = args[23];
        prefersPets = args[24];
        String signup_request_url = context.getString(R.string.ride_request_url);;

        try {
            String rideParameters = "&ownerUserId=" + ownerUserId;
            rideParameters += "&day=" + day;
            rideParameters += "&date=" + date;
            rideParameters += "&month=" + month;
            rideParameters += "&year=" + year;
            rideParameters += "&hour=" + hour;
            rideParameters += "&minute=" + minute;
            rideParameters += "&seats=" + seats;
            rideParameters += "&price=" + URLEncoder.encode(price, "UTF-8");
            rideParameters += "&pickupAddressFull=" + URLEncoder.encode(pickupAddressFull, "UTF-8");
            rideParameters += "&dropoffAddressFull=" + URLEncoder.encode(dropoffAddressFull, "UTF-8");
            rideParameters += "&pickupAddressDisplay=" + URLEncoder.encode(pickupAddressDisplay, "UTF-8");
            rideParameters += "&dropoffAddressDisplay=" + URLEncoder.encode(dropoffAddressDisplay, "UTF-8");
            rideParameters += "&pickupCity=" + URLEncoder.encode(pickupCity, "UTF-8");
            rideParameters += "&dropoffCity=" + URLEncoder.encode(dropoffCity, "UTF-8");
            rideParameters += "&pickupLatitude=" + URLEncoder.encode(pickupLatitude, "UTF-8");
            rideParameters += "&pickupLongitude=" + URLEncoder.encode(pickupLongitude, "UTF-8");
            rideParameters += "&dropoffLatitude=" + URLEncoder.encode(dropoffLatitude, "UTF-8");
            rideParameters += "&dropoffLongitude=" + URLEncoder.encode(dropoffLongitude, "UTF-8");

            if (!acceptsCash.equals("-1")) {
                rideParameters += "&acceptsCash=" + URLEncoder.encode(acceptsCash, "UTF-8");
            }

            if (!acceptsInappPayments.equals("-1")) {
                rideParameters += "&acceptsInappPayments=" + URLEncoder.encode(acceptsInappPayments, "UTF-8");
            }

            if (!prefersMusic.equals("-1")) {
                rideParameters += "&prefersMusic=" + URLEncoder.encode(prefersMusic, "UTF-8");
            }

            if (!prefersDrinks.equals("-1")) {
                rideParameters += "&prefersDrinks=" + URLEncoder.encode(prefersDrinks, "UTF-8");
            }

            if (!prefersLuggage.equals("-1")) {
                rideParameters += "&prefersExtraLuggage=" + URLEncoder.encode(prefersLuggage, "UTF-8");
            }

            if (!prefersPets.equals("-1")) {
                rideParameters += "&prefersPets=" + URLEncoder.encode(prefersPets, "UTF-8");
            }

            System.out.println("pickupLatitude string : " + pickupLatitude);
            System.out.println("pickupLongitude string : " + pickupLongitude);

            URL url = new URL(signup_request_url);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(rideParameters);
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

        String[] splitResult = null;
        if (result != null && !result.equals("")) {
            splitResult = result.split("\\s+");
        }

        if (result == null || result.equals("") || result.equals("failed")) {
            alertDialog.setTitle("Something went wrong");
            alertDialog.setMessage("Could not create ride");
            alertDialog.show();
        } else if (splitResult != null && splitResult[0].equals("success")) {
            RidePost ridePost = new RidePost();
            ridePost.setRideAndOwnerId(Integer.parseInt(splitResult[1]), Integer.parseInt(ownerUserId));
            ridePost.setDate(Integer.parseInt(day), Integer.parseInt(date), Integer.parseInt(month), Integer.parseInt(year));
            ridePost.setTime(Integer.parseInt(hour), Integer.parseInt(minute));
            ridePost.setSeats(Integer.parseInt(seats));
            ridePost.setPrice(Double.parseDouble(price));
            ridePost.setPickupAddress(pickupAddressFull, pickupAddressDisplay, pickupCity);
            ridePost.setDropoffAddress(dropoffAddressFull, dropoffAddressDisplay, dropoffCity);
            ridePost.setPickupAddressCoordinates(Double.parseDouble(pickupLatitude), Double.parseDouble(pickupLongitude));
            ridePost.setDropoffAddressCoordinates(Double.parseDouble(dropoffLatitude), Double.parseDouble(dropoffLongitude));

            SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.saved_data_preferences_name), Context.MODE_PRIVATE).edit();

            UserAccount userAccount = ((AppActivity)context).getUserAccount();
            if (!acceptsCash.equals("-1")) {
                userAccount.setAcceptsCash(Integer.parseInt(acceptsCash));
                editor.putBoolean(context.getString(R.string.saved_data_accepts_cash_name), Integer.parseInt(acceptsCash) != 0);
            }
            if (!acceptsInappPayments.equals("-1")) {
                userAccount.setAcceptsInAppPayments(Integer.parseInt(acceptsInappPayments));
                editor.putBoolean(context.getString(R.string.saved_data_accepts_inapp_payments_name), Integer.parseInt(acceptsInappPayments) != 0);
            }
            if (!prefersMusic.equals("-1")) {
                userAccount.setPrefersMusic(Integer.parseInt(prefersMusic));
                editor.putBoolean(context.getString(R.string.saved_data_prefers_music_name), Integer.parseInt(prefersMusic) != 0);
            }
            if (!prefersDrinks.equals("-1")) {
                userAccount.setPrefersDrinks(Integer.parseInt(prefersDrinks));
                editor.putBoolean(context.getString(R.string.saved_data_prefers_drinks_food_name), Integer.parseInt(prefersDrinks) != 0);
            }
            if (!prefersLuggage.equals("-1")) {
                userAccount.setPrefersExtraLuggage(Integer.parseInt(prefersLuggage));
                editor.putBoolean(context.getString(R.string.saved_data_prefers_extra_luggage_name), Integer.parseInt(prefersLuggage) != 0);
            }
            if (!prefersPets.equals("-1")) {
                userAccount.setPrefersPets(Integer.parseInt(prefersPets));
                editor.putBoolean(context.getString(R.string.saved_data_prefers_pets_name), Integer.parseInt(prefersPets) != 0);
            }
            editor.apply();
            ((AppActivity)context).setUserAccount(userAccount);

            FragmentTransaction transaction = ((AppActivity)context).getSupportFragmentManager().beginTransaction();
            RideDetailFragment rideDetailFragment = new RideDetailFragment(RideDetailFragment.ACCESSOR_DRIVER, ridePost);
            transaction.replace(R.id.fragment_container, rideDetailFragment);
            transaction.commit();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
