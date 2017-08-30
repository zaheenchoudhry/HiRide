package me.zaheenchoudhry.rideandgo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

public class CreateAccountServerRequest extends AsyncTask<String, Void, String> {

    private Context context;
    //private AlertDialog alertDialog;
    private ProgressDialog pleaseWaitDislog;

    public CreateAccountServerRequest(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected String doInBackground(String... args) {
        String accountType = args[0];
        String name = args[1];
        String phoneNumber = args[2];
        String email = args[3];
        String password = args[4];
        String facebookAccountNumber = args[5];
        String facebookProfilePicURI = args[6];
        String signup_request_url = context.getString(R.string.new_account_request_url);

        System.out.println("INFO STARTS HERE!!!!");
        System.out.println("AccountType " + accountType);
        System.out.println("Name " + name);
        System.out.println("PhoneNumber " + phoneNumber);
        System.out.println("INFO ENDS HERE!!!!");

        try {
            String accountParameters = "&accountType=" + accountType;
            accountParameters += "&name=" + URLEncoder.encode(name, "UTF-8");
            accountParameters += "&phoneNumber=" + phoneNumber;
            accountParameters += "&email=" + URLEncoder.encode(email, "UTF-8");
            accountParameters += "&password=" + URLEncoder.encode(password, "UTF-8");
            accountParameters += "&facebookAccountNumber=" + facebookAccountNumber;
            accountParameters += "&facebookProfilePicURI=" + URLEncoder.encode(facebookProfilePicURI, "UTF-8");

            URL url = new URL(signup_request_url);
            URLConnection connection = url.openConnection();
            //connection.setConnectTimeout(5000);
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
        pleaseWaitDislog = new ProgressDialog(context);
        pleaseWaitDislog.setTitle("Creating Account");
        pleaseWaitDislog.setMessage("Please wait...");
        pleaseWaitDislog.setCanceledOnTouchOutside(false);
        pleaseWaitDislog.show();
        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Create Ride Response");
    }

    @Override
    protected void onPostExecute(String result) {
        pleaseWaitDislog.dismiss();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Create Ride Response");
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
