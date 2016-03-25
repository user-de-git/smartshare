package com.mss.group3.smartshare.model;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mss.group3.smartshare.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class VehicleAdaptor extends BaseAdapter {

    private Context mContext;
    private List<VehicleDataStore> mProductList;
    private Integer list_item_back;

    //Constructor

    public VehicleAdaptor(Context mContext, List<VehicleDataStore> mProductList, Integer rID) {
        this.mContext = mContext;
        this.mProductList = mProductList;
        this.list_item_back = rID;
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

    public String DateSetter(String DateTime)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
        Date result = null;
        try {
            result = df.parse(DateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(result);


        return  simpleDateFormat.format(result);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.listitemvehicle, null);
        Drawable  drawable = null;//new Image that was added to the res folder

        switch(list_item_back) {
            case 1:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.list_item_back_1);

                break;
            case 2:

                break;
            case 3:
                drawable = ContextCompat.getDrawable(mContext, R.drawable.list_item_back_3);
                break;

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(drawable);
        }
        TextView car_type = (TextView)v.findViewById(R.id.car_type);
        TextView car_capacity = (TextView)v.findViewById(R.id.car_capacity);
        TextView tv_dateFrom = (TextView)v.findViewById(R.id.tv_dateFrom);
        TextView tv_dateTo = (TextView)v.findViewById(R.id.tv_dateTo);
        TextView tv_price = (TextView)v.findViewById(R.id.tv_price);


        //Set text for TextView
        car_type.setText(mProductList.get(position).getVehicle_type());
        car_capacity.setText(String.valueOf(mProductList.get(position).getCapacity())+ " Seats");
        String from = DateSetter(mProductList.get(position).getDateFrom().toString());
        String to = DateSetter(mProductList.get(position).getDateTo().toString());
        DecimalFormat format = new DecimalFormat("0.##");
        Double price = mProductList.get(position).getPrice();
        format.format(price);


        tv_dateFrom.setText("Start "+from);
        tv_dateTo.setText(  "End   "+to);
        tv_price.setText("$ "+String.valueOf(format.format(price))+" /km");


        //Save product id to tag
        v.setTag(mProductList.get(position).getId());

        return v;
    }
}
