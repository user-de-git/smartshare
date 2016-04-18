package com.mss.group3.smartshare.model;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.common.SaveSharedPreference;
import com.mss.group3.smartshare.controller.FindVehicleController;
import com.mss.group3.smartshare.controller.LoginController;
import com.mss.group3.smartshare.controller.MyAccountController;
import com.mss.group3.smartshare.controller.PostVehicleController;
import com.mss.group3.smartshare.controller.UserTypeController;
import com.mss.group3.smartshare.utility.DistanceAndTimeApiCall;
import com.parse.FindCallback;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by inder on 2016-03-01.
 * This class will search the vehicle from database
 * and will remove the conflicting vehicles
 */
public class FindVehicleList extends AppCompatActivity {


    private static int lastVehicleRemovedCheck = 0;
    private FindVehicleContainer findVehicleContainerAdapter;
    private List<VehicleWithRangeList> vehicleWithRangeListArray = new ArrayList<>();
    private List<VehicleWithRangeList> vehicleWithRangeListArrayForDisplay = new ArrayList<>();
    private List<VehicleWithRangeList> bookedVehicles = new ArrayList<>();
    private ListView vehiclelistDisplay;
    private LinearLayout mLinearScroll;
    private ParseGeoPoint getCurrentGeoPoint;
    private ParseGeoPoint getNextGeoPoint;
    private FindVehiclelistSingleton objVehicleSingleton = FindVehiclelistSingleton.getInstance();
    private AlertDialog.Builder builder;
    private ParseObject parseObject;
    private double pricePerKm = 0;
    private double perHourLateCharges = 0;
    private String email;
    private SmsManager smsManager = SmsManager.getDefault();
    private int rowSize = 5;
    final long ONE_MINUTE_IN_MILLIS = 60000;
    private String vehiclePlateNumberInProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findvehiclelist);
        vehiclelistDisplay = (ListView) findViewById(R.id.listView);
        mLinearScroll = (LinearLayout) findViewById(R.id.linear_scroll);
        lastVehicleRemovedCheck = 0;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //get all vehicles from database
        getVehicleListThatCanBeBooked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_post) {
            Intent screen = new Intent(FindVehicleList.this,PostVehicleController.class);
            startActivity(screen);

        } else if(id==R.id.action_search) {
            Intent screen = new Intent(FindVehicleList.this,FindVehicleController.class);
            startActivity(screen);
        } else if(id==R.id.action_manage) {
            Intent screen = new Intent(FindVehicleList.this,MyAccountController.class);
            startActivity(screen);
        } else {
            SaveSharedPreference.setUserName(FindVehicleList.this, "");
            SaveSharedPreference.setPassword(FindVehicleList.this, "");
            Intent ownerlayout = new Intent(FindVehicleList.this, LoginController.class);
            startActivity(ownerlayout);
        }
        return super.onOptionsItemSelected(item);
    }



    //add vehicles to display
    //remove conflicting vehicles
    private void addItem(int i) {

        vehicleWithRangeListArrayForDisplay.clear();

        i = i * rowSize;

        for (int j = 0; j < rowSize && i < vehicleWithRangeListArray.size(); j++) {

            if (lastVehicleRemovedCheck < vehicleWithRangeListArray.size()) {

                //add travel timing
                VehicleWithRangeList ob = vehicleWithRangeListArray.get(lastVehicleRemovedCheck);

                // .5* because geo point found only straight distance
                double timeBetweenSouceAddressAndDatabaseAddressMinutes = ob.point.distanceInKilometersTo(getCurrentGeoPoint);
                double timeBetweenDepartureAddressAndDatabaseAddressMinutes = ob.point.distanceInKilometersTo(getNextGeoPoint);
                timeBetweenSouceAddressAndDatabaseAddressMinutes += timeBetweenSouceAddressAndDatabaseAddressMinutes * .5;
                timeBetweenDepartureAddressAndDatabaseAddressMinutes += timeBetweenDepartureAddressAndDatabaseAddressMinutes * .5;

                long time = ob.fromDate.getTime();
                time += timeBetweenSouceAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                ob.fromDate.setTime(time);

                time = ob.toDate.getTime();
                time -= timeBetweenDepartureAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                ob.toDate.setTime(time);

                if (objVehicleSingleton.departureDate.getTime().compareTo(ob.fromDate) < 0 || objVehicleSingleton.arrivalDate.getTime().compareTo(ob.toDate) > 0) {
                    vehicleWithRangeListArray.remove(lastVehicleRemovedCheck);
                    j--;
                    continue;
                }

                boolean removed = false;

                for (int ii = 0; ii < bookedVehicles.size(); ii++) {


                    if (vehicleWithRangeListArray.get(lastVehicleRemovedCheck).plateNumber.equals(bookedVehicles.get(ii).plateNumber)) {

                        //now see timing conflicts and remove element
                        if (bookedVehicles.get(ii).fromDate.after(vehicleWithRangeListArray.get(lastVehicleRemovedCheck).fromDate)) {
                            if (bookedVehicles.get(ii).fromDate.before(vehicleWithRangeListArray.get(lastVehicleRemovedCheck).toDate)) {
                                vehicleWithRangeListArray.remove(lastVehicleRemovedCheck);
                                removed = true;
                                j--;
                                break;
                            }
                        } else {

                            if (bookedVehicles.get(i).toDate.before(vehicleWithRangeListArray.get(lastVehicleRemovedCheck).toDate)) {
                                vehicleWithRangeListArray.remove(lastVehicleRemovedCheck);
                                removed = true;
                                j--;
                                break;
                            }
                        }


                    }
                }

                if (removed) continue;

                //remove driving time at both ends
                time = ob.fromDate.getTime();
                time -= timeBetweenSouceAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                ob.fromDate.setTime(time);

                time = ob.toDate.getTime();
                time -= timeBetweenDepartureAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                ob.toDate.setTime(time);

                lastVehicleRemovedCheck++;
            }

            vehicleWithRangeListArrayForDisplay.add(j, vehicleWithRangeListArray.get(i));
            i++;
        }
        setView();
    }

    //display vehicles in pages and book vehicle on click
    private void setView() {

        findVehicleContainerAdapter = new FindVehicleContainer(getApplicationContext(), vehicleWithRangeListArrayForDisplay

        );
        vehiclelistDisplay.setAdapter(findVehicleContainerAdapter);

        vehiclelistDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parseObject = new ParseObject("RegisteredVehicles");

                String number = ((TextView) view.findViewById(R.id.plateNumber)).getText().toString();
                vehiclePlateNumberInProcess = number;
                UserSingleton userName = UserSingleton.getInstance();
                String address;


                for (int i = 0; i < vehicleWithRangeListArray.size(); i++) {
                    //add travel timing at both ends
                    if ( vehicleWithRangeListArray.get(i).plateNumber.equals(number) &&
                            vehicleWithRangeListArray.get(i).fromDate.before(objVehicleSingleton.departureDate.getTime())
                            && vehicleWithRangeListArray.get(i).toDate.after(objVehicleSingleton.arrivalDate.getTime()))
                    {
                        address = vehicleWithRangeListArray.get(i).postalCode;
                        pricePerKm = vehicleWithRangeListArray.get(i).pricePerKm;
                        perHourLateCharges = vehicleWithRangeListArray.get(i).capacity;
                        double timeBetweenSouceAddressAndDatabaseAddressMinutes =
                                findDistanceAndDuration(address,
                                objVehicleSingleton.departureAddressPostalCodeText, 1);



                        email = vehicleWithRangeListArray.get(i).email;
                        long time = objVehicleSingleton.departureDate.getTime().getTime();
                        time -= timeBetweenSouceAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                        objVehicleSingleton.departureDate.setTimeInMillis(time);


                        double timeBetweenDestinationAddressAndDatabaseAddressMinutes =
                                findDistanceAndDuration(address,
                                        objVehicleSingleton.arrivalAddressDepartureCode, 1);


                         time = objVehicleSingleton.arrivalDate.getTime().getTime();
                        time += timeBetweenDestinationAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                        objVehicleSingleton.arrivalDate.setTimeInMillis(time);


                        break;
                    }
                }
                try {

                    parseObject.put("PlateNumber", number);
                    parseObject.put("SourceAddress", objVehicleSingleton.departureAddressPostalCodeText);
                    parseObject.put("DestinationAddress", objVehicleSingleton.arrivalAddressDepartureCode);
                    parseObject.put("StartDate", objVehicleSingleton.departureDate.getTime());
                    parseObject.put("EndDate", objVehicleSingleton.arrivalDate.getTime());
                    parseObject.put("RenterEmail", userName.emailAddress);
                    parseObject.put("isViewedSharer", false);
                    parseObject.put("TripDone", false);
                    parseObject.put("BaseCost",(objVehicleSingleton.distance/1000 * pricePerKm) );
                    parseObject.put("TotalCost", (objVehicleSingleton.distance/1000 * pricePerKm));
                } catch (Exception e) {

                }
                builder.setTitle("Confirm");
                builder.setMessage("Total Cost = " + String.format("%1.2f", (objVehicleSingleton.distance / 1000 * pricePerKm)) +
                        " $                        " +
                        "           " + "Per Hour late Charges " + perHourLateCharges + " $" +"                     "+ "Are you ok?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("VehicleTable");
                        query.whereEqualTo("Plate_number", vehiclePlateNumberInProcess);
                        query.whereLessThanOrEqualTo("FromDate", objVehicleSingleton.departureDate.getTime());
                        query.whereGreaterThanOrEqualTo("ToDate", objVehicleSingleton.arrivalDate.getTime());
                        query.whereEqualTo("Capacity", objVehicleSingleton.capacity);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, com.parse.ParseException e) {
                                if (e == null) {

                                    if (list.size() == 0) {
                                        Toast.makeText(getApplicationContext(), "Someone Booked!! Retry", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        Intent myIntent = new Intent(FindVehicleList.this, UserTypeController.class);
                                        startActivity(myIntent);
                                    } else {
                                        ParseQuery<ParseObject> queryForRegisterdVehicles = new ParseQuery<ParseObject>("RegisteredVehicles");
                                        queryForRegisterdVehicles.whereEqualTo("PlateNumber", vehiclePlateNumberInProcess);
                                        queryForRegisterdVehicles.whereLessThanOrEqualTo("StartDate", objVehicleSingleton.arrivalDate.getTime());
                                        queryForRegisterdVehicles.whereGreaterThanOrEqualTo("EndDate", objVehicleSingleton.departureDate.getTime());
                                        queryForRegisterdVehicles.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> list, com.parse.ParseException e) {
                                                if (e == null) {

                                                    if (list.size() == 0) {
                                                        // send booking SMS
                                                        sendSMS();

                                                        parseObject.saveInBackground();

                                                        Toast.makeText(getApplicationContext(), "Booked", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();

                                                        //Move to home page
                                                        Intent myIntent = new Intent(FindVehicleList.this, MyAccountController.class);
                                                        myIntent.putExtra("calling-activity", 1001);
                                                        startActivity(myIntent);
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Someone Booked!! Retry", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        Intent myIntent = new Intent(FindVehicleList.this, UserTypeController.class);
                                                        startActivity(myIntent);
                                                    }


                                                }
                                            }
                                        });


                                    }

                                }
                            }
                        });


                    }

                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private double findDistanceAndDuration(String postalCodeFromDatabase, String postalCodeUser, int option) {
        //la lattitude
        //ln longitude
        double la1 = 0, la2 = 0, ln1 = 0, ln2 = 0;
        builder = new AlertDialog.Builder(this);
        double distance;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses =  geoCoder.getFromLocationName(postalCodeUser, 1);

            if (addresses.size() > 0) {

                la1 = addresses.get(0).getLatitude();
                ln1 = addresses.get(0).getLongitude();
            }

            List<Address> addressesArrival =   geoCoder.getFromLocationName(postalCodeFromDatabase, 1);

            if (addressesArrival.size() > 0) {
                la2 = addressesArrival.get(0).getLatitude();
                ln2 = addressesArrival.get(0).getLongitude();
            }

            DistanceAndTimeApiCall apiCall = new DistanceAndTimeApiCall(la1, ln1, la2, ln2);
            apiCall.calculate();

            if (option == 0) {
                distance = apiCall.getDistance();
            } else {
                distance = apiCall.getDuration();
            }

        } catch (IOException ew) {
            ew.printStackTrace();
            distance = 0;
        }
        return distance;
    }


    private ParseGeoPoint getGeoPoint(String address) {
        double la1 = 0, ln1 = 0;
        ParseGeoPoint point = new ParseGeoPoint();
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(address, 1);

            if (addresses.size() > 0) {

                la1 = addresses.get(0).getLatitude();
                ln1 = addresses.get(0).getLongitude();
                point.setLatitude(la1);
                point.setLongitude(ln1);

            }
        } catch (IOException ew) {
            ew.printStackTrace();
        }
        return point;
    }

    public void sendSMS() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContains("username", email);
        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    try {
                        for (ParseUser p : objects) {
                            smsManager.sendTextMessage("+1" + p.getString("userContactNumber"), null,
                                    "your vehicle is booked by " + UserSingleton.getInstance().emailAddress + " from " +
                                            objVehicleSingleton.departureAddressPostalCodeText +
                                            " To " + objVehicleSingleton.arrivalAddressDepartureCode, null, null);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    // Something went wrong.
                }
            }

        });
    }

    public void getVehicleListThatCanBeBooked() {
        vehicleWithRangeListArray.clear();
        lastVehicleRemovedCheck = 0;
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("VehicleTable");
            getCurrentGeoPoint = getGeoPoint(objVehicleSingleton.departureAddressPostalCodeText);
            getNextGeoPoint = getGeoPoint(objVehicleSingleton.arrivalAddressDepartureCode);
            query.whereLessThanOrEqualTo("FromDate", objVehicleSingleton.departureDate.getTime());
            query.whereGreaterThanOrEqualTo("ToDate", objVehicleSingleton.arrivalDate.getTime());
            query.whereEqualTo("Capacity", objVehicleSingleton.capacity);
            query.whereNotEqualTo("Owner_email", UserSingleton.getInstance().emailAddress);
            query.setLimit(10000);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, com.parse.ParseException e) {
                    if (e == null) {
                        List<String> plateNumber = new ArrayList<String>();
                        for (ParseObject p : list) {

                            ParseGeoPoint getGeo = p.getParseGeoPoint("geopoint");
                            double distance = getGeo.distanceInKilometersTo(getCurrentGeoPoint);
                            int ran = p.getInt("vehicle_range");
                            distance = distance + distance * .5;
                            if (getGeo.distanceInKilometersTo(getCurrentGeoPoint) < p.getInt("vehicle_range")) {
                                vehicleWithRangeListArray.add(new VehicleWithRangeList(p.getObjectId(), p.getString("Plate_number"), p.getString("Vehicle_type"),
                                        p.getInt("Overdue_hr_rate"), p.getInt("vehicle_range"), p.getString("Address") +" "+ p.getString("PostalCode"),
                                        p.getDate("FromDate"), p.getDate("ToDate"), p.getInt("Price_km"), p.getString("Owner_email"), getGeo));
                                plateNumber.add(p.getString("Plate_number"));
                            }
                        }
                        //filter vehicle from already registered vehivle
                        //to verify the schedule mismatch if already booking is done
                        ParseQuery<ParseObject> queryForRegisterdVehicles = new ParseQuery<ParseObject>("RegisteredVehicles");
                        queryForRegisterdVehicles.whereContainedIn("PlateNumber", plateNumber);
                        queryForRegisterdVehicles.whereLessThanOrEqualTo("StartDate", objVehicleSingleton.arrivalDate.getTime());
                        queryForRegisterdVehicles.whereGreaterThanOrEqualTo("EndDate", objVehicleSingleton.departureDate.getTime());
                        queryForRegisterdVehicles.findInBackground(new FindCallback<ParseObject>() {
                                                                       @Override
                                                                       public void done(List<ParseObject> list, com.parse.ParseException e) {
                                                                           if (e == null) {
                                                                               for (ParseObject p : list) {
                                                                                   bookedVehicles.add(new VehicleWithRangeList(p.getObjectId(),
                                                                                           p.getString("PlateNumber"), "",
                                                                                           0, 0,
                                                                                           "NR",
                                                                                           p.getDate("StartDate"), p.getDate("EndDate"), 0, "", null));
                                                                               }
                                                                               try {
                                                                                   addItem(0);
                                                                                   int size = vehicleWithRangeListArray.size() / rowSize;

                                                                                   if(vehicleWithRangeListArray.size() % rowSize != 0)
                                                                                   {
                                                                                       size = size+1;
                                                                                   }
                                                                                   for (int j = 0; j < size; j++) {
                                                                                       final int k;
                                                                                       k = j;
                                                                                       final Button btnPage = new Button(FindVehicleList.this);
                                                                                       Toolbar.LayoutParams lp = new Toolbar.LayoutParams(120, ActionBar.LayoutParams.WRAP_CONTENT);
                                                                                       lp.setMargins(5, 2, 2, 2);
                                                                                       btnPage.setTextColor(Color.WHITE);
                                                                                       btnPage.setTextSize(26.0f);
                                                                                       btnPage.setId(j);
                                                                                       btnPage.setText(String.valueOf(j + 1));
                                                                                       mLinearScroll.addView(btnPage, lp);
                                                                                       btnPage.setOnClickListener(new View.OnClickListener() {
                                                                                           @Override
                                                                                           public void onClick(View v) {
                                                                                               addItem(k);
                                                                                           }
                                                                                       });
                                                                                   }
                                                                               } catch (Exception e1) {                                                                        }
                                                                           }
                                                                       }
                                                                   }
                        );
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
