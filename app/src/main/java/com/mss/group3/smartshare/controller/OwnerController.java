package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.model.UserSingleton;
import com.mss.group3.smartshare.model.VehicleAdaptor;
import com.mss.group3.smartshare.model.VehicleDataStore;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class OwnerController extends Activity {

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
        lvProduct = (ListView)findViewById(R.id.listView);
        final List<VehicleDataStore> Olist = new ArrayList<VehicleDataStore>();
        mProductList = new ArrayList<>();

        getData();


    }

    public void getData() {
        mProductList.clear();
        UserSingleton userSingleton = UserSingleton.getInstance();
        query.whereEqualTo("Owner_email", userSingleton.emailAddress);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject p : list) {
                    mProductList.add(new VehicleDataStore(p.getObjectId(), p.getString("Vehicle_type"), p.getInt("Capacity"), p.getDate("ToDate")  )  );
                }

                adapter = new VehicleAdaptor(getApplicationContext(), mProductList);
                lvProduct.setAdapter(adapter);

                lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Do something
                        //Ex: display msg with product id get from view.getTag
                        Intent myIntent = new Intent(OwnerController.this, UpdateVehicleController.class);
                        myIntent.putExtra(VEHICLE_ID,(String)view.getTag());
                        startActivity(myIntent);
                        //Toast.makeText(getApplicationContext(), "Clicked product id =" + view.getTag(), Toast.LENGTH_SHORT).show();
                    }
                });

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
        Intent screenfour = new Intent(OwnerController.this,PostVehicleController.class);
        startActivity(screenfour);
    }
}
