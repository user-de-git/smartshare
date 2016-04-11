package com.mss.group3.smartshare.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mss.group3.smartshare.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bhupinder on 4/11/2016.
 */
public class ShareAdaptor extends BaseAdapter {

    private Context mContext;
    private List<ShareDataStore> mProductList;
    static String vehicle_type = "";
    static String userName = "";
    static String _status = null;
    private Integer list_item_back;

    //Constructor

    public ShareAdaptor(Context mContext, List<ShareDataStore> mProductList, Integer rID) {
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
        final View v = View.inflate(mContext, R.layout.listitemvehicle_shares, null);
        Drawable drawable = null;//new Image that was added to the res folder

        drawable = ContextCompat.getDrawable(mContext, R.drawable.list_item_back_3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(drawable);
        }

        final TextView car_type = (TextView)v.findViewById(R.id.car_type);
        final TextView tv_dateFrom = (TextView)v.findViewById(R.id.tv_dateFrom);
        final TextView tv_dateTo = (TextView)v.findViewById(R.id.tv_dateTo);
        final TextView tv_dateSource = (TextView)v.findViewById(R.id.tv_dateSource);
        final TextView tv_dateDestination = (TextView)v.findViewById(R.id.tv_dateDestination);

        //final TextView tv_dateSourceDestination = (TextView)v.findViewById(R.id.tv_dateSourceDestination);
        TextView tv_base_cost = (TextView)v.findViewById(R.id.base_cost);
        TextView userInfo = (TextView)v.findViewById(R.id.tv_renterInfo);
        TextView tripStatus = (TextView)v.findViewById(R.id.tv_tripStatus);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("VehicleTable");
        query.whereEqualTo("Plate_number", mProductList.get(position).getPlateNumber());
        try {
            vehicle_type = query.getFirst().getString("Vehicle_type");
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseUser> que = ParseUser.getQuery();
        que.whereEqualTo("email", mProductList.get(position).getRenterEmail());
        try {
            userName = que.getFirst().getString("userFirstName");
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        if(mProductList.get(position).getTripStatus()) {
            _status = "Completed";
        } else {
            _status = "In Progress";
        }

        car_type.setText(vehicle_type);
        String from = DateSetter(mProductList.get(position).getStartDateTime().toString());
        String to = DateSetter(mProductList.get(position).getEndDateTime().toString());

        String startAddress = mProductList.get(position).getStartAddress();
        String endAddress = mProductList.get(position).getEndAddress();

        tv_base_cost.setText("Amt : "+mProductList.get(position).getBaseCost().toString()+" $");
        tv_dateFrom.setText(from);
        tv_dateTo.setText(to);
        tv_dateSource.setText(startAddress);
        tv_dateDestination.setText(endAddress );
        userInfo.setText(userName+" <"+mProductList.get(position).getRenterEmail()+">");
        tripStatus.setText(_status);



        //Save product id to tag
        v.setTag(mProductList.get(position).getId());

        return v;


    }
}
