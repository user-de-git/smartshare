package com.mss.group3.smartshare;

import android.content.Intent;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mss.group3.smartshare.controller.FindVehicleController;
import com.mss.group3.smartshare.controller.OwnerController;
import com.mss.group3.smartshare.model.FindVehiclelistSingleton;
import com.mss.group3.smartshare.model.VehicleDataStore;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

/**
 * Created by Inder on 4/22/2016.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class HControllerTest {

    @Rule
    public ActivityTestRule<FindVehicleController> lController =
            new ActivityTestRule<>(FindVehicleController.class);

    @Test
    public void Search_Vehicle() {

        try {
            Thread.sleep(2550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.departureAddressLineOneText)).
                perform(typeText("6425 windsor St"),closeSoftKeyboard());


        onView(withId(R.id.departureAddressCityNameText)).
                perform(typeText("vancouver"),closeSoftKeyboard());

        onView(withId(R.id.departureAddressCountryNameText)).
                perform(typeText("Canada"),closeSoftKeyboard());

        onView(withId(R.id.departureAddressPostalCodeText)).
                perform(typeText("v5w 3j4"), closeSoftKeyboard());


        onView(withId(R.id.arrivalAddressLineOneText)).
                perform(typeText("33463 11th Ave"),closeSoftKeyboard());

        onView(withId(R.id.arrivalAddressCityNameText)).
                perform(typeText("Mission"),closeSoftKeyboard());

        onView(withId(R.id.arrivalAddressCountryNameText)).
                perform(typeText("Canada"), closeSoftKeyboard());

        onView(withId(R.id.arrivalAddressPostalCodeText)).
                perform(typeText("V2V 6Z5"), closeSoftKeyboard());

                try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.getFromDate)).
                perform(typeText("4/21/2016 15:15"), closeSoftKeyboard());

        onView(withId(R.id.getFromDateButton)).
                perform(scrollTo());

        onView(withId(R.id.getFromDateButton)).
                perform(click());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("OK")).perform(click()) ;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withText("OK")).perform(click());


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.findVehicleProceedButton)).
                perform(click());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}
