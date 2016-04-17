package com.mss.group3.smartshare;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mss.group3.smartshare.R;
import com.mss.group3.smartshare.controller.LoginController;
import com.mss.group3.smartshare.controller.MainController;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainControllerTest {

    @Rule
    public ActivityTestRule<MainController> booksActivityTestRule =
            new ActivityTestRule<>(MainController.class);

    @Test
    public void AcceptTerms_Navigate_LoginScreen() {
        //acceptTerms();
    }

    public void acceptTerms() {
        onView(withId(R.id.acceptbuttonid)).check(matches(isDisplayed()));

        onView(withId(R.id.acceptbuttonid)).
                perform(click());

        onView(withId(R.id.userPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void Valid_User_Login() {
        acceptTerms();

        onView(withId(R.id.userName)).
               perform(typeText("bmanhas007@hotmail.com"));
        onView(withId(R.id.userPassword)).
                perform(typeText("sandy"));
        onView(withText("Login")).
                perform(click());

        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.Share_Vehicle)).check(matches(isDisplayed()));
        onView(withId(R.id.Find_Vehicle)).check(matches(isDisplayed()));
        onView(withId(R.id.My_Account)).check(matches(isDisplayed()));
    }
}
