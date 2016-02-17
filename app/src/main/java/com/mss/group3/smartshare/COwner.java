package com.mss.group3.smartshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class COwner extends Activity {

    private ListView lvProduct;
    private VehicleAdaptor adapter;
    private List<VehicleDataStore> mProductList;
    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("VehicleTable");
    static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cowner);
        lvProduct = (ListView)findViewById(R.id.listView);
        final List<VehicleDataStore> Olist = new ArrayList<VehicleDataStore>();
        mProductList = new ArrayList<>();
//
//        Parse.enableLocalDatastore(this);
//        Parse.initialize(this);
//        ParseUser.enableAutomaticUser();
//        ParseACL defaultACL = new ParseACL();
//        ParseACL.setDefaultACL(defaultACL, true);
        getData();


    }

    public void getData() {
        mProductList.clear();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject p : list) {
                    mProductList.add(new VehicleDataStore(p.getObjectId(), p.getString("Vehicle_type"), p.getInt("Capacity"), p.getString("Date")));
                }

                adapter = new VehicleAdaptor(getApplicationContext(), mProductList);
                lvProduct.setAdapter(adapter);
            }
        });
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

        Intent screenfour = new Intent(COwner.this,CVehicleRegistration.class);
        startActivity(screenfour);

    }
}
