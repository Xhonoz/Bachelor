package com.wordpress.honeymoonbridge.bridgeapp;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.wordpress.honeymoonbridge.bridgeapp.Activities.MainActivity;
import com.wordpress.honeymoonbridge.bridgeapp.Activities.PlayActivity;
import com.wordpress.honeymoonbridge.bridgeapp.Activities.ResultActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Carmen on 02.04.2018.
 */
public class ResultActivityTest {
    @Rule
    public IntentsTestRule<ResultActivity> mResultActivityTest = new IntentsTestRule<ResultActivity>(ResultActivity.class);
    private ResultActivity mActivity;

    @Before
    public void setUp() throws Exception {
        mActivity = mResultActivityTest.getActivity();
    }

    @Test
    public void fromResultToPlayActivity(){
        onView(withId(R.id.playAgain)).perform(click());
        intended(hasComponent(PlayActivity.class.getName()));
    }

    @Test
    public void fromResultToMainActivity(){
        onView(withId(R.id.mainMenu)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

}