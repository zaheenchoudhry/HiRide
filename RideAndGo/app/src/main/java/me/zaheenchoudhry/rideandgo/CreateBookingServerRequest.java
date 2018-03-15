package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateBookingServerRequest extends AsyncTask<String, Void, String> {

    private Context context;
    private ProgressDialog pleaseWaitDialog;
    private String ownerUserId, BookingId, RideId, PassengerUserId, SeatsBookedByPassenger;
    private String PaymentType, HasPayed, IsAccepted, PhoneNumber;
    private RidePost ridePost;

    public CreateBookingServerRequest(Context context) {
        super();
        this.context = context;
    }

    public void setRidePost(RidePost ridePost) {
        this.ridePost = ridePost;
    }

    @Override
    protected String doInBackground(String... args) {
        ownerUserId = args[0];
        RideId = args[1];
        PassengerUserId = args[2];
        SeatsBookedByPassenger = args[3];
        PaymentType = args[4];
        HasPayed = args[5];
        PhoneNumber = args[6];
//        BookingId = args[6];

        String signup_request_url = context.getString(R.string.booking_request_url);;

        try {
            String rideParameters = "&ownerUserId=" + ownerUserId;
            rideParameters += "&RideId=" + RideId;
            rideParameters += "&PassengerUserId=" + PassengerUserId;
            rideParameters += "&SeatsBookedByPassenger=" + SeatsBookedByPassenger;
            rideParameters += "&PaymentType=" + PaymentType;
            rideParameters += "&HasPayed=" + HasPayed;
            rideParameters += "&bookingUserPhoneNumber=" + PhoneNumber;

//            rideParameters += "&BookingId=" + BookingId;


            System.out.println("THIS IS BOOKING ID !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            System.out.println("Booking ID: " + BookingId);

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
        pleaseWaitDialog.setTitle("Creating Booking");
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

        System.out.println("THIS IS RESULT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Result: " + result);

        if (result == null || result.equals("") || result.equals("failed") || (splitResult != null && splitResult[0].equals("failed"))) {
            alertDialog.setTitle("Something went wrong");
            alertDialog.setMessage("Could not create booking");
            alertDialog.show();
        } else if (splitResult != null && splitResult[0].equals("success")) {
//            Toast.makeText(getApplicationContext(), "finished booking", Toast.LENGTH_SHORT).show();
        }

        FragmentTransaction transaction = ((AppActivity)context).getSupportFragmentManager().beginTransaction();
        RideDetailFragment rideDetailFragment = new RideDetailFragment(RideDetailFragment.ACCESSOR_PASSENGER, ridePost);
        transaction.replace(R.id.fragment_container, rideDetailFragment);
        transaction.commit();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
