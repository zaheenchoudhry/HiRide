package me.zaheenchoudhry.rideandgo;

public class RidePost {

    private int rideId, ownerUserId, day, date, month, year, hour, minute, seats;
    private double price, pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude;
    private String pickupAddressFull, dropoffAddressFull, pickupAddressDisplay, dropoffAddressDisplay, pickupCity, dropoffCity;

    public RidePost() {}

    public void setRideAndOwnerId(int rideId, int ownerUserId) {
        this.rideId = rideId;
        this.ownerUserId = ownerUserId;
    }

    public void setDate(int day, int date, int month, int year) {
        this.day = day;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPickupAddress(String pickupAddressFull, String pickupAddressDisplay, String pickupCity) {
        this.pickupAddressFull = pickupAddressFull;
        this.pickupAddressDisplay = pickupAddressDisplay;
        this.pickupCity = pickupCity;
    }

    public void setDropoffAddress(String dropoffAddressFull, String dropoffAddressDisplay, String dropoffCity) {
        this.dropoffAddressFull = dropoffAddressFull;
        this.dropoffAddressDisplay = dropoffAddressDisplay;
        this.dropoffCity = dropoffCity;
    }

    public void setPickupAddressCoordinates(double latitude, double longitude) {
        this.pickupLatitude = latitude;
        this.pickupLongitude = longitude;
    }

    public void setDropoffAddressCoordinates(double latitude, double longitude) {
        this.dropoffLatitude = latitude;
        this.dropoffLongitude = longitude;
    }

    public int getDay() {
        return this.day;
    }

    public int getDate() {
        return this.date;
    }

    public int getMonth() {
        return this.month;
    }

    public int getYear() {
        return this.year;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public int getSeats() {
        return this.seats;
    }

    public double getPrice() {
        return this.price;
    }

    public String getPickupAddressFull() {
        return this.pickupAddressFull;
    }

    public String getDropoffAddressFull() {
        return this.dropoffAddressFull;
    }

    public String getPickupAddressDisplay() {
        return this.pickupAddressDisplay;
    }

    public String getDropoffAddressDisplay() {
        return this.dropoffAddressDisplay;
    }

    public String getPickupCity() {
        return this.pickupCity;
    }

    public String getDropoffCity() {
        return this.dropoffCity;
    }

    /*
            pick-up location
        drop off location
            - fixed or variable, if it variable, free or a charge, specify radius, with different price which different range


        save these options next time for the drive and driver has to have a fixed start location


        start time

        duration

        seats

        price

        date



        option to shortlist post
     */
}
