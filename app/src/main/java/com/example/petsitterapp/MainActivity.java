package com.example.petsitterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        //Testing PetformRoutes, newPet and editPet
        JSONObject PetChoiceNew  =  new JSONObject();
        JSONObject PetChoiceEdit  =  new JSONObject();
        // This is for testing CreateNewPet
        try {
            PetChoiceNew.put("NewOrEdit","New");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // This is for testing EditPet
        try {
            PetChoiceEdit.put("NewOrEdit","Edit");
            //add an array of HardCoded PetInfo... look it up
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Starts PetFormActivity
        goToPetFormActivity_CreateNewPet(PetChoiceNew); //This is for Making an activity for a new pet.
        //goToPetFormActivity_CreateNewPet(PetChoiceEdit); //This is for Making an activity for editing a pet.
    }

    public void goToPetFormActivity_CreateNewPet(JSONObject PetChoice) {


        // Create an Intent to go to DataActivity
        Intent intent = new Intent(this, PetFormActivity.class);
        //intent.putExtra("shifterNum", model.getNum());
        // start a DataActivity
        startActivity(intent);

    }


}