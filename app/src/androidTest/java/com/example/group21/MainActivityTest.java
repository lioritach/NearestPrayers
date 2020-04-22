package com.example.group21;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;

    Instrumentation.ActivityMonitor monitorSiddur = getInstrumentation().addMonitor(Siddur.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }


    @Test
    public void testCardViewSiddur(){
        assertNotNull(mainActivity.findViewById(R.id.siddur_cardID));

        onView(withId(R.id.siddur_cardID)).perform(click());
        Activity Siddur = getInstrumentation().waitForMonitorWithTimeout(monitorSiddur, 5000);
        assertNotNull(Siddur);
        Siddur.finish();
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}