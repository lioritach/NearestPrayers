package com.example.group21;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.rule.ActivityTestRule;

import com.example.group21.Authentication.Login;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class SiddurTest {

    @Rule
    public ActivityTestRule<Siddur> siddurActivityTestRule = new ActivityTestRule<Siddur>(Siddur.class);
    private Siddur mSiddur = null;
    Instrumentation.ActivityMonitor monitorSiddur = getInstrumentation().addMonitor(Siddur.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mSiddur = siddurActivityTestRule.getActivity();
    }

    @Test
    public void testAshkenazActivity(){
        assertNotNull(mSiddur.findViewById(R.id.ashkenaz));
        onView(withId(R.id.ashkenaz)).perform(click());
        Activity Siddur = getInstrumentation().waitForMonitorWithTimeout(monitorSiddur, 5000);
        assertNotNull(Siddur);
        Siddur.finish();
    }

    @Test
    public void testSfaradiActivity(){
        assertNotNull(mSiddur.findViewById(R.id.sfaradi));
        onView(withId(R.id.sfaradi)).perform(click());
        Activity Siddur = getInstrumentation().waitForMonitorWithTimeout(monitorSiddur, 5000);
        assertNotNull(Siddur);
        Siddur.finish();
    }

    @After
    public void tearDown() throws Exception {
        mSiddur = null;
    }
}