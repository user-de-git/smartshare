package com.mss.group3.smartshare;

import android.content.Intent;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.mss.group3.smartshare.controller.OwnerController;
import com.mss.group3.smartshare.model.VehicleDataStore;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Bhupinder on 4/22/2016.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class EControllerTest{

    @Rule
    public ActivityTestRule<OwnerController> lController =
            new ActivityTestRule<>(OwnerController.class);

    @Test
    public void Update_Post() {
        onData(is(instanceOf(VehicleDataStore.class))) // We are using the position so don't need to specify a data matcher
                .inAdapterView(withId(R.id.listView)) // Specify the explicit id of the ListView
                .atPosition(0) // Explicitly specify the adapter item to use
                .perform(click()); // Standard ViewAction

        try {
            Thread.sleep(4750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.et_pricekm)).
                perform(clearText(), typeText("4"));

        onView(withId(R.id.button_addModifyVehicle)).
                perform(scrollTo());

        onView(withId(R.id.button_addModifyVehicle)).
                perform(click());

        try {
            Thread.sleep(4750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .check(matches(hasDescendant(allOf(withId(R.id.tv_price), withText(containsString("$ 4 /km"))))));

    }




}
