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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Carmen on 21.03.2018.
 */
public class PlayActivityTest {

    @Rule
    public IntentsTestRule<PlayActivity> mPlayActivityTest = new IntentsTestRule<PlayActivity>(PlayActivity.class);
    private PlayActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mPlayActivityTest.getActivity();
    }

    @Test
    public void fromPlayToResultActivity(){
        onView(withId(R.id.YourPlayedCard)).perform(click());
        intended(hasComponent(ResultActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {

        mActivity = null;
    }

}