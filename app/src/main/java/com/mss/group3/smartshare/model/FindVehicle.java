package com.mss.group3.smartshare.model;

        import android.location.Address;
        import android.location.Geocoder;

        import com.mss.group3.smartshare.common.User;
        import com.mss.group3.smartshare.utility.DistanceAndTimeApiCall;

        import java.io.IOException;
        import java.util.Calendar;
        import java.util.List;

/**
 * Created by inder on 2016-02-20.
 */
public class FindVehicle extends User {


    private Calendar  departureDate;
    private Calendar  arrivalDate;

    private double    distnaceInMeters;
    private double    timeInMinutes;
    private String    departureAddressLineOneText;
    private String    departureAddressCityNameText;
    private String    departureAddressCountryNameText;
    private String    departureAddressPostalCodeText;

    private String    arrivalAddressLineOneText;
    private String    arrivalAddressCityNameText;
    private String    arrivalAddressCountryNameText;
    private String    arrivalAddressPostalCodeText;


    public String getDepartureAddressLineOneText() {
        return departureAddressLineOneText;
    }

    public void setDepartureAddressLineOneText(String departureAddressLineOneText) {
        this.departureAddressLineOneText = departureAddressLineOneText;
    }

    public String getDepartureAddressCityNameText() {
        return departureAddressCityNameText;
    }

    public void setDepartureAddressCityNameText(String departureAddressCityNameText) {
        this.departureAddressCityNameText = departureAddressCityNameText;
    }

    public String getDepartureAddressCountryNameText() {
        return departureAddressCountryNameText;
    }

    public void setDepartureAddressCountryNameText(String departureAddressCountryNameText) {
        this.departureAddressCountryNameText = departureAddressCountryNameText;
    }

    public String getDepartureAddressPostalCodeText() {
        return departureAddressPostalCodeText;
    }

    public void setDepartureAddressPostalCodeText(String departureAddressPostalCodeText) {
        this.departureAddressPostalCodeText = departureAddressPostalCodeText;
    }

    public String getArrivalAddressLineOneText() {
        return arrivalAddressLineOneText;
    }

    public void setArrivalAddressLineOneText(String arrivalAddressLineOneText) {
        this.arrivalAddressLineOneText = arrivalAddressLineOneText;
    }

    public String getArrivalAddressCityNameText() {
        return arrivalAddressCityNameText;
    }

    public void setArrivalAddressCityNameText(String arrivalAddressCityNameText) {
        this.arrivalAddressCityNameText = arrivalAddressCityNameText;
    }

    public String getArrivalAddressCountryNameText() {
        return arrivalAddressCountryNameText;
    }

    public void setArrivalAddressCountryNameText(String arrivalAddressCountryNameText) {
        this.arrivalAddressCountryNameText = arrivalAddressCountryNameText;
    }

    public String getArrivalAddressPostalCodeText() {
        return arrivalAddressPostalCodeText;
    }

    public void setArrivalAddressPostalCodeText(String arrivalAddressPostalCodeText) {
        this.arrivalAddressPostalCodeText = arrivalAddressPostalCodeText;
    }



    public double getDistnaceInMeters() {
        return distnaceInMeters;
    }

    public void setDistnaceInMeters(double distnaceInMeters) {
        this.distnaceInMeters = distnaceInMeters;
    }

    public double getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(double timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public Calendar getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Calendar departureDate) {
        this.departureDate = departureDate;
    }

    public Calendar getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Calendar arrivalDate) {
        this.arrivalDate = arrivalDate;
    }




    public boolean findDistanceAndDuration(Geocoder geoCoder)
    {
        //la lattitude
        //ln longitude
        double la1 = 0, la2 = 0, ln1 = 0, ln2 = 0;

        boolean result;


        try {



            List<Address> addresses =
                    geoCoder.getFromLocationName(
                            getDepartureAddressCityNameText() + "," +
                                    getDepartureAddressCountryNameText() + "," +
                                    getDepartureAddressPostalCodeText(), 4);

            if (addresses.size() > 0) {

                la1 = addresses.get(0).getLatitude();
                ln1 = addresses.get(0).getLongitude();

            }

            List<Address> addressesArrival =
                    geoCoder.getFromLocationName(getArrivalAddressLineOneText() + "," +
                            getArrivalAddressCityNameText() + "," +
                            getArrivalAddressCountryNameText() + "," +
                            getArrivalAddressPostalCodeText(), 4);

            if (addressesArrival.size() > 0) {
                la2 = addressesArrival.get(0).getLatitude();
                ln2 = addressesArrival.get(0).getLongitude();

            }

            DistanceAndTimeApiCall apiCall = new DistanceAndTimeApiCall(la1, ln1, la2, ln2);
            apiCall.calculate();

            FindVehiclelistSingleton obj = FindVehiclelistSingleton.getInstance();
            obj.distance = apiCall.getDistance();
            setDistnaceInMeters(apiCall.getDistance());
            setTimeInMinutes(apiCall.getDuration());

            result = true;

        } catch (IOException ew) {
            ew.printStackTrace();
            result = false;
        }
        return result;
    }

}
