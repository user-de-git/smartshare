package com.mss.group3.smartshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CUserType extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cusertype);
    }

    public void postVehicle(View view) {
        Toast.makeText(CUserType.this, "Click", Toast.LENGTH_LONG).show();
        Intent screenthree = new Intent(CUserType.this,COwner.class);
        startActivity(screenthree);

    }
}
