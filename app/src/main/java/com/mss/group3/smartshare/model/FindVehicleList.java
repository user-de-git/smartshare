package com.mss.group3.smartshare.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.controller.UserTypeController;
import com.mss.group3.smartshare.utility.DistanceAndTimeApiCall;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by inder on 2016-03-01.
 */
public class FindVehicleList extends Activity {


    private FindVehicle findVehicle;

    FindVehicleContainer findVehicleContainer;
    List<VehicleWithRangeList> vehicleWithRangeListArray = new ArrayList<>();
    List<VehicleWithRangeList> vehicleWithRangeListProcessArray = new ArrayList<>();
    List<VehicleWithRangeList> bookedVehicles = new ArrayList<>();
    ListView vehiclelistDisplay;
    List<String> vehicleid;
    boolean done = false;
    ArrayAdapter<CharSequence> adaptorVehicleCapacity;
    Spinner spinnerVehicleCapacity;
    int vehicle_capacity;
    FindVehiclelistSingleton obj = FindVehiclelistSingleton.getInstance();
    AlertDialog.Builder builder;
    ParseObject testObject;
    ParseObject userObject;
    double totalPrice = 0;
    double pricePerKm = 0;
    String email;

    Thread t;
    SmsManager smsManager = SmsManager.getDefault();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findvehiclelist);
        vehiclelistDisplay = (ListView) findViewById(R.id.listView);

        ((TextView) findViewById(R.id.filtervehicleNoVehicleFoundMessage)).setVisibility(View.INVISIBLE);




        spinnerVehicleCapacity = (Spinner) findViewById(R.id.filtervehiclecapacity);
        adaptorVehicleCapacity = ArrayAdapter.createFromResource(this,R.array.vehicle_capacity,android.R.layout.simple_spinner_item);
        adaptorVehicleCapacity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleCapacity.setAdapter(adaptorVehicleCapacity);
        spinnerVehicleCapacity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                vehicle_capacity = Integer.parseInt((String) parent.getItemAtPosition(pos));

                if(vehicle_capacity == 0)
                {
                    findVehicleContainer = new
                            FindVehicleContainer(getApplicationContext(), vehicleWithRangeListArray

                    );
                    vehiclelistDisplay.setAdapter(findVehicleContainer);
                    return;
                }

                ((ProgressBar) findViewById(R.id.progressBarFilterVehicle)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.filtervehicleNoVehicleFoundMessage)).setVisibility(View.INVISIBLE);


                vehicleWithRangeListProcessArray.clear();
                vehiclelistDisplay.setAdapter(null);


                int count = 0;
                for( int i = 0; i< vehicleWithRangeListArray.size(); i++)
                {
                    if(vehicleWithRangeListArray.get(i).capacity.equals(vehicle_capacity))
                    {
                        vehicleWithRangeListProcessArray.add(count,vehicleWithRangeListArray.get(i) );
                        count++;
                    }
                }

                if(vehicleWithRangeListProcessArray.size() == 0)
                {
                    ((TextView) findViewById(R.id.filtervehicleNoVehicleFoundMessage)).setVisibility(View.VISIBLE);
                    return;
                }

                findVehicleContainer = new
                        FindVehicleContainer(getApplicationContext(), vehicleWithRangeListProcessArray

                );
                vehiclelistDisplay.setAdapter(findVehicleContainer);
                ((ProgressBar) findViewById(R.id.progressBarFilterVehicle)).setVisibility(View.INVISIBLE);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getVehicleListThatCanBeBooked();

        vehiclelistDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                testObject = new ParseObject("RegisteredVehicles");

                int number = Integer.decode(((TextView) view.findViewById(R.id.plateNumber)).getText().toString());
                //DatePickerDialog d1 = (TextView) view.findViewById(R.id.toDate);
                // Date d2 = (TextView) view.findViewById(R.id.fromDate);
                UserSingleton userName = UserSingleton.getInstance();
                String address;


                for(int i = 0; i< vehicleWithRangeListArray.size(); i++ )
                {
                    if(vehicleWithRangeListArray.get(i).plateNumber.equals(number) &&
                            vehicleWithRangeListArray.get(i).fromDate.before(obj.departureDate.getTime() )
                                    && vehicleWithRangeListArray.get(i).toDate.after(obj.arrivalDate.getTime()))
                    {
                        address = vehicleWithRangeListArray.get(i).postalCode;
                        pricePerKm = vehicleWithRangeListArray.get(i).pricePerKm;
                        double timeBetweenSouceAddressAndDatabaseAddressMinutes = findDistanceAndDuration(address,
                                obj.departureAddressPostalCodeText, 1);

                        email = vehicleWithRangeListArray.get(i).email;
                        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
                        long time =  obj.departureDate.getTime().getTime();
                        time -= timeBetweenSouceAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                        obj.departureDate.setTimeInMillis(time);
                        break;
                    }
                }
                try {

                    testObject.put("PlateNumber", number);
                    testObject.put("SourceAddress", obj.departureAddressPostalCodeText);
                    testObject.put("DestinationAddress", obj.arrivalAddressDepartureCode);
                    testObject.put("StartDate", obj.departureDate.getTime());
                    testObject.put("EndDate", obj.arrivalDate.getTime());
                    testObject.put("RenterEmail", userName.emailAddress);
                } catch (Exception e) {

                }
                builder.setTitle("Confirm");

                builder.setMessage("Total Cost = " + Double.toString(obj.distance * pricePerKm) + " $-- " + "Are you ok?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog



                      sendSMS();

                        // Do nothing but close the dialog
                        testObject.saveInBackground();


                        //Do something
                        //Ex: display msg with product id get from view.getTag
                        Toast.makeText(getApplicationContext(), "Booked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        Intent myIntent = new Intent(FindVehicleList.this, UserTypeController.class);

                        startActivity(myIntent);


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

    public double findDistanceAndDuration(String postalCodeFromDatabase, String postalCodeUser, int option) {
        //la lattitude
        //ln longitude
        double la1 = 0, la2 = 0, ln1 = 0, ln2 = 0;
        builder = new AlertDialog.Builder(this);
        double distance;
        Point point;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses =
                    geoCoder.getFromLocationName(postalCodeUser, 1);

            if (addresses.size() > 0) {

                la1 = addresses.get(0).getLatitude();
                ln1 = addresses.get(0).getLongitude();

            }

            List<Address> addressesArrival =
                    geoCoder.getFromLocationName(postalCodeFromDatabase, 1);

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

    public void sendSMS()
    {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContains("username", email);
        query.findInBackground(new FindCallback<ParseUser>() {


            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    try
                    {
                        for (ParseUser p : objects) {
                            smsManager.sendTextMessage("+1" + p.getString("userContactNumber"), null,
                                    "your vehicle is booked by " + email + " from " +
                                            obj.departureAddressPostalCodeText +
                                            " To " + obj.arrivalAddressDepartureCode, null, null);
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

        try {


            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("VehicleTable");


           /* ParseObject testObject;
            testObject = new ParseObject("VehicleTable");

            testObject.put("FromDate", obj.departureDate.getTime());
            testObject.put("ToDate", obj.arrivalDate.getTime());
            testObject.saveInBackground();*/


            query.whereLessThanOrEqualTo("FromDate", obj.departureDate.getTime());
            query.whereGreaterThanOrEqualTo("ToDate", obj.arrivalDate.getTime());

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, com.parse.ParseException e) {
                    if (e == null) {
                        for (ParseObject p : list) {
                            vehicleWithRangeListArray.add(new VehicleWithRangeList(p.getObjectId(), p.getInt("Plate_number"), p.getString("Vehicle_type"),
                                    p.getInt("Capacity"), p.getInt("vehicle_range"),p.getString("PostalCode"),
                                    p.getDate("FromDate"), p.getDate("ToDate"),p.getInt("Price_km"),p.getString("Owner_email")));
                        }


                        //vehicles with in given range (Range Filter)
                        for (int i = vehicleWithRangeListArray.size() - 1; i >= 0; i--) {
                            VehicleWithRangeList ob = vehicleWithRangeListArray.get(i);
                            double distanceBetweenTwoUsers = findDistanceAndDuration(ob.postalCode, obj.departureAddressPostalCodeText, 0);
                            if (distanceBetweenTwoUsers * 1000 > (ob.kmRange * 1000))

                            {
                                vehicleWithRangeListArray.remove(i);
                            }

                        }


                        //add driving time at both ends
                        for (int i = vehicleWithRangeListArray.size() - 1; i >= 0; i--) {
                            VehicleWithRangeList ob = vehicleWithRangeListArray.get(i);
                            double timeBetweenSouceAddressAndDatabaseAddressMinutes = findDistanceAndDuration(ob.postalCode,
                                    obj.departureAddressPostalCodeText, 1);
                            double timeBetweenDepartureAddressAndDatabaseAddressMinutes = findDistanceAndDuration(ob.postalCode,
                                    obj.arrivalAddressDepartureCode, 1);
                            final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
                            long time = ob.fromDate.getTime();
                            time += timeBetweenSouceAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                            ob.fromDate.setTime(time);

                            time = ob.toDate.getTime();
                            time -= timeBetweenDepartureAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                            ob.toDate.setTime(time);

                        }


                        //get vehicle IDS from current vehicle list
                        final List<Integer> vehicleID = new ArrayList<Integer>();


                        //remove the vehicles that can not be booked
                        for (int i = 0; i < vehicleWithRangeListArray.size(); i++) {
                            VehicleWithRangeList ob = vehicleWithRangeListArray.get(i);

                            String a = obj.departureDate.getTime().toString();
                            if (obj.departureDate.getTime().compareTo(ob.fromDate) < 0 || obj.arrivalDate.getTime().compareTo(ob.toDate) > 0) {
                                vehicleWithRangeListArray.remove(i);
                            }

                            vehicleID.add(i, ob.plateNumber);
                        }

                        if(vehicleWithRangeListArray.size() == 0)
                        {
                            ((TextView) findViewById(R.id.filtervehicleNoVehicleFoundMessage)).setVisibility(View.VISIBLE);
                            return;
                        }

                        //filter vehicle from already registered vehivle
                        //to verify the schedule mismatch if already booking is done
                        ParseQuery<ParseObject> queryForRegisterdVehicles = new ParseQuery<ParseObject>("RegisteredVehicles");
                        queryForRegisterdVehicles.whereContainedIn("PlateNumber", vehicleID);
                        queryForRegisterdVehicles.whereLessThanOrEqualTo("StartDate", obj.arrivalDate.getTime());
                        queryForRegisterdVehicles.whereGreaterThanOrEqualTo("EndDate",obj.departureDate.getTime() );
                        queryForRegisterdVehicles.findInBackground(new FindCallback<ParseObject>() {
                                                                       @Override
                                                                       public void done(List<ParseObject> list, com.parse.ParseException e) {
                                                                           if (e == null) {
                                                                               for (ParseObject p : list) {
                                                                                   bookedVehicles.add(new VehicleWithRangeList(p.getObjectId(),
                                                                                           p.getInt("PlateNumber"), "NR",
                                                                                           0, 0,
                                                                                           "NR",
                                                                                           p.getDate("StartDate"), p.getDate("EndDate"),0,"fg"));

                                                                               }


                                                                               //compare plate number and check timing (if conflict remove)
                                                                               for (int i = 0; i < bookedVehicles.size(); i++) {

                                                                                   for (int j = 0; j < vehicleWithRangeListArray.size(); j++) {
                                                                                       if (vehicleWithRangeListArray.get(j).plateNumber.equals(bookedVehicles.get(i).plateNumber)) {

                                                                                           //now see timing conflicts and remove element
                                                                                           if (bookedVehicles.get(i).fromDate.after(vehicleWithRangeListArray.get(j).fromDate)) {
                                                                                               if (bookedVehicles.get(i).fromDate.before(vehicleWithRangeListArray.get(j).toDate)) {
                                                                                                   vehicleWithRangeListArray.remove(j);
                                                                                               }
                                                                                           } else {

                                                                                               if (bookedVehicles.get(i).toDate.before(vehicleWithRangeListArray.get(j).toDate)) {
                                                                                                   vehicleWithRangeListArray.remove(j);
                                                                                               }
                                                                                           }
                                                                                       }
                                                                                   }

                                                                               }


                                                                               //remove driving time at both ends
                                                                               for (int i = vehicleWithRangeListArray.size() - 1; i >= 0; i--) {
                                                                                   VehicleWithRangeList ob = vehicleWithRangeListArray.get(i);
                                                                                   double timeBetweenSouceAddressAndDatabaseAddressMinutes = findDistanceAndDuration(ob.postalCode,
                                                                                           obj.departureAddressPostalCodeText, 1);
                                                                                   double timeBetweenDepartureAddressAndDatabaseAddressMinutes = findDistanceAndDuration(ob.postalCode,
                                                                                           obj.arrivalAddressDepartureCode, 1);
                                                                                   final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
                                                                                   long time = ob.fromDate.getTime();
                                                                                   time -= timeBetweenSouceAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                                                                                   ob.fromDate.setTime(time);

                                                                                   time = ob.toDate.getTime();
                                                                                   time -= timeBetweenDepartureAddressAndDatabaseAddressMinutes * ONE_MINUTE_IN_MILLIS;
                                                                                   ob.toDate.setTime(time);
                                                                               }


                                                                               done = true;
                                                                               if(vehicleWithRangeListArray.size() == 0)
                                                                               {
                                                                                   ((TextView) findViewById(R.id.filtervehicleNoVehicleFoundMessage)).setVisibility(View.VISIBLE);
                                                                                   return;
                                                                               }
                                                                               findVehicleContainer = new
                                                                                       FindVehicleContainer(getApplicationContext(), vehicleWithRangeListArray

                                                                               );
                                                                               vehiclelistDisplay.setAdapter(findVehicleContainer);
                                                                               ((ProgressBar) findViewById(R.id.progressBarFilterVehicle)).setVisibility(View.INVISIBLE);
                                                                               ((TextView) findViewById(R.id.filtervehicleNoVehicleFoundMessage)).setVisibility(View.INVISIBLE);

                                                                           }
                                                                           if (list.size() == 0) {

                                                                               if(vehicleWithRangeListArray.size() == 0)
                                                                               {
                                                                                   ((TextView) findViewById(R.id.filtervehicleNoVehicleFoundMessage)).setVisibility(View.VISIBLE);
                                                                                   return;
                                                                               }

                                                                               findVehicleContainer = new
                                                                                       FindVehicleContainer(getApplicationContext(), vehicleWithRangeListArray

                                                                               );
                                                                               vehiclelistDisplay.setAdapter(findVehicleContainer);
                                                                               ((ProgressBar) findViewById(R.id.progressBarFilterVehicle)).setVisibility(View.INVISIBLE);
                                                                               ((TextView) findViewById(R.id.filtervehicleNoVehicleFoundMessage)).setVisibility(View.INVISIBLE);
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
