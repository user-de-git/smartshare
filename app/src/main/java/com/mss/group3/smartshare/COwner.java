package com.mss.group3.smartshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class COwner extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cowner);
        final List<CList> Olist = new ArrayList<CList>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("VehicleTable");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (ParseObject p : list) {
                    //CList n = new CList(p.getInt("age"),p.getString("name"),p.getString("city"));
                    Olist.add(new CList(p.getInt("age"),p.getString("name"),p.getString("city")));
                   // LOG.d("--", (String) p.get("foo") + p.getCreatedAt());
                }

                ArrayAdapter<CList> CArrayAdaptor = new ArrayAdapter<CList>(COwner.this,android.R.layout.simple_list_item_1,Olist);
                ListView lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(CArrayAdaptor);
            }
        });
    }

    public void postVehicle(View view) {

        Intent screenfour = new Intent(COwner.this,CVehicleRegistration.class);
        startActivity(screenfour);

    }
}
