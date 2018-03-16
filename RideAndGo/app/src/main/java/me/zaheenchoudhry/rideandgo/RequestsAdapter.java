package me.zaheenchoudhry.rideandgo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;

import static me.zaheenchoudhry.rideandgo.RideDetailFragment.ACCESSOR_DRIVER;
import static me.zaheenchoudhry.rideandgo.RideDetailFragment.ACCESSOR_PASSENGER;
import static me.zaheenchoudhry.rideandgo.RideDetailFragment.ACCESSOR_VIEWER;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RidePostHolder> {

    private RidePost ridePost;
    private final Context context;
    private List<RidePost> ridePostList;
    private float screenX, screenY;
    private SetBookingStatusServerRequest setBookingStatusNull;
    private SetBookingStatusServerRequest setBookingStatusAccepted;
    private SetBookingStatusServerRequest setBookingStatusRejected;


    public static class RidePostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RidePost ridePost;
        private TextView pickupAddressDisplayText, dropoffAddressDisplayText;
        private TextView startCityText, endCityText, dayText, dateText, monthText;
        private TextView seatsText, timeText, timeAmPmText, priceText, driverNameText;
        private String[] dayOfMonth = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        private String[] dayOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        private ImageView driverImage;
        private RelativeLayout driverImageHolder;
        private float screenX, screenY;

        private Button acceptClick, rejectClick;
        SetBookingStatusServerRequest setNull, setAccepted, setRejected;

        public RidePostHolder(View itemView, float screenX, float screenY, SetBookingStatusServerRequest setNull, SetBookingStatusServerRequest setAccepted, SetBookingStatusServerRequest setRejected) {
            super(itemView);
            this.setNull = setNull;
            this.setAccepted = setAccepted;
            this.setRejected = setRejected;
            this.screenX = screenX;
            this.screenY = screenY;
//            pickupAddressDisplayText = (TextView)itemView.findViewById(R.id.pickup_address_text);
//            dropoffAddressDisplayText = (TextView)itemView.findViewById(R.id.dropoff_address_text);
            startCityText = (TextView)itemView.findViewById(R.id.start_city);
            endCityText = (TextView)itemView.findViewById(R.id.end_city);
            dayText = (TextView)itemView.findViewById(R.id.date_day);
            dateText = (TextView)itemView.findViewById(R.id.date_date);
            monthText = (TextView)itemView.findViewById(R.id.date_month);
            priceText = (TextView)itemView.findViewById(R.id.price_num);
            seatsText = (TextView)itemView.findViewById(R.id.seat_num);

            acceptClick = (Button)itemView.findViewById(R.id.accept_click);
            rejectClick = (Button)itemView.findViewById(R.id.reject_click);


            driverImageHolder = (RelativeLayout)itemView.findViewById(R.id.ride_detail_driver_image_holder);
            driverImage = (ImageView)itemView.findViewById(R.id.ride_detail_driver_image);
            driverNameText = (TextView)itemView.findViewById(R.id.ride_detail_driver_name);
//            timeText = (TextView)itemView.findViewById(R.id.time_num);
//            timeAmPmText = (TextView)itemView.findViewById(R.id.time_num_am_pm);
            //itemView.setOnClickListener(this);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    FragmentTransaction transaction = ((AppActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
//                    RideDetailFragment rideDetailFragment = new RideDetailFragment(ACCESSOR_DRIVER, ridePost, false);
//                    transaction.replace(R.id.fragment_container, rideDetailFragment);
//                    transaction.commit();
//                }
//            });
        }

        public void initializeRidePost(final RidePost ridePost, Context context) {
            this.ridePost = ridePost;
            final Context appContext = context;
            int hourFormat12 = (ridePost.getHour() > 12) ? (ridePost.getHour() - 12) : ridePost.getHour();
            hourFormat12 = (hourFormat12 == 0) ? 12 : hourFormat12;
            String timeString = Integer.toString(hourFormat12) + ":";
            timeString += (ridePost.getMinute() < 10) ? ("0" + Integer.toString(ridePost.getMinute())) : Integer.toString(ridePost.getMinute());
//            timeText.setText(timeString);
            DecimalFormat df = new DecimalFormat("#.00");
            String priceString = (ridePost.getPrice() != 0) ? df.format(ridePost.getPrice()) : "0.00";
            priceText.setText("$" + priceString);
//            timeAmPmText.setText((ridePost.getHour() >= 12) ? "PM" : "AM");
//            pickupAddressDisplayText.setText(ridePost.getPickupAddressDisplay());
//            dropoffAddressDisplayText.setText(ridePost.getDropoffAddressDisplay());
            startCityText.setText(ridePost.getPickupCity());
            endCityText.setText(ridePost.getDropoffCity());
            seatsText.setText(Integer.toString(ridePost.getSeatsTotal()));
            dayText.setText(dayOfWeek[ridePost.getDay() - 1]);
            monthText.setText(dayOfMonth[ridePost.getMonth() - 1]);
            dateText.setText((ridePost.getDate() < 10) ? ("0" + Integer.toString(ridePost.getDate())) : Integer.toString(ridePost.getDate()));


            if (ridePost.getIsAccepted().equals("1")) {
                acceptClick.setBackground(itemView.getContext().getDrawable(R.drawable.ride_detail_button_border));
                acceptClick.setText("Accepted");
                acceptClick.setTextColor(Color.parseColor("#444A5A"));

                rejectClick.setText("Edit");
                acceptClick.setClickable(false);
            } else if (ridePost.getIsAccepted().equals("2")) {
                acceptClick.setBackground(itemView.getContext().getDrawable(R.drawable.ride_detail_button_border));
                acceptClick.setTextColor(Color.parseColor("#444A5A"));

                acceptClick.setText("Rejected");
                rejectClick.setText("Edit");

                acceptClick.setClickable(false);
            }


            acceptClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    acceptClick.setBackground(itemView.getContext().getDrawable(R.drawable.ride_detail_button_border));
                    acceptClick.setText("Accepted");
                    acceptClick.setTextColor(Color.parseColor("#444A5A"));

                    rejectClick.setText("Edit");
                    acceptClick.setClickable(false);

                    OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
                    System.out.println("CURR OSID:" + status.getSubscriptionStatus().getUserId());
                    System.out.println("SENDING OSID:" + ridePost.getPassenger().getOneSignalId()  + ridePost.getPassenger().getUserId());


                    try {
                        JSONObject notificationContent = new JSONObject(
                                "{'contents': {'en': 'Your booking request for ride going from " + ridePost.getPickupCity() + " to " + ridePost.getDropoffCity() + " has been accepted.'}," +
                                        "'include_player_ids': ['" + ridePost.getPassenger().getOneSignalId() + "'], " +
                                        "'headings': {'en': 'Booking Request Accepted'}, " +
                                        "'big_picture': ''}");
                        OneSignal.postNotification(notificationContent, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setAccepted.execute(String.valueOf(ridePost.getBookingId()));
                }
            });

            rejectClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rejectClick.getText().equals("Reject")) {

                        acceptClick.setBackground(itemView.getContext().getDrawable(R.drawable.ride_detail_button_border));
                        acceptClick.setTextColor(Color.parseColor("#444A5A"));

                        acceptClick.setText("Rejected");
                        rejectClick.setText("Edit");

                        acceptClick.setClickable(false);

                        try {
                            JSONObject notificationContent = new JSONObject(
                                    "{'contents': {'en': 'Your booking request for ride going from " + ridePost.getPickupCity() + " to " + ridePost.getDropoffCity() + " has been rejected.'}," +
                                            "'include_player_ids': ['" + ridePost.getPassenger().getOneSignalId() + "'], " +
                                            "'headings': {'en': 'Booking Request Rejected'}, " +
                                            "'big_picture': ''}");
                            OneSignal.postNotification(notificationContent, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        setRejected.execute(String.valueOf(ridePost.getBookingId()));

                    } else if (rejectClick.getText().equals("Edit")) {

                        acceptClick.setBackgroundColor(Color.parseColor("#444A5A"));
                        acceptClick.setTextColor(Color.parseColor("#ffffff"));
                        acceptClick.setClickable(true);
                        acceptClick.setText("Accept");
                        rejectClick.setText("Reject");

                    }

                }
            });

            SetProfileImageAsyncTask setProfileImageAsyncTask = new SetProfileImageAsyncTask(driverImage);
            setProfileImageAsyncTask.execute(ridePost.getPassenger().getFacebookProfilePicURI());

            driverNameText.setText(ridePost.getPassenger().getName());

            RelativeLayout.LayoutParams driverImageHolderParams = (RelativeLayout.LayoutParams)driverImageHolder.getLayoutParams();
            RelativeLayout.LayoutParams driverImageParams = (RelativeLayout.LayoutParams)driverImage.getLayoutParams();
            RelativeLayout.LayoutParams driverNameTextParams = (RelativeLayout.LayoutParams)driverNameText.getLayoutParams();
            driverImageHolderParams.width = (int)(screenY * 0.075f);
            driverImageHolderParams.height = (int)(screenY * 0.075f);
            driverImageParams.width = (int)(screenY * 0.075f);
            driverImageParams.height = (int)(screenY * 0.075f);
            driverNameText.setTextSize((int)(screenX * 0.009f));
            driverNameTextParams.topMargin = (int)(screenY * 0.002f);

            driverImageHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = ridePost.getPassenger().getFacebookProfileLinkURI();

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    appContext.startActivity(i);
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }



    class SetBookingStatusServerRequest extends AsyncTask<String, Void, String> {
        String bookingId;
        String isAccepted;

        public SetBookingStatusServerRequest(String isAccepted) {
            this.isAccepted = isAccepted;
        }

        @Override
        protected String doInBackground(String... args) {
            this.bookingId = args[0];
            String result = "";
            String string_url = context.getString(R.string.get_set_booking_status_url);

            String rideParameters = "&BookingId=" + this.bookingId;
            rideParameters += "&IsAccepted=" + isAccepted;

            try {
                URL url = new URL(string_url);
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

                String line = bufferedReader.readLine();

                while (line != null) {
                    result += line;
                    line = bufferedReader.readLine();
                }

                bufferedReader.close();
                inputStreamReader.close();

                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            System.out.println(result);
        }
    }

    public RequestsAdapter(List<RidePost> ridePostList, float screenX, float screenY, Context context) {
        this.ridePostList = ridePostList;
        this.screenX = screenX;
        this.screenY = screenY;
        this.context = context;
        this.setBookingStatusNull = new SetBookingStatusServerRequest("0");
        this.setBookingStatusAccepted = new SetBookingStatusServerRequest("1");
        this.setBookingStatusRejected = new SetBookingStatusServerRequest("2");
    }

    @Override
    public RidePostHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.requested_ride_card, viewGroup, false);

        return new RidePostHolder(itemView, screenX, screenY, setBookingStatusNull, setBookingStatusAccepted, setBookingStatusRejected);
    }

    @Override
    public void onBindViewHolder(RidePostHolder holder, int position) {
        holder.initializeRidePost(ridePostList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return ridePostList.size();
    }
}
