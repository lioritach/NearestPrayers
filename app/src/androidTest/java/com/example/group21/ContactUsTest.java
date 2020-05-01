package com.example.group21;

import android.app.Activity;
import android.app.Instrumentation;
import android.widget.EditText;

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

public class ContactUsTest {

    @Rule
    public ActivityTestRule<ContactUs> ContactUsActivityTestRule = new ActivityTestRule<ContactUs>(ContactUs.class);
    private ContactUs mContactUs = null;

    Instrumentation.ActivityMonitor monitorContactUs = getInstrumentation().addMonitor(ContactUs.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        mContactUs = ContactUsActivityTestRule.getActivity();
    }

    @Test
    public void ContactUsTestTextView(){

        EditText ourEmailData = mContactUs.findViewById(R.id.ourmailID);
        EditText messageData = mContactUs.findViewById(R.id.messageID);
        EditText subjectData = mContactUs.findViewById(R.id.subjectID);

        //Check if the email, message, subject are not null
        assertNotNull(ourEmailData);
        assertNotNull(messageData);
        assertNotNull(subjectData);
    }

    //Test for send button
    @Test
    public void ContactUsTestButton(){

        assertNotNull(mContactUs.findViewById(R.id.btn_sendID));
        onView(withId(R.id.btn_sendID)).perform(click());
        Activity SendButton = getInstrumentation().waitForMonitorWithTimeout(monitorContactUs, 5000);
        assertNotNull(SendButton);
        SendButton.finish();

    }

    @After
    public void tearDown() throws Exception {
        mContactUs = null;
    }
}