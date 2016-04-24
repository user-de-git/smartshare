package com.mss.group3.smartshare;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import com.mss.group3.smartshare.controller.UserTypeController;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;
import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;


/**
 * Created by Bhupinder on 4/23/2016.
 */


@RunWith(AndroidJUnit4.class)
@MediumTest
public class GControllerTest {

    @Rule
    public ActivityTestRule<UserTypeController> lController =
            new ActivityTestRule<>(UserTypeController.class);

    @Test
    public void Edit_Account() {

        onView(withText(" My Account  ")).perform(click());

        try {Thread.sleep(2750);}
        catch (InterruptedException e) {e.printStackTrace();}

        String alphabet= "abcdefghijklmnopqrstuvwxyz";
        String s = "";
        Random random = new Random();
        int randomLen = 1+random.nextInt(9);
        for (int i = 0; i < randomLen; i++) {
            char c = alphabet.charAt(random.nextInt(26));
            s+=c;
        }

        onView(withId(R.id.firstNameText)).perform(clearText(), typeText(s));

        onView(withId(R.id.registrationButton)).
                perform(scrollTo());

        onView(withId(R.id.registrationButton)).perform(click());


        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Logout")).perform(click());

        try {
            Thread.sleep(3750);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        onView(withId(R.id.userName)).
                perform(typeText("bmanhas007@hotmail.com"));
        onView(withId(R.id.userPassword)).
                perform(typeText("sandy"));
        onView(withText("Login")).
                perform(click());

        try {Thread.sleep(3750);}
        catch (InterruptedException e) {e.printStackTrace();}

        onView(withText(" My Account  ")).perform(click());

        try {Thread.sleep(2750);}
        catch (InterruptedException e) {e.printStackTrace();}

        onView(withId(R.id.firstNameText)).check(matches(withText(s)));


    }
}
