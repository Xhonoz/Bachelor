package com.wordpress.honeymoonbridge.bridgeapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.wordpress.honeymoonbridge.bridgeapp.Activities.MainActivity;
import com.wordpress.honeymoonbridge.bridgeapp.Activities.SettingsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Carmen on 21.03.2018.
 */
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mMainActivityTest =
            new IntentsTestRule<>(MainActivity.class);
    private MainActivity mActivity = null;


   @Before
   public void setUp() throws Exception {
mActivity = mMainActivityTest.getActivity();
    }



    @Test
    public void fromMainToBiddingActivity(){

        onView(withId(R.id.play)).perform(click());

        intended(hasComponent(BiddingActivity.class.getName()));
    }


    @Test
    public void fromMainToSettings(){
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Settings"))
                .perform(click());

        intended(hasComponent(SettingsActivity.class.getName()));
    }



    @After
    public void tearDown() throws Exception {

       mActivity = null;
    }



}