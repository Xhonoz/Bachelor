package com.wordpress.honeymoonbridge.bridgeapp;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasBackground;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by Carmen on 02.04.2018.
 */
public class SettingsActivityTest {

    @Rule
    public IntentsTestRule<SettingsActivity> mSettingsActivityTest = new IntentsTestRule<SettingsActivity>(SettingsActivity.class);
    private SettingsActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mSettingsActivityTest.getActivity();
    }

    @Test
    public void toGeneral(){
        onView(withText("General")).perform(click());
        hasBackground(R.xml.pref_general);

    }

    @Test
    public void toNotificitions(){
        onView(withText("Notifications")).perform(click());
        hasBackground(R.xml.pref_notification);
    }

    @Test
    public void toDataAndSync(){
        onView(withText("Data & sync")).perform(click());
        hasBackground(R.xml.pref_data_sync);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

}