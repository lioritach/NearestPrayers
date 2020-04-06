package com.example.group21;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

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

public class LoginTest {

    @Rule
    public ActivityTestRule<Login> loginActivityTestRule = new ActivityTestRule<Login>(Login.class);
    private Login mlogin = null;

    Instrumentation.ActivityMonitor monitorLogin = getInstrumentation().addMonitor(Login.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mlogin = loginActivityTestRule.getActivity();
    }

    /* Test for Login Button */
    @Test
    public void testLoginActivity(){
        assertNotNull(mlogin.findViewById(R.id.login_id));
        onView(withId(R.id.login_id)).perform(click());
        Activity Login = getInstrumentation().waitForMonitorWithTimeout(monitorLogin, 5000);
        assertNotNull(Login);
        Login.finish();

    }

    @After
    public void tearDown() throws Exception {
        mlogin = null;
    }
}