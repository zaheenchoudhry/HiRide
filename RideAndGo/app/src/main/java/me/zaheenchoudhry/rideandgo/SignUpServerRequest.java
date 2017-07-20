package me.zaheenchoudhry.rideandgo;

//SignUpServerRequest

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SignUpServerRequest extends AsyncTask<String, Void, String> {

    private Context context;
    private AlertDialog alertDialog;

    public SignUpServerRequest(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        String name = args[0];
        String email = args[1];
        String password = args[2];
        String signup_request_url = "http://zaheenchoudhry.me/rideandgo/signupRequest.php";

        try {
            String signup_request_data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(name, "UTF-8");
            signup_request_data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            signup_request_data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");

            URL url = new URL(signup_request_url);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(signup_request_data);
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
        alertDialog.setTitle("Signup Response");
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
