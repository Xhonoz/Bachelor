package com.wordpress.honeymoonbridge.bridgeapp;

import android.support.test.espresso.intent.rule.IntentsTestRule;

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
public class BiddingActivityTest {

    @Rule
public IntentsTestRule<BiddingActivity> mBiddingActivity = new IntentsTestRule<BiddingActivity>(BiddingActivity.class);
private BiddingActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
    mActivity = mBiddingActivity.getActivity();
    }

    @Test
    public void fromBiddingToChooseCardActivity(){
        onView(withId(R.id.playAfterBidding)).perform(click());

        intended(hasComponent(ChooseCardActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception {
mActivity = null;
    }

}