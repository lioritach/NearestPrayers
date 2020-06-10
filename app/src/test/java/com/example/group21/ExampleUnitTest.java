package com.example.group21;

import com.example.group21.Authentication.GabayRegister;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void registerGabay_testPassLenFailed(){
        GabayRegister gabayRegister = new GabayRegister();
        assertEquals(false, gabayRegister.regValidation("lior", "0524499896", "lioritach1@gmail.com", "123", "123"));
    }


    @Test
    public void registerGabay_testConfPassFailed(){
        GabayRegister gabayRegister = new GabayRegister();
        assertEquals(false, gabayRegister.regValidation("lior", "0524499896", "lioritach1@gmail.com", "123456", "12345"));
    }

    @Test
    public void registerGabay_testPhoneEmptyFailed(){
        GabayRegister gabayRegister = new GabayRegister();
        assertEquals(false, gabayRegister.regValidation("lior", "", "lioritach1@gmail.com", "123456", "12345"));
    }

    @Test
    public void registerGabay_testEmailPatternFailed(){
        GabayRegister gabayRegister = new GabayRegister();
        assertEquals(false, gabayRegister.regValidation("lior", "123456", "123456", "0524499896", "lioritach1^gmail.com"));
    }
}
