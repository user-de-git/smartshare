package com.mss.group3.smartshare;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mss.group3.smartshare.controller.LoginController;
import com.mss.group3.smartshare.controller.PostVehicleController;
import com.mss.group3.smartshare.controller.UserTypeController;
import com.parse.Parse;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class DControllerTest {

    @Rule
    public ActivityTestRule<UserTypeController> lController =
            new ActivityTestRule<>(UserTypeController.class);

    @Test
    public void Post_Vehicle() {

        onView(withText("Share Vehicle")).
                perform(click());

        try {
            Thread.sleep(2750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.button)).check(matches(isDisplayed()));

        onView(withId(R.id.button)).perform(click());

        try {
            Thread.sleep(2750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.et_overduecharges)).
                perform(typeText("100"));

        onView(withId(R.id.et_platenumber)).
                perform(typeText("test 1234"));

        onView(withId(R.id.et_pricekm)).
                perform(typeText("2"));

        onView(withId(R.id.get_StartDateTime)).
                perform(typeText("5/20/2016 15:15"));

        onView(withId(R.id.get_EndDateTime)).
                perform(typeText("5/25/2016 15:15"));

        //swipeDown();


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

        onView(withId(R.id.et_vehiclerange)).
                perform(typeText("100"));

        onView(withId(R.id.button_addModifyVehicle)).perform(click());





    }
}
