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
    Instrumentation.ActivityMonitor monitorZmaniHayum = getInstrumentation().addMonitor(ZmaniHayum.class.getName(), null, false);
    Instrumentation.ActivityMonitor monitorMap = getInstrumentation().addMonitor(map.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mainActivity = mainActivityActivityTestRule.getActivity();
    }


    //test for Siddur Card View
    @Test
    public void SiddurTestCardView(){
        assertNotNull(mainActivity.findViewById(R.id.siddur_cardID));

        onView(withId(R.id.siddur_cardID)).perform(click());
        onView(withId(R.id.ashkenaz)).perform(click());
        Activity Siddur = getInstrumentation().waitForMonitorWithTimeout(monitorSiddur, 5000);
        assertNotNull(Siddur);
        Siddur.finish();
    }

    //test for ZmaniHayum Card View
    @Test
    public void ZmaniHayumTestCardView(){

        assertNotNull(mainActivity.findViewById(R.id.zmaniHayum_cardID));
        onView(withId(R.id.zmaniHayum_cardID)).perform(click());
        Activity ZmaniHayum = getInstrumentation().waitForMonitorWithTimeout(monitorZmaniHayum, 5000);
        assertNotNull(ZmaniHayum);
        ZmaniHayum.finish();

    }

    //test for HalachaYomit Card View
    @Test
    public void HalachaYomitTestCardView(){

        assertNotNull(mainActivity.findViewById(R.id.halacha_yomit_cardID));
        onView(withId(R.id.halacha_yomit_cardID)).perform(click());

    }

    //test for map Card View
    @Test
    public void mapTestActivity(){
        assertNotNull(mainActivity.findViewById(R.id.findMinyan_cardID));
        onView(withId(R.id.findMinyan_cardID)).perform(click());
        Activity map = getInstrumentation().waitForMonitorWithTimeout(monitorMap, 5000);
        assertNotNull(map);
        map.finish();
    }




    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}
