package com.mss.group3.smartshare.model;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mss.group3.smartshare.R;

import java.util.List;

public class VehicleAdaptor extends BaseAdapter {

    private Context mContext;
    private List<VehicleDataStore> mProductList;

    //Constructor

    public VehicleAdaptor(Context mContext, List<VehicleDataStore> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.listitemvehicle, null);
        TextView car_type = (TextView)v.findViewById(R.id.car_type);
        TextView car_capacity = (TextView)v.findViewById(R.id.car_capacity);
        TextView tv_date = (TextView)v.findViewById(R.id.tv_date);
        //Set text for TextView
        car_type.setText(mProductList.get(position).getVehicle_type());
        car_capacity.setText(String.valueOf(mProductList.get(position).getCapacity()));
        tv_date.setText(mProductList.get(position).getDate());

        //Save product id to tag
        v.setTag(mProductList.get(position).getId());

        return v;
    }
}
