package me.zaheenchoudhry.rideandgo;

public class Booking {

    private int ownerUserId;
    private int BookingId;
    private int rideId;
    private int PassengerUserId;
    private int SeatsBookedByPassenger;
    private int PaymentType;
    private int HasPayed;
    private int IsAccepted;

    public Booking() {}


    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public int getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(int paymentType) {
        PaymentType = paymentType;
    }

    public int getHasPayed() {
        return HasPayed;
    }

    public void setHasPayed(int hasPayed) {
        HasPayed = hasPayed;
    }

    public int getIsAccepted() {
        return IsAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        IsAccepted = isAccepted;
    }


    public int getBookingId() {
        return BookingId;
    }

    public void setBookingId(int bookingId) {
        this.BookingId = bookingId;
    }


    public int getPassengerUserId() {
        return PassengerUserId;
    }

    public void setPassengerUserId(int passengerUserId) {
        this.PassengerUserId = passengerUserId;
    }


    public int getSeatsBookedByPassenger() {
        return SeatsBookedByPassenger;
    }

    public void setSeatsBookedByPassenger(int seatsBookedByPassenger) {
        this.SeatsBookedByPassenger = seatsBookedByPassenger;
    }
}
