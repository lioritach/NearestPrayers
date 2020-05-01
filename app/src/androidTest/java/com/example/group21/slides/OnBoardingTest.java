package com.example.group21.slides;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.rule.ActivityTestRule;

import com.example.group21.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class OnBoardingTest {

    @Rule
    public ActivityTestRule<OnBoarding> OnBoardingActivityTestRule = new ActivityTestRule<OnBoarding>(OnBoarding.class);
    private OnBoarding onBoarding = null;
    Instrumentation.ActivityMonitor monitorOnBoarding = getInstrumentation().addMonitor(OnBoarding.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        onBoarding = OnBoardingActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        onBoarding = null;
    }

    @Test
    public void skip() {
        assertNotNull(onBoarding.findViewById(R.id.skipBtn_ID));
        onView(withId(R.id.skipBtn_ID)).perform(click());
        Activity OnBoardingSkip = getInstrumentation().waitForMonitorWithTimeout(monitorOnBoarding, 5000);
        assertNotNull(OnBoardingSkip);
        OnBoardingSkip.finish();
    }

    @Test
    public void next() {
        assertNotNull(onBoarding.findViewById(R.id.next_button));
        onView(withId(R.id.next_button)).perform(click());
        Activity OnBoardingNext = getInstrumentation().waitForMonitorWithTimeout(monitorOnBoarding, 5000);
        assertNotNull(OnBoardingNext);
        OnBoardingNext.finish();
    }


}