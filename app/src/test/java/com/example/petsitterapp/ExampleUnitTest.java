package com.example.petsitterapp;

import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;




import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.*;




/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }



    @Test
    public void user_creation() {
        User testUser = new User(5, null, new ArrayList<Pet>());
    }

    @Test
    public void user_getPets() {
        //testing null pets
        User testUser = new User(5, null, new ArrayList<Pet>());
        testUser.getPets();
    }

    @Test
    public void User_updatePetList() {
        User testUser = new User(5, null, new ArrayList<Pet>());
        testUser.updatePetList(new ArrayList<Pet>());
        testUser.updateOwnerJobs(new ArrayList<SittingJob>());
        testUser.updateSitterJobs(new ArrayList<SittingJob>());
    }


    @Test
    public void crazy(){
        JobActivity j = new JobActivity();
        //j.allJobsList();
        //j.getJobArr();
        //j.goToViewJob(new SittingJob( new JSONObject()));
    }




}
