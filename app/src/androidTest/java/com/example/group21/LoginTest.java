package com.example.group21;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.EditText;

import androidx.test.rule.ActivityTestRule;

import com.example.group21.Authentication.GabayLogin;
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

public class LoginTest {

    @Rule
    public ActivityTestRule<GabayLogin> loginActivityTestRule = new ActivityTestRule<GabayLogin>(GabayLogin.class);
    private GabayLogin mlogin = null;

    Instrumentation.ActivityMonitor monitorLogin = getInstrumentation().addMonitor(GabayLogin.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mlogin = loginActivityTestRule.getActivity();
    }

    /* Test for Login Button */
    @Test
    public void testLoginButtonActivity(){
        assertNotNull(mlogin.findViewById(R.id.login_id));
        onView(withId(R.id.login_id)).perform(click());
        Activity Login = getInstrumentation().waitForMonitorWithTimeout(monitorLogin, 5000);
        assertNotNull(Login);
        Login.finish();
    }

    @Test
    public void loginTestEditText() {

        EditText registerPassword = mlogin.findViewById(R.id.password);
        EditText registerEmail = mlogin.findViewById(R.id.Email);

        //Check if the name, email, phone are not null
        assertNotNull(registerPassword);
        assertNotNull(registerEmail);

    }

    @After
    public void tearDown() throws Exception {
        mlogin = null;
    }
}
