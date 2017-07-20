package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.content.Context;
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
    private AlertDialog alertDialog;

    public CreateRideServerRequest(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        String day = args[0];
        String date = args[1];
        String month = args[2];
        String year = args[3];
        String hour = args[4];
        String minute = args[5];
        String seats = args[6];
        String price = args[7];
        String pickupAddressFull = args[8];
        String dropoffAddressFull = args[9];
        String pickupAddressDisplay = args[10];
        String dropoffAddressDisplay = args[11];
        String pickupCity = args[12];
        String dropoffCity = args[13];
        String pickupLatitude = args[14];
        String pickupLongitude = args[15];
        String dropoffLatitude = args[16];
        String dropoffLongitude = args[17];
        String signup_request_url = "http://zaheenchoudhry.me/rideandgo/newRideRequest.php";

        try {
            /*String signup_request_data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(dropoffLongitude, "UTF-8");
            signup_request_data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            signup_request_data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");*/

            String rideParameters = "day=" + day;
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

            System.out.println("pickupLatitude string : " + pickupLatitude);
            System.out.println("pickupLongitude string : " + pickupLongitude);

            URL url = new URL(signup_request_url);
            URLConnection connection = url.openConnection();
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
        alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Create Ride Response");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
