package com.mss.group3.smartshare.controller;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.common.InputValidation;
import com.mss.group3.smartshare.common.SaveSharedPreference;
import com.mss.group3.smartshare.common.User;
import com.mss.group3.smartshare.model.FindVehiclelistSingleton;
import com.mss.group3.smartshare.model.RentAdaptor;
import com.mss.group3.smartshare.model.RentDataStore;
import com.mss.group3.smartshare.model.ShareAdaptor;
import com.mss.group3.smartshare.model.ShareDataStore;
import com.mss.group3.smartshare.model.UserSingleton;
import com.mss.group3.smartshare.utility.LocationServices;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import static android.content.DialogInterface.*;

public class MyAccountController extends AppCompatActivity {
    ParseUser cUser;
    String emailAddress;
    final Context context = this;
    private ListView lvProduct_shares,lvProduct_rents;
    private ShareAdaptor adapter_shares;
    private SmsManager smsManager = SmsManager.getDefault();
    private String selection_dropdown = "All";

    private ShareAdaptor adapter_shares_weekly;
    private ShareAdaptor adapter_shares_monthly;
    //private ShareAdaptor adapter_shares_all;

    private RentAdaptor adapter_rents;
    //private List<ShareDataStore> mProductList_shares;

    CopyOnWriteArrayList<ShareDataStore> mProductList_shares = new CopyOnWriteArrayList<ShareDataStore>();

    CopyOnWriteArrayList<ShareDataStore> mProductList_shares_weekly = new CopyOnWriteArrayList<ShareDataStore>();
    CopyOnWriteArrayList<ShareDataStore> mProductList_shares_monthly = new CopyOnWriteArrayList<ShareDataStore>();
    //private List<ShareDataStore> mProductList_shares_all;

    private List<RentDataStore> mProductList_rents;
    //ParseQuery<ParseObject> query_shares = new ParseQuery<ParseObject>("RegisteredVehicles");
    ParseQuery<ParseObject> query_rents  = new ParseQuery<ParseObject>("RegisteredVehicles");
    static TabHost host;
    String EnddateString = null;
    Double base_rev_weekly = 0.0;
    Double base_rev_monthly =0.0;
    Double total_rev = 0.0;
    static Boolean first = false;

    TextView tv_get_expected_revenue;

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
        //mProductList_shares = new ArrayList<>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        tv_get_expected_revenue = (TextView) findViewById(R.id.get_expected_revenue);

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
        cUser.getSessionToken();
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

        Spinner spinner = (Spinner) findViewById(R.id.spinner_shares);

        List<String> categories = new ArrayList<String>();
        categories.add("All");
        categories.add("Weekly");
        categories.add("Monthly");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // pv.setVehicle_type((String) parent.getItemAtPosition(pos));
                String selection = (String) parent.getItemAtPosition(pos);
                //Toast.makeText(getApplicationContext(), (String) parent.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                if (first) {
                    String rev = null;
                    if (selection.equals("All")) {
                        selection_dropdown = "All";
                        rev = new DecimalFormat("0.##").format(total_rev);

                        tv_get_expected_revenue.setText(rev+" CAD");
                        adapter_shares = new ShareAdaptor(getApplicationContext(), mProductList_shares, 2);
                        lvProduct_shares.setAdapter(adapter_shares);
                    } else if (selection.equals("Weekly")) {
                        selection_dropdown = "Weekly";
                        rev = new DecimalFormat("0.##").format(base_rev_weekly);

                        tv_get_expected_revenue.setText(rev+" CAD");
                        adapter_shares_weekly = new ShareAdaptor(getApplicationContext(), mProductList_shares_weekly, 2);
                        lvProduct_shares.setAdapter(adapter_shares_weekly);
                    } else {
                        selection_dropdown = "Monthly";
                        rev = new DecimalFormat("0.##").format(base_rev_monthly);

                        tv_get_expected_revenue.setText(rev+" CAD");
                        adapter_shares_monthly = new ShareAdaptor(getApplicationContext(), mProductList_shares_monthly, 2);
                        lvProduct_shares.setAdapter(adapter_shares_monthly);
                    }
                }
                first = true;


            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        final UserSingleton userSingleton = UserSingleton.getInstance();
        //int count_shared_vehicles = User.vehicle_list.size();
        mProductList_shares.clear();
        //= new ParseQuery<ParseObject>("RegisteredVehicles");
        int size = User.vehicle_list.size();
        ParseQuery[] query_shares = new ParseQuery[size];
        for (int i = 0; i < User.vehicle_list.size(); i++) {
            query_shares[i] = new ParseQuery<ParseObject>("RegisteredVehicles");
            query_shares[i].whereEqualTo("PlateNumber", User.vehicle_list.get(i));
            final int finalI = i;
            /*
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
            */

            query_shares[i].findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    for (ParseObject p : list) {
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

                        if( isDateInCurrentWeek(p.getDate("StartDate")) ) {
                            base_rev_weekly+=p.getDouble("BaseCost");
                            if(!p.getBoolean("isViewedSharer")) {
                                mProductList_shares_weekly.add(new ShareDataStore(
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



                        if( isDateInCurrentMonth(p.getDate("StartDate")) ) {
                            base_rev_monthly+=p.getDouble("BaseCost");
                            if(!p.getBoolean("isViewedSharer")) {
                                mProductList_shares_monthly.add(new ShareDataStore(
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

                        total_rev+=p.getDouble("BaseCost");
                    }

                    if(finalI ==User.vehicle_list.size()-1) {
                        adapter_shares = new ShareAdaptor(getApplicationContext(), mProductList_shares, 2);
                        lvProduct_shares.setAdapter(adapter_shares);
                        String rev = new DecimalFormat("0.##").format(total_rev);
                        tv_get_expected_revenue.setText(rev+" CAD");
                    }

                }
            });
        }



        lvProduct_shares.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( context);
                alertDialogBuilder.setTitle("Confirm status");
                alertDialogBuilder
                        .setMessage("Remove from history ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(getApplicationContext(), "Clicked product id =" + view.getTag(), Toast.LENGTH_SHORT).show();
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("RegisteredVehicles");
                                query.getInBackground(view.getTag().toString(), new GetCallback<ParseObject>() {
                                    public void done(ParseObject vt, ParseException e) {
                                        if (e == null) {
                                            vt.put("isViewedSharer", true);
                                            vt.saveInBackground();
                                        }
                                    }
                                });
                                String Obj_ID = null;
                                if(selection_dropdown.equals("All")) {
                                    Obj_ID = mProductList_shares.get(position).getId();
                                    ShareDataStore item = mProductList_shares.get(position);
                                    mProductList_shares.remove(item);

                                    adapter_shares = new ShareAdaptor(getApplicationContext(), mProductList_shares, 3);
                                    lvProduct_shares.setAdapter(adapter_shares);

                                    removeItem_mProductList_shares_weekly(Obj_ID);
                                    removeItem_mProductList_shares_monthly(Obj_ID);
                                } else if(selection_dropdown.equals("Weekly")) {
                                    Obj_ID = mProductList_shares_weekly.get(position).getId();
                                    ShareDataStore item = mProductList_shares_weekly.get(position);
                                    mProductList_shares_weekly.remove(item);

                                    adapter_shares_weekly = new ShareAdaptor(getApplicationContext(), mProductList_shares_weekly, 3);
                                    lvProduct_shares.setAdapter(adapter_shares_weekly);

                                    removeItem_mProductList_shares(Obj_ID);
                                    removeItem_mProductList_shares_monthly(Obj_ID);

                                } else {

                                    Obj_ID = mProductList_shares_monthly.get(position).getId();
                                    ShareDataStore item = mProductList_shares_monthly.get(position);
                                    mProductList_shares_monthly.remove(item);

                                    adapter_shares_monthly = new ShareAdaptor(getApplicationContext(), mProductList_shares_monthly, 3);
                                    lvProduct_shares.setAdapter(adapter_shares_monthly);

                                    removeItem_mProductList_shares_weekly(Obj_ID);
                                    removeItem_mProductList_shares(Obj_ID);

                                }


                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new OnClickListener() {
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


        lvProduct_rents = (ListView)findViewById(R.id.listView_rents);
        mProductList_rents = new ArrayList<>();

        //UserSingleton userSingleton = UserSingleton.getInstance();
        query_rents.whereEqualTo("RenterEmail", userSingleton.emailAddress);
        query_rents.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject p : list) {
                    if(!p.getBoolean("TripDone")) {
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

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.return_dialog);
                final Calendar calendar = Calendar.getInstance();
                Button dialogButtonreturn = (Button) dialog.findViewById(R.id.button_return);
                Button dialogButtoncancel = (Button) dialog.findViewById(R.id.button_cancel);
                ImageView dialogButtonFeedback = (ImageView) dialog.findViewById(R.id.img_right);

                ImageView iv = (ImageView) dialog.findViewById(R.id.img_left);

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialogButtoncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(EnddateString==null) {
                            Toast.makeText(getApplicationContext(), "Date & Time missing", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("RegisteredVehicles");
                        query.getInBackground(view.getTag().toString(), new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    Date start = object.getDate("StartDate");
                                    Date _cancel = InputValidation.DateSetter(EnddateString);
                                    Log.d("start",start.toString());
                                    Log.d("_cancel",_cancel.toString());
                                    if(_cancel.before(start)) {

                                        try {


                                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("VehicleTable");
                                            query2.whereEqualTo("Plate_number", mProductList_rents.get(position).getPlateNumber());
                                            try {
                                                emailAddress = query2.getFirst().getString("Owner_email");
                                                sendSMS(emailAddress,mProductList_rents.get(position).getPlateNumber()+ "Vehicle booking is cancelled from " +
                                                        mProductList_rents.get(position).getStartDateTime() +"   To " +mProductList_rents.get(position).getEndDateTime());
                                            } catch (ParseException ex) {
                                                ex.printStackTrace();
                                            }


                                            object.delete();
                                            RentDataStore item = mProductList_rents.get(position);
                                            mProductList_rents.remove(item);

                                            adapter_rents = new RentAdaptor(getApplicationContext(), mProductList_rents, 3);
                                            lvProduct_rents.setAdapter(adapter_rents);
                                        } catch (ParseException ec) {
                                            Log.d("exception",ec.toString());
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Transaction is process, cancellation aborted", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });

                dialogButtonreturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(EnddateString==null) {
                            Toast.makeText(getApplicationContext(), "Return Date & Time missing", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final ParseQuery<ParseObject> query = ParseQuery.getQuery("RegisteredVehicles");
                        query.getInBackground(view.getTag().toString(), new GetCallback<ParseObject>() {
                            public void done(ParseObject vt, ParseException e) {
                                if (e == null) {
                                    Date end = vt.getDate("EndDate");
                                    Date start = vt.getDate("StartDate");
                                    Double baseCost = vt.getDouble("BaseCost");
                                    String plate_number = vt.getString("PlateNumber");
                                    Boolean returned= false;
                                    Double Total_cost = 0.0;

                                    Date _return = InputValidation.DateSetter(EnddateString);

                                    if( _return.after(start) && _return.before(end) ) {
                                        vt.put("TripDone", true);
                                        Total_cost = baseCost;
                                        Toast.makeText(getApplicationContext(), "Cost :"+Total_cost.toString()+" $ CAD", Toast.LENGTH_SHORT).show();

                                        vt.saveInBackground();
                                        returned = true;
                                    } else if(_return.after(end)) {
                                        long diff = _return.getTime() - end.getTime();
                                        long diffHours = diff; /// (60 * 60 * 1000);
                                        Integer overDueRate = -1;
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("VehicleTable");
                                        query.whereEqualTo("Plate_number", vt.getString("PlateNumber"));
                                        try {
                                            overDueRate = query.getFirst().getInt("Overdue_hr_rate");
                                        } catch (ParseException ex) {
                                            ex.printStackTrace();
                                        }

                                        int diffInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(diff);

                                        Double hrs =  diffInSeconds*1.0 / (3600.00);
                                        Double additional_cost = overDueRate * hrs;

                                        Total_cost = additional_cost + baseCost;

                                        // Log.d("hrs",hrs.toString());
                                        // Log.d("Total_cost", Total_cost.toString());

                                        Toast.makeText(getApplicationContext(), "Cost :"+Total_cost.toString()+" $ CAD", Toast.LENGTH_SHORT).show();


                                        vt.put("TripDone", true);
                                        vt.put("TotalCost", Total_cost);
                                        vt.saveInBackground();
                                        returned = true;
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Return date & time Invalid", Toast.LENGTH_SHORT).show();
                                        return;
                                    }


                                    if(returned) {

                                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("VehicleTable");
                                        query2.whereEqualTo("Plate_number", mProductList_rents.get(position).getPlateNumber());
                                        try {
                                            emailAddress = query2.getFirst().getString("Owner_email");
                                            sendSMS(emailAddress, "Vehicle is returned " + mProductList_rents.get(position).getPlateNumber());
                                        } catch (ParseException ex) {
                                            ex.printStackTrace();
                                        }

                                        RentDataStore item = mProductList_rents.get(position);
                                        mProductList_rents.remove(item);



                                        adapter_rents = new RentAdaptor(getApplicationContext(), mProductList_rents, 3);
                                        lvProduct_rents.setAdapter(adapter_rents);

                                        ParseQuery<ParseObject> query_renter = ParseQuery.getQuery("UserCredit");
                                        query_renter.whereEqualTo("user_email", userSingleton.emailAddress);

                                        final Double finalTotal_cost = Total_cost;
                                        query_renter.getFirstInBackground(
                                                new GetCallback<ParseObject>() {
                                                    public void done( ParseObject object, ParseException e) {
                                                        if(e==null) {
                                                            Double final_credit = object.getDouble("user_credit") - finalTotal_cost;
                                                            Log.d("Current Credit", final_credit.toString());
                                                            object.put("user_credit", final_credit);
                                                            object.saveInBackground();
                                                        } else {
                                                            Log.d("ERROR -> ", "Error finding user");

                                                        }
                                                    }
                                                }
                                        );


//
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("VehicleTable");
                                        query.whereEqualTo("Plate_number", plate_number);
                                        String Owner_email= null;
                                        try {
                                            Owner_email = query.getFirst().getString("Owner_email");
                                            Log.d("Owner_email", Owner_email);

                                            ParseQuery<ParseObject> query_sharer = ParseQuery.getQuery("UserCredit");
                                            query_sharer.whereEqualTo("user_email", Owner_email);

                                            query_sharer.getFirstInBackground(
                                                    new GetCallback<ParseObject>() {
                                                        public void done(ParseObject object, ParseException e) {
                                                            if (e == null) {
                                                                Double final_credit = object.getDouble("user_credit") + finalTotal_cost;
                                                                Log.d("Current Credit", final_credit.toString());
                                                                object.put("user_credit", final_credit);
                                                                object.saveInBackground();
                                                            } else {
                                                                Log.d("ERROR -> ", "Error finding user");
                                                            }
                                                        }
                                                    }
                                            );

                                        } catch (com.parse.ParseException ec) {
                                            ec.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });


                /////feedback function

                dialogButtonFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RentDataStore item = mProductList_rents.get(position);
                        String plateNumber = item.getPlateNumber();


                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("VehicleTable");
                        query2.whereEqualTo("Plate_number", plateNumber);
                try {
                            emailAddress = query2.getFirst().getString("Owner_email");
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }

                        dialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Feedback Now!");

// Set up the input
                        final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        builder.setView(input);

// Set up the buttons
                        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String message = input.getText().toString();

                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.setType("plain/text");
                                email.putExtra(Intent.EXTRA_EMAIL, new String[] { emailAddress });


                                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback from SmartShare");
                                email.putExtra(Intent.EXTRA_TEXT, message);

                                //need this to prompts email client only
                                email.setType("message/rfc822");

                                startActivity(Intent.createChooser(email, "Choose a client"));


                                dialog.dismiss();

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();

                    }


                });

////////////feedback end



                Button button_setReturnDateTime = (Button) dialog.findViewById(R.id.button_setReturnDateTime);
                final EditText get_ReturnDateTime = (EditText) dialog.findViewById(R.id.get_ReturnDateTime);

                button_setReturnDateTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override public void onDateSet(DatePicker view,
                                                                    int year, int month, int day) {
                                        EnddateString = (month+1) +"/" + day + "/" + year;
                                        new TimePickerDialog(context,
                                                new TimePickerDialog.OnTimeSetListener() {
                                                    @Override public void onTimeSet(TimePicker view,
                                                                                    int hour, int min) {
                                                        EnddateString+=" "+hour + ":"+min;
                                                        get_ReturnDateTime.setText(EnddateString);
                                                    }
                                                }, calendar.get(Calendar.HOUR_OF_DAY),
                                                calendar.get(Calendar.MINUTE),
                                                android.text.format.DateFormat.is24HourFormat(context)).show();

                                    }
                                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });



                dialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public void removeItem_mProductList_shares(String OBj_ID) {
        for(int i=0;i<mProductList_shares.size();i++) {
            ShareDataStore item = mProductList_shares.get(i);
            if(OBj_ID.equals(mProductList_shares.get(i).getId())) {
                mProductList_shares.remove(item);
                return;
            }
        }
    }

    public void removeItem_mProductList_shares_weekly(String OBj_ID) {
        for(int i=0;i<mProductList_shares_weekly.size();i++) {
            ShareDataStore item = mProductList_shares_weekly.get(i);
            if(OBj_ID.equals(mProductList_shares_weekly.get(i).getId())) {
                mProductList_shares_weekly.remove(item);
                return;
            }
        }
    }

    public void removeItem_mProductList_shares_monthly(String OBj_ID) {
        for(int i=0;i<mProductList_shares_monthly.size();i++) {
            ShareDataStore item = mProductList_shares_monthly.get(i);
            if(OBj_ID.equals(mProductList_shares_monthly.get(i).getId())) {
                mProductList_shares_monthly.remove(item);
                return;
            }
        }
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

    public boolean isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }

    public boolean isDateInCurrentMonth(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int month = currentCalendar.get(Calendar.MONTH);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetmonth = targetCalendar.get(Calendar.MONTH);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return month== targetmonth && year == targetYear;
    }

    public void sendSMS(String email, final String Message) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContains("username", email);
        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    try {
                        for (ParseUser p : objects) {
                            smsManager.sendTextMessage("+1" + p.getString("userContactNumber"), null,
                                    Message +"--  by " +UserSingleton.getInstance().emailAddress , null, null);
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


}