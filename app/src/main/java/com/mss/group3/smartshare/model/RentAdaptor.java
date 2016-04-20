package com.mss.group3.smartshare.model;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mss.group3.smartshare.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class RentAdaptor extends BaseAdapter {

    private Context mContext;
    private List<RentDataStore> mProductList;
    static String vehicle_type = "";
    static String arriving_from= "";

    private Integer list_item_back;

    //Constructor

    public RentAdaptor(Context mContext, List<RentDataStore> mProductList, Integer rID) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View v = View.inflate(mContext, R.layout.listitemvehicle_rents, null);
        Drawable  drawable = null;//new Image that was added to the res folder

        drawable = ContextCompat.getDrawable(mContext, R.drawable.list_item_back_3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(drawable);
        }

        TextView tv_from = (TextView)v.findViewById(R.id.tv_dateFrom);

        TextView tv_to = (TextView)v.findViewById(R.id.tv_dateTo);

        TextView tv_Source = (TextView)v.findViewById(R.id.tv_dateSource);

        TextView tv_destination = (TextView)v.findViewById(R.id.tv_dateDestination);

        final TextView car_type = (TextView)v.findViewById(R.id.car_type);

        TextView tv_price = (TextView)v.findViewById(R.id.tv_price);

        TextView tv_arriving_from = (TextView)v.findViewById(R.id.arriving_from);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("VehicleTable");
        query.whereEqualTo("Plate_number", mProductList.get(position).getPlateNumber());
        try {
            vehicle_type = query.getFirst().getString("Vehicle_type");
            arriving_from = query.getFirst().getString("Address")+", "+
                            query.getFirst().getString("City");
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        car_type.setText(vehicle_type);
        String from = DateSetter(mProductList.get(position).getStartDateTime().toString());
        String to = DateSetter(mProductList.get(position).getEndDateTime().toString());

        String startAddress = mProductList.get(position).getStartAddress();
        String endAddress = mProductList.get(position).getEndAddress();
        //DecimalFormat format = new DecimalFormat("0.##");

        tv_from.setText(from);
        tv_to.setText(to);

        tv_Source.setText(startAddress );
        tv_destination.setText(endAddress );
        tv_arriving_from.setText(arriving_from);



        //Save product id to tag
        v.setTag(mProductList.get(position).getId());

        return v;


    }
}
