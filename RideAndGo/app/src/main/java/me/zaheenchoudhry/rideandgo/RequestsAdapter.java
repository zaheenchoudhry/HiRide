package me.zaheenchoudhry.rideandgo;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import static me.zaheenchoudhry.rideandgo.RideDetailFragment.ACCESSOR_DRIVER;
import static me.zaheenchoudhry.rideandgo.RideDetailFragment.ACCESSOR_PASSENGER;
import static me.zaheenchoudhry.rideandgo.RideDetailFragment.ACCESSOR_VIEWER;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RidePostHolder> {

    private List<RidePost> ridePostList;
    private float screenX, screenY;

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

        public RidePostHolder(View itemView, float screenX, float screenY) {
            super(itemView);
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

        public void initializeRidePost(RidePost ridePost) {
            this.ridePost = ridePost;
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

            acceptClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    acceptClick.setBackground(itemView.getContext().getDrawable(R.drawable.ride_detail_button_border));
                    acceptClick.setText("Accepted");
                    acceptClick.setTextColor(Color.parseColor("#444A5A"));

                    rejectClick.setText("Edit");
                    acceptClick.setClickable(false);

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
        }

        @Override
        public void onClick(View view) {

        }
    }

    public RequestsAdapter(List<RidePost> ridePostList, float screenX, float screenY) {
        this.ridePostList = ridePostList;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    @Override
    public RidePostHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.requested_ride_card, viewGroup, false);
        return new RidePostHolder(itemView, screenX, screenY);
    }

    @Override
    public void onBindViewHolder(RidePostHolder holder, int position) {
        holder.initializeRidePost(ridePostList.get(position));
    }

    @Override
    public int getItemCount() {
        return ridePostList.size();
    }
}
