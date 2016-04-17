package com.mss.group3.smartshare;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mss.group3.smartshare.controller.LoginController;
import com.parse.Parse;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class LoginControllerTest {

    @Rule
    public ActivityTestRule<LoginController> lController =
            new ActivityTestRule<>(LoginController.class);

    @Test
    public void Login_Valid_User() {


        onView(withId(R.id.userName)).
                perform(typeText("bmanhas007@hotmail.com"));
        onView(withId(R.id.userPassword)).
                perform(typeText("sandy"));
        onView(withText("Login")).
                perform(click());

        try {
            Thread.sleep(3750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.Share_Vehicle)).check(matches(isDisplayed()));
        onView(withId(R.id.Find_Vehicle)).check(matches(isDisplayed()));
        onView(withId(R.id.My_Account)).check(matches(isDisplayed()));
    }
}
