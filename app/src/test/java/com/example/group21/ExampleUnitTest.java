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

    //register tests
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
        assertEquals(true, gabayRegister.regValidation("lior", "", "lioritach1@gmail.com", "123456", "123456"));
    }

    @Test
    public void registerGabay_testEmailPatternFailed(){
        GabayRegister gabayRegister = new GabayRegister();
        assertEquals(false, gabayRegister.regValidation("lior", "0524499896", "lioritach1&gmail.com", "123456", "123456"));
    }

    @Test
    public void registerGabay_testRegister_correct(){
        GabayRegister gabayRegister = new GabayRegister();
        assertEquals(true, gabayRegister.regValidation("lior", "0524499896", "lioritach1@gmail.com", "123456", "123456"));
    }


    //edit synagogue tests
    @Test
    public void editSynGabay_testFullAddressEmptyFailed(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "beersheva", "israel", "darom", "sfaradi", "08:00", "13:00", "20:00", ""));
    }

    @Test
    public void editSynGabay_testCityEmptyFailed(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "", "israel", "darom", "sfaradi", "08:00", "13:00", "20:00", "name of street"));
    }

    @Test
    public void editSynGabay_testCountryEmptyFailed(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "beersheva", "", "darom", "sfaradi", "08:00", "13:00", "20:00", "name of street"));
    }

    @Test
    public void editSynGabay_testStateEmptyFailed(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "beersheva", "israel", "", "sfaradi", "08:00", "13:00", "20:00", "name of street"));
    }

    @Test
    public void editSynGabay_testCategoryEmptyFailed(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "beersheva", "israel", "darom", "", "08:00", "13:00", "20:00", "name of street"));
    }

    @Test
    public void editSynGabay_testShacharitEmptyFailed(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "beersheva", "israel", "darom", "sfaradi", "", "13:00", "20:00", "name of street"));
    }

    @Test
    public void editSynGabay_testMinhaEmptyFailed(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "beersheva", "israel", "darom", "sfaradi", "08:00", "", "20:00", "name of street"));
    }

    @Test
    public void editSynGabay_testArvitEmptyFailed(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "beersheva", "israel", "darom", "sfaradi", "08:00", "13:00", "", "name of street"));
    }

    @Test
    public void editSynGabay_testEditCorrect(){
        EditSynagogueActivity editSynagogueActivity = new EditSynagogueActivity();
        assertEquals(true, editSynagogueActivity.editValidation("name", "beersheva", "israel", "darom", "sfaradi", "08:00", "13:00", "20:00", "name of street"));
    }


}
