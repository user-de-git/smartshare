package com.mss.group3.smartshare;

import android.content.Intent;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.ListView;

import com.mss.group3.smartshare.controller.OwnerController;
import com.mss.group3.smartshare.model.VehicleDataStore;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
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
 * Created by Bhupinder on 4/22/2016.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class FControllerTest {

    @Rule
    public ActivityTestRule<OwnerController> lController =
            new ActivityTestRule<>(OwnerController.class);



    @Test
    public void Delete_Post() {

        final int[] counts_before = new int[1];
        onView(withId(R.id.listView)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;

                counts_before[0] = listView.getCount();

                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));


        onData(is(instanceOf(VehicleDataStore.class))) // We are using the position so don't need to specify a data matcher
                .inAdapterView(withId(R.id.listView)) // Specify the explicit id of the ListView
                .atPosition(1) // Explicitly specify the adapter item to use
                .perform(click()); // Standard ViewAction

        onView(withId(R.id.button_deleteVehicle)).
                perform(scrollTo());

        onView(withId(R.id.button_deleteVehicle)).
                perform(click());

        //Intent intent = new Intent();
        //lController.launchActivity(intent);

        try {
            Thread.sleep(4750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final int[] counts_after = new int[1];
        onView(withId(R.id.listView)).check(matches(new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                ListView listView = (ListView) view;

                counts_after[0] = listView.getCount();

                return true;
            }

            @Override
            public void describeTo(Description description) {

            }
        }));


        assertEquals("size decreae by one", counts_before[0]-counts_after[0], 1);

       /*
        onData(anything()) // We are using the position so don't need to specify a data matcher
                .inAdapterView(withId(R.id.listView)) // Specify the explicit id of the ListView
                .atPosition(1) // Explicitly specify the adapter item to use
                .check(matches(not(isDisplayed())));
                */

    }
}
