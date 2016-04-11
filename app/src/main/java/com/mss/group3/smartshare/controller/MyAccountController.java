package com.mss.group3.smartshare.controller;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.common.SaveSharedPreference;
import com.mss.group3.smartshare.common.User;
import com.mss.group3.smartshare.model.RentAdaptor;
import com.mss.group3.smartshare.model.RentDataStore;
import com.mss.group3.smartshare.model.ShareAdaptor;
import com.mss.group3.smartshare.model.ShareDataStore;
import com.mss.group3.smartshare.model.UserSingleton;
import com.mss.group3.smartshare.model.VehicleAdaptor;
import com.mss.group3.smartshare.model.VehicleDataStore;
import com.mss.group3.smartshare.utility.LocationServices;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class MyAccountController extends AppCompatActivity {
    ParseUser cUser;
    final Context context = this;
    private ListView lvProduct_shares,lvProduct_rents;
    private ShareAdaptor adapter_shares;
    private RentAdaptor adapter_rents;
    private List<ShareDataStore> mProductList_shares;
    private List<RentDataStore> mProductList_rents;
    ParseQuery<ParseObject> query_shares = new ParseQuery<ParseObject>("RegisteredVehicles");
    ParseQuery<ParseObject> query_rents  = new ParseQuery<ParseObject>("RegisteredVehicles");
    static TabHost host;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_acount);
        lvProduct_shares = (ListView)findViewById(R.id.listView_shares);
        mProductList_shares = new ArrayList<>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Profile");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Shares");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Shares");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Rents");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Rents");
        host.addTab(spec);

        int callingActivity = getIntent().getIntExtra("calling-activity", 0);
        if(callingActivity == 1001)
        host.setCurrentTabByTag("Rents");


        cUser = ParseUser.getCurrentUser();
        if (cUser != null) {

            ((EditText) findViewById(R.id.contactText)).setText(cUser.getString("userContactNumber"));
            ((EditText) findViewById(R.id.firstNameText)).setText(cUser.getString("userFirstName"));
            ((EditText) findViewById(R.id.lastNameText)).setText(cUser.getString("userLastName"));
            ((EditText) findViewById(R.id.emailText)).setText(cUser.getString("email"));

            ((EditText) findViewById(R.id.addressLineOneText)).setText(cUser.getString("userAddressLineOne"));
            ((EditText) findViewById(R.id.cityNameText)).setText(cUser.getString("userCityName"));
            ((EditText) findViewById(R.id.postalCodeText)).setText(cUser.getString("userPostalCode"));
            ((EditText) findViewById(R.id.countryNameText)).setText(cUser.getString("userCountryName"));

        } else {
            // show the signup or login screen
        }


        UserSingleton userSingleton = UserSingleton.getInstance();
        //int count_shared_vehicles = User.vehicle_list.size();
        mProductList_shares.clear();
        for (int i = 0; i < User.vehicle_list.size(); i++) {
            query_shares.whereEqualTo("PlateNumber", User.vehicle_list.get(i));
            final int finalI = i;
            List<ParseObject> rented_list = new ArrayList<ParseObject>();
            try {
                rented_list = query_shares.find();
            } catch(ParseException e) {

            }

            for (ParseObject p : rented_list) {
                if (!p.getBoolean("isViewedSharer")) {
                    mProductList_shares.add(new ShareDataStore(
                            p.getObjectId(),
                            p.getString("PlateNumber"),
                            p.getString("SourceAddress"),
                            p.getString("DestinationAddress"),
                            p.getDate("StartDate"),
                            p.getDate("EndDate"),
                            p.getDouble("BaseCost"),
                            p.getString("RenterEmail"),
                            p.getBoolean("TripDone")
                    ));
                }
            }

            if(finalI ==User.vehicle_list.size()-1) {
                //adapter_rents = new RentAdaptor(getApplicationContext(), mProductList_rents, 2);

                adapter_shares = new ShareAdaptor(getApplicationContext(), mProductList_shares, 2);
                lvProduct_shares.setAdapter(adapter_shares);
            }

            /*
            query_shares.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for (ParseObject p : list) {
                        if (!p.getBoolean("isViewed")) {
                            mProductList_shares.add(new RentDataStore(
                                    p.getObjectId(),
                                    p.getString("PlateNumber"),
                                    p.getString("SourceAddress"),
                                    p.getString("DestinationAddress"),
                                    p.getDate("StartDate"),
                                    p.getDate("EndDate")
                            ));
                        }
                    }

                    if(finalI ==User.vehicle_list.size()-1) {
                        //adapter_rents = new RentAdaptor(getApplicationContext(), mProductList_rents, 2);

                        adapter_shares = new RentAdaptor(getApplicationContext(), mProductList_shares, 2);
                        lvProduct_shares.setAdapter(adapter_shares);
                    }

                }
            });

            */
        }


        /*
        lvProduct_shares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( context);
                alertDialogBuilder.setTitle("Confirm status");
                alertDialogBuilder
                        .setMessage("Remove from history ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(getApplicationContext(), "Clicked product id =" + view.getTag(), Toast.LENGTH_SHORT).show();
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("VehicleTable");
                                query.getInBackground(view.getTag().toString(), new GetCallback<ParseObject>() {
                                    public void done(ParseObject vt, ParseException e) {
                                        if (e == null) {
                                            vt.put("isViewed", true);
                                            vt.saveInBackground();
                                        }
                                    }
                                });

                                VehicleDataStore item = mProductList_shares.get(position);
                                mProductList_shares.remove(item);

                                adapter_shares = new VehicleAdaptor(getApplicationContext(), mProductList_shares, 3);
                                lvProduct_shares.setAdapter(adapter_shares);


                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }


        });
        */

        lvProduct_rents = (ListView)findViewById(R.id.listView_rents);
        mProductList_rents = new ArrayList<>();

        //UserSingleton userSingleton = UserSingleton.getInstance();
        query_rents.whereEqualTo("RenterEmail", userSingleton.emailAddress);
        query_rents.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject p : list) {
                    if(!p.getBoolean("isViewed")) {
                        mProductList_rents.add(new RentDataStore(
                                p.getObjectId(),
                                p.getString("PlateNumber"),
                                p.getString("SourceAddress"),
                                p.getString("DestinationAddress"),
                                p.getDate("StartDate"),
                                p.getDate("EndDate")
                        ));
                    }
                }
                adapter_rents = new RentAdaptor(getApplicationContext(), mProductList_rents, 2);
                lvProduct_rents.setAdapter(adapter_rents);
            }
        });

        lvProduct_rents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("Confirm Status");

                alertDialogBuilder
                        .setMessage("Remove from history ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("RegisteredVehicles");
                                query.getInBackground(view.getTag().toString(), new GetCallback<ParseObject>() {
                                    public void done(ParseObject vt, ParseException e) {
                                        if (e == null) {
                                            vt.put("isViewed", true);
                                            vt.saveInBackground();
                                        }
                                    }
                                });

                                RentDataStore item = mProductList_rents.get(position);
                                mProductList_rents.remove(item);

                                adapter_rents = new RentAdaptor(getApplicationContext(), mProductList_rents, 3);
                                lvProduct_rents.setAdapter(adapter_rents);
                                dialog.cancel();
                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }



        });
    //});
/*
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                if ("Tab One".equals(tabId)) {
                    Toast.makeText(getApplicationContext(), "Tab One Pressed", Toast.LENGTH_SHORT).show();
                } else if ("Tab Two".equals(tabId)) {
                    Toast.makeText(getApplicationContext(), "Tab Two Pressed", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });
        */
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
            Intent screen = new Intent(MyAccountController.this,PostVehicleController.class);
            startActivity(screen);

        } else if(id==R.id.action_search) {
            Intent screen = new Intent(MyAccountController.this,FindVehicleController.class);
            startActivity(screen);
        } else if(id==R.id.action_manage) {
            Intent screen = new Intent(MyAccountController.this,MyAccountController.class);
            startActivity(screen);
        } else {
            SaveSharedPreference.setUserName(MyAccountController.this, "");
            SaveSharedPreference.setPassword(MyAccountController.this, "");
            Intent ownerlayout = new Intent(MyAccountController.this, LoginController.class);
            startActivity(ownerlayout);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateAccount(View view) {

        cUser.put("userContactNumber", ((EditText) findViewById(R.id.contactText)).getText().toString());
        cUser.put("userLastName",((EditText) findViewById(R.id.lastNameText)).getText().toString());
        cUser.put("userFirstName",((EditText) findViewById(R.id.firstNameText)).getText().toString());
        cUser.put("email",((EditText) findViewById(R.id.emailText)).getText().toString());
        cUser.put("userAddressLineOne",((EditText) findViewById(R.id.addressLineOneText)).getText().toString());
        cUser.put("userCityName",((EditText) findViewById(R.id.cityNameText)).getText().toString());
        cUser.put("userPostalCode",((EditText) findViewById(R.id.postalCodeText)).getText().toString());
        cUser.put("userCountryName", ((EditText) findViewById(R.id.countryNameText)).getText().toString());

        cUser.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), "Looks like there was an issue !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Account Updated Successfully  !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //refresh current location
    public void getCurrentLocationButtonClick(View v) {
        if (Build.VERSION.SDK_INT < 23) {
            String currentAddress = null;
            getLocation(currentAddress);
        }
        else {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }
    }

    //get current location
    private void getLocation(String address) {




        LocationServices mLocationServices = new LocationServices(this);
        mLocationServices.getLocation();
        if (mLocationServices.isLocationAvailable == false) {
            //try again
            Toast.makeText(getApplicationContext(), "Your location is not available, " +
                    "Enter address manually or Press refresh after enabling location.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Getting location co-ordinates
            double latitude = mLocationServices.getLatitude();
            double longitude = mLocationServices.getLongitude();
            address = mLocationServices.getLocationAddress();
            try {
                ((EditText) findViewById(R.id.addressLineOneText)).setText(mLocationServices.getLineOneAddress());
                ((EditText) findViewById(R.id.cityNameText)).setText(mLocationServices.getLocationCity());
                ((EditText) findViewById(R.id.countryNameText)).setText(mLocationServices.getLocationCountry());
                ((EditText) findViewById(R.id.postalCodeText)).setText(mLocationServices.getPostalCode());

            } catch (Exception e) {

            }
        }
        //close the gps
        mLocationServices.closeGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        switch(requestCode) {

            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    String currentAddress = null;
                    getLocation(currentAddress);
                }
                else {

                }
                break;
        }
    }
    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(ContextCompat.checkSelfPermission(this, perm)==
                PackageManager.PERMISSION_GRANTED);
    }



    public void trackUserButtonClick(View view) {

        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("LocationTracking");
        String pp = ((EditText) findViewById(R.id.emailIdToTrack)).getText().toString();
        query1.whereEqualTo("Email",pp );


        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {

                if (e == null) {
                    for (ParseObject p : list) {

                        Toast.makeText(getApplicationContext(), p.getString("address"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

}
