package com.wordpress.honeymoonbridge.bridgeapp;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.wordpress.honeymoonbridge.bridgeapp.Activities.ChooseCardActivity;
import com.wordpress.honeymoonbridge.bridgeapp.Activities.PlayActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onView;

/**
 * Created by Carmen on 21.03.2018.
 */
public class ChooseCardActivityTest {

    @Rule
    public IntentsTestRule<ChooseCardActivity> mChooseCardActivityTest = new IntentsTestRule<ChooseCardActivity>(ChooseCardActivity.class);
    private ChooseCardActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mChooseCardActivityTest.getActivity();
    }

    @Test
    public void fromChooseCardToPlayActivity(){
        onView(withId(R.id.trump)).perform(click());

        intended(hasComponent(PlayActivity.class.getName()));

    }


    @After
    public void tearDown() throws Exception {

        mActivity = null;
    }

}