package me.zaheenchoudhry.rideandgo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserAccount implements Serializable {

    public static final int ACCOUNT_TYPE_APP = 0;
    public static final int ACCOUNT_TYPE_FACEBOOK_ACCOUNT = 1;
    public static final int NUM_OF_PREFERENCES = 4;
    public static final int PREFERENCE_MUSIC = 0;
    public static final int PREFERENCE_FOOD_DRINKS = 1;
    public static final int PREFERENCE_EXTRA_LUGGAGE = 2;
    public static final int PREFERENCE_PETS = 3;

    private int userId, accountType;
    private double rating;
    private int numOfTrips, numOfKm, numOfRiders;
    private String phoneNumber, name, emailId, facebookProfilePicURI, facebookProfileLinkURI, facebookAccountNumber;
    private boolean wasLogginSuccessful;
    private boolean acceptsCash, acceptsInAppPayments;
    private boolean prefersMusic, prefersDrinks, prefersExtraLuggage, prefersPets;

    public UserAccount(String name) {
        this(-1, -1, name, "-1");
        this.wasLogginSuccessful = false;
    }

    public UserAccount(int userId, int accountType, String name, String phoneNumber) {
        this.userId = userId;
        this.accountType = accountType;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.facebookAccountNumber = "-1";
        this.emailId = "";
        this.facebookProfilePicURI = "";
        this.wasLogginSuccessful = true;
    }

    public void setTrips(int trips){
        this.numOfTrips = trips;
    }

    public void setKm(int km){
        this.numOfKm = km;
    }

    public void setRiders(int riders){
        this.numOfRiders = riders;
    }

    public int getTrips() {
        return this.numOfTrips;
    }

    public int getKm() {
        return this.numOfKm;
    }

    public int getRiders() {
        return this.numOfRiders;
    }

    public void setFacebookUserSpecificDetails(String facebookAccountNumber, String facebookProfileLinkURI, String facebookProfilePicURI) {
        this.facebookAccountNumber = facebookAccountNumber;
        this.facebookProfileLinkURI = facebookProfileLinkURI;
        this.facebookProfilePicURI = facebookProfilePicURI;
    }

    public void setAcceptedPaymentMethods(int acceptsCash, int acceptsInAppPayments) {
        this.acceptsCash = (acceptsCash != 0);
        this.acceptsInAppPayments = (acceptsInAppPayments != 0);
    }

    public void setPreferences(int prefersMusic, int prefersDrinks, int prefersExtraLuggage, int prefersPets) {
        this.prefersMusic = (prefersMusic != 0);
        this.prefersDrinks = (prefersDrinks != 0);
        this.prefersExtraLuggage = (prefersExtraLuggage != 0);
        this.prefersPets = (prefersPets != 0);
    }

    public int getUserId() {
        return userId;
    }

    public int getAccountType() {
        return accountType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFacebookAccountNumber() {
        return facebookAccountNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getFacebookProfilePicURI() {
        return facebookProfilePicURI;
    }

    public String getFacebookProfileLinkURI() {
        return facebookProfileLinkURI;
    }

    public boolean doesAcceptCash() {
        return acceptsCash;
    }

    public boolean doesAcceptInAppPayments() {
        return acceptsInAppPayments;
    }

    public boolean doesPreferMusic() {
        return prefersMusic;
    }

    public boolean doesPreferDrinks() {
        return prefersDrinks;
    }

    public boolean doesPreferExtraLuggage() {
        return prefersExtraLuggage;
    }

    public boolean doesPreferPets() {
        return prefersPets;
    }

    public boolean isLoggedIn() {
        return wasLogginSuccessful;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFacebookAccountNumber(String facebookAccountNumber) {
        this.facebookAccountNumber = facebookAccountNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setFacebookProfilePicURI(String facebookProfilePicURI) {
        this.facebookProfilePicURI = facebookProfilePicURI;
    }

    public void setFacebookProfileLinkURI(String facebookProfileLinkURI) {
        this.facebookProfileLinkURI = facebookProfileLinkURI;
    }

    public void setAcceptsCash(boolean acceptsCash) {
        this.acceptsCash = acceptsCash;
    }

    public void setAcceptsInAppPayments(boolean acceptsInAppPayments) {
        this.acceptsInAppPayments = acceptsInAppPayments;
    }

    public void setPrefersMusic(boolean prefersMusic) {
        this.prefersMusic = prefersMusic;
    }

    public void setPrefersDrinks(boolean prefersDrinks) {
        this.prefersDrinks = prefersDrinks;
    }

    public void setPrefersExtraLuggage(boolean prefersExtraLuggage) {
        this.prefersExtraLuggage = prefersExtraLuggage;
    }

    public void setPrefersPets(boolean prefersPets) {
        this.prefersPets = prefersPets;
    }

    public void setWasLogginSuccessful(boolean wasLogginSuccessful) {
        this.wasLogginSuccessful = wasLogginSuccessful;
    }

    public void setAcceptsCash(int acceptsCash) {
        this.acceptsCash = (acceptsCash != 0);
    }

    public void setAcceptsInAppPayments(int acceptsInAppPayments) {
        this.acceptsInAppPayments = (acceptsInAppPayments != 0);
    }

    public void setPrefersMusic(int prefersMusic) {
        this.prefersMusic = (prefersMusic != 0);
    }

    public void setPrefersDrinks(int prefersDrinks) {
        this.prefersDrinks = (prefersDrinks != 0);
    }

    public void setPrefersExtraLuggage(int prefersExtraLuggage) {
        this.prefersExtraLuggage = (prefersExtraLuggage != 0);
    }

    public void setPrefersPets(int prefersPets) {
        this.prefersPets = (prefersPets != 0);
    }

    public void setWasLogginSuccessful(int wasLogginSuccessful) {
        this.wasLogginSuccessful = (wasLogginSuccessful != 0);
    }
}
