package com.example.group21;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.EditText;

import androidx.test.rule.ActivityTestRule;

import com.example.group21.Authentication.GabayRegister;
import com.example.group21.Authentication.Register;

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
    public ActivityTestRule<GabayRegister> registerActivityTestRule = new ActivityTestRule<GabayRegister>(GabayRegister.class);
    private GabayRegister mRegister = null;

    Instrumentation.ActivityMonitor monitorRegister = getInstrumentation().addMonitor(GabayRegister.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        mRegister = registerActivityTestRule.getActivity();

    }

    /* Test for Register Button */
    @Test
    public void testRegisterActivity(){
        assertNotNull(mRegister.findViewById(R.id.alreadyRegister));

        onView(withId(R.id.alreadyRegister)).perform(click());
        Activity Register = getInstrumentation().waitForMonitorWithTimeout(monitorRegister, 5000);
        assertNotNull(Register);
        Register.finish();
    }

    //Test for the Edit Texts fields in the register class
    @Test
    public void registerTestEditText() {

        EditText registerName = mRegister.findViewById(R.id.fullName);
        EditText registerEmail = mRegister.findViewById(R.id.Email);
        EditText registerPhone = mRegister.findViewById(R.id.password);

        //Check if the name, email, phone are not null
        assertNotNull(registerName);
        assertNotNull(registerEmail);
        assertNotNull(registerPhone);

    }

    @After
    public void tearDown() throws Exception {
        mRegister = null;

    }
}
