package com.mss.group3.smartshare.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mss.group3.smartshare.R;

import java.util.List;

/**
 * Created by inder on 2016-03-03.
 */
public class FindVehicleContainer extends BaseAdapter {

    private Context context;
    private List<VehicleWithRangeList> vehicleList;

    //Constructor

    public FindVehicleContainer(Context context, List<VehicleWithRangeList> vehicleList) {
        this.context = context;
        this.vehicleList = vehicleList;
    }

    @Override
    public int getCount() {
        return vehicleList.size();
    }

    @Override
    public Object getItem(int position) {
        return vehicleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.listitemfindvehicle, null);
        TextView cartype = (TextView)v.findViewById(R.id.car_type);
        TextView carcapacity = (TextView)v.findViewById(R.id.car_capacity);
        TextView fromdate = (TextView)v.findViewById(R.id.fromDate);
        TextView toDate  =  (TextView)v.findViewById(R.id.toDate);
        TextView plateNumber = (TextView)v.findViewById(R.id.plateNumber);

        //Set text for TextView
        cartype.setText(vehicleList.get(position).vehicle_type);
        carcapacity.setText(String.valueOf(vehicleList.get(position).capacity));
        fromdate.setText(vehicleList.get(position).fromDate.toString());
        toDate.setText(vehicleList.get(position).toDate.toString());
        plateNumber.setText(vehicleList.get(position).plateNumber.toString());

        //Save product id to tag
        v.setTag(vehicleList.get(position).id);

        return v;
    }
}
