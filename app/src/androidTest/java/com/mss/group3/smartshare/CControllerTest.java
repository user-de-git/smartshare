

package com.mss.group3.smartshare;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mss.group3.smartshare.controller.LoginController;
import com.mss.group3.smartshare.controller.UserTypeController;
import com.parse.Parse;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class CControllerTest {

    @Rule
    public ActivityTestRule<UserTypeController> lController =
            new ActivityTestRule<>(UserTypeController.class);

    @Test
    public void Click_share_Vehicle() {

        onView(withText("Share Vehicle")).
                perform(click());

        try {
            Thread.sleep(2750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.button)).check(matches(isDisplayed()));

        pressBack();

    }


    @Test
    public void Click_find_Vehicle() {

        onView(withText(" Find Vehicle ")).
                perform(click());

        try {
            Thread.sleep(2750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.registerText)).check(matches(isDisplayed()));

        pressBack();

    }

    @Test
    public void Click_manage_account() {

        onView(withText(" My Account  ")).
                perform(click());

        try {
            Thread.sleep(2750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText("Manage account")).check(matches(isDisplayed()));

        pressBack();

    }

}

