package me.zaheenchoudhry.rideandgo;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class BookedRidesAdapter extends RecyclerView.Adapter<BookedRidesAdapter.RidePostHolder> {

    private List<RidePost> ridePostList;

    public static class RidePostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RidePost ridePost;
        private TextView pickupAddressDisplayText, dropoffAddressDisplayText;
        private TextView startCityText, endCityText, dayText, dateText, monthText;
        private TextView seatsText, timeText, timeAmPmText, priceText;
        private String[] dayOfMonth = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        private String[] dayOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

        public RidePostHolder(View itemView) {
            super(itemView);
            pickupAddressDisplayText = (TextView)itemView.findViewById(R.id.pickup_address_text);
            dropoffAddressDisplayText = (TextView)itemView.findViewById(R.id.dropoff_address_text);
            startCityText = (TextView)itemView.findViewById(R.id.start_city);
            endCityText = (TextView)itemView.findViewById(R.id.end_city);
            dayText = (TextView)itemView.findViewById(R.id.date_day);
            dateText = (TextView)itemView.findViewById(R.id.date_date);
            monthText = (TextView)itemView.findViewById(R.id.date_month);
            priceText = (TextView)itemView.findViewById(R.id.price_num);
            seatsText = (TextView)itemView.findViewById(R.id.seat_num);
            timeText = (TextView)itemView.findViewById(R.id.time_num);
            timeAmPmText = (TextView)itemView.findViewById(R.id.time_num_am_pm);
            //itemView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction transaction = ((AppActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
                    RideDetailFragment rideDetailFragment = new RideDetailFragment(RideDetailFragment.ACCESSOR_PASSENGER, ridePost, false);
                    transaction.replace(R.id.fragment_container, rideDetailFragment);
                    transaction.commit();
                }
            });
        }

        public void initializeRidePost(RidePost ridePost) {
            this.ridePost = ridePost;
            int hourFormat12 = (ridePost.getHour() > 12) ? (ridePost.getHour() - 12) : ridePost.getHour();
            hourFormat12 = (hourFormat12 == 0) ? 12 : hourFormat12;
            String timeString = Integer.toString(hourFormat12) + ":";
            timeString += (ridePost.getMinute() < 10) ? ("0" + Integer.toString(ridePost.getMinute())) : Integer.toString(ridePost.getMinute());
            timeText.setText(timeString);
            DecimalFormat df = new DecimalFormat("#.00");
            String priceString = (ridePost.getPrice() != 0) ? df.format(ridePost.getPrice()) : "0.00";
            priceText.setText("$" + priceString);
            timeAmPmText.setText((ridePost.getHour() >= 12) ? "PM" : "AM");
            pickupAddressDisplayText.setText(ridePost.getPickupAddressDisplay());
            dropoffAddressDisplayText.setText(ridePost.getDropoffAddressDisplay());
            startCityText.setText(ridePost.getPickupCity());
            endCityText.setText(ridePost.getDropoffCity());
            seatsText.setText(Integer.toString(ridePost.getSeatsTotal()));
            dayText.setText(dayOfWeek[ridePost.getDay() - 1]);
            monthText.setText(dayOfMonth[ridePost.getMonth() - 1]);
            dateText.setText((ridePost.getDate() < 10) ? ("0" + Integer.toString(ridePost.getDate())) : Integer.toString(ridePost.getDate()));
        }

        @Override
        public void onClick(View view) {

        }
    }

    public BookedRidesAdapter(List<RidePost> ridePostList) {
        this.ridePostList = ridePostList;
    }

    @Override
    public RidePostHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offered_rides_card, viewGroup, false);
        return new RidePostHolder(itemView);
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
