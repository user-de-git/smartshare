package com.mss.group3.smartshare;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;

import com.mss.group3.smartshare.controller.LoginController;
import com.mss.group3.smartshare.controller.OwnerController;
import com.mss.group3.smartshare.controller.PostVehicleController;
import com.mss.group3.smartshare.controller.UserTypeController;
import com.mss.group3.smartshare.model.VehicleDataStore;
import com.parse.Parse;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.text.TextUtils.isEmpty;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class DControllerTest  {



    @Rule
    public ActivityTestRule<OwnerController> lController =
            new ActivityTestRule<>(OwnerController.class);

    @Test
    public void Post_Vehicle() {

        //onView(withText("Share Vehicle")).
        //        perform(click());

        try {
            Thread.sleep(2750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.button)).check(matches(isDisplayed()));


        for(int i=0;i<2;i++) {
            onView(withId(R.id.button)).perform(click());

            try {
                Thread.sleep(2750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            onView(withId(R.id.et_overduecharges)).
                    perform(typeText("100"));

            onView(withId(R.id.et_platenumber)).
                    perform(typeText("tcar"+i));

            onView(withId(R.id.et_pricekm)).
                    perform(typeText("2"));

            onView(withId(R.id.et_vehiclerange)).
                    perform(typeText("100"));

            onView(withId(R.id.get_StartDateTime)).
                    perform(typeText("5/20/2016 15:15"));

            onView(withId(R.id.get_EndDateTime)).
                    perform(typeText("5/25/2016 15:15"));

            onView(withId(R.id.button_addModifyVehicle)).
                    perform(scrollTo());

            onView(withId(R.id.et_address)).
                    perform(typeText("6559 131 st"));

            onView(withId(R.id.et_city)).
                    perform(typeText("Surrey"));

            onView(withId(R.id.et_postalCode)).
                    perform(typeText("v3w 8g3"));

            onView(withId(R.id.et_province)).
                    perform(typeText("BC"));

            onView(withId(R.id.button_addModifyVehicle)).
                    perform(click());

            try {
                Thread.sleep(4750);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        onData(is(instanceOf(VehicleDataStore.class)))
                         .atPosition(0)// We are using the position so don't need to specify a data matcher
                        .check(matches(isDisplayed()));

        onData(is(instanceOf(VehicleDataStore.class)))
                .atPosition(1)// We are using the position so don't need to specify a data matcher
                .check(matches(isDisplayed()));

    /*
        .inAdapterView(withId(R.id.listView)) // Specify the explicit id of the ListView
                .atPosition(1) // Explicitly specify the adapter item to use
                .perform(click()); // Standard ViewAction

    */



    }



}
