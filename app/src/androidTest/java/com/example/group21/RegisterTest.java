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

public class RegisterTest {

    @Rule
    public ActivityTestRule<Register> registerActivityTestRule = new ActivityTestRule<Register>(Register.class);
    private Register mRegister = null;

    Instrumentation.ActivityMonitor monitorRegister = getInstrumentation().addMonitor(Register.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mRegister = registerActivityTestRule.getActivity();

    }

    @Test
    public void testRegisterActivity(){
        assertNotNull(mRegister.findViewById(R.id.alreadyRegister));

        onView(withId(R.id.alreadyRegister)).perform(click());
        Activity Register = getInstrumentation().waitForMonitorWithTimeout(monitorRegister, 5000);
        assertNotNull(Register);
        Register.finish();
    }

    @After
    public void tearDown() throws Exception {
        mRegister = null;

    }
}