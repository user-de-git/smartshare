package com.mss.group3.smartshare.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mss.group3.smartshare.*;
import com.mss.group3.smartshare.common.SaveSharedPreference;

public class UserTypeController extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.usertype);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void postVehicle(View view) {

        Intent ownerlayout = new Intent(UserTypeController.this, OwnerController.class);

        startActivity(ownerlayout);

    }

    public void findVehicleButtonClick(View view) {

        Intent entervehiclelayout = new Intent(UserTypeController.this, FindVehicleController.class);

        startActivity(entervehiclelayout);
    }



    public void manageAcountButtonClick(View view) {

        Intent entervehiclelayout = new Intent(UserTypeController.this, MyAccountController.class);

        startActivity(entervehiclelayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_post) {
            Intent screen = new Intent(UserTypeController.this,PostVehicleController.class);
            startActivity(screen);

        } else if(id==R.id.action_search) {
            Intent screen = new Intent(UserTypeController.this,FindVehicleController.class);
            startActivity(screen);
        } else if(id==R.id.action_manage) {
            Intent screen = new Intent(UserTypeController.this,MyAccountController.class);
            startActivity(screen);
        } else if(id==R.id.action_logout){

            SaveSharedPreference.setUserName(UserTypeController.this, "");
            SaveSharedPreference.setPassword(UserTypeController.this, "");
            Intent ownerlayout = new Intent(UserTypeController.this, LoginController.class);

            startActivity(ownerlayout);
            finish();
        } else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




}
