package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.common.SaveSharedPreference;
import com.mss.group3.smartshare.model.UserSingleton;
import com.mss.group3.smartshare.model.VehicleAdaptor;
import com.mss.group3.smartshare.model.VehicleDataStore;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OwnerController extends AppCompatActivity {

    public static final String VEHICLE_ID = "VehicleId";
    private ListView lvProduct;
    private VehicleAdaptor adapter;
    private List<VehicleDataStore> mProductList;
    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("VehicleTable");
    static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        lvProduct = (ListView)findViewById(R.id.listView);
        mProductList = new ArrayList<>();

        if(counter==0)  getData();
    }

    public void getData() {
        query = new ParseQuery<ParseObject>("VehicleTable");
        mProductList.clear();
        UserSingleton userSingleton = UserSingleton.getInstance();
        query.whereEqualTo("Owner_email", userSingleton.emailAddress);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject p : list) {
                    mProductList.add(new VehicleDataStore(
                            p.getObjectId(),
                            p.getString("Vehicle_type"),
                            p.getInt("Capacity"),
                            p.getDate("FromDate"),
                            p.getDate("ToDate"),
                            p.getDouble("Price_km") ));
                }

                adapter = new VehicleAdaptor(getApplicationContext(), mProductList,1);
                lvProduct.setAdapter(adapter);

                lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent myIntent = new Intent(OwnerController.this, UpdateVehicleController.class);
                        String VID = (String) view.getTag();
                        myIntent.putExtra(VEHICLE_ID, VID);
                        startActivity(myIntent);
                    }
                });
                query = null;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_post) {
            Intent screen = new Intent(OwnerController.this,PostVehicleController.class);
            startActivity(screen);

        } else if(id==R.id.action_search) {
            Intent screen = new Intent(OwnerController.this,FindVehicleController.class);
            startActivity(screen);
        } else if(id==R.id.action_manage) {
            Intent screen = new Intent(OwnerController.this,MyAccountController.class);
            startActivity(screen);
        } else {
            SaveSharedPreference.setUserName(OwnerController.this, "");
            SaveSharedPreference.setPassword(OwnerController.this, "");
            Intent ownerlayout = new Intent(OwnerController.this, LoginController.class);
            startActivity(ownerlayout);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        counter++;
        super.onResume();
        if(counter>1) {
            getData();
        }
    }

    public void postVehicle(View view) {
        Intent screenfour = new Intent(OwnerController.this,PostVehicleController.class);
        startActivity(screenfour);
    }
}
