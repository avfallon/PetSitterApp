package com.example.petsitterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w("Testing","---This Is The Start Of The Test!!!---");
        TestPetApp();
        Log.w("Testing","---This Is The End Of The Test!!!---");

        //Current Testing results, shows what will be send through intent using PutExtra for both.
        //These messeges below show the CONNECTIONS between this activity(Psuedo Owner) &
            //The Petform activity. The snippet below shows what data will be sent forward and
            //Back, as proof of concept. It currently only tests using Name and Species.
            //This is hardcoded input/output test.
        /*
            ---This Is The Start Of The Test!!!---
            W/Testing: This is what will be sent to petForm Acitivty: {"NewOrEdit":"New"}
            W/Testing: The returned JSON Object is: {"NewOrEdit":"New"}
            W/Testing: ---This Is The End Of The Test!!!---
            W/Testing: ---This Is The Start Of The Test!!!---
            W/Testing: This is what will be sent to petForm Acitivty:
                {"NewOrEdit":"Edit","FakePetObjectJSON":{"petName":"Pumpkin","petSpecies":"Dog"}}
            W/Testing: The returned JSON Object is: {"NewOrEdit":"Edit","FakePetObjectJSON":
                {"petName":"Pumpkin","petSpecies":"Dog"}}
            W/Testing: ---This Is The End Of The Test!!!---
         */
    }

    public void TestPetApp() {
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
        //Add a FakePetObject in JSON format ONTO the editPet
        try {
            PetChoiceEdit = fakePetObject( PetChoiceEdit );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Testing//Log.w("Testing","PetChoiceEdit after adding petObject info: " + PetChoiceEdit);

        //Starts PetFormActivity
            //This is for Making an activity for a new pet.
        //JSONObject result = goToPetFormActivity_CreateNewPet(PetChoiceNew);
            //This is for Making an activity for editing a pet.
        JSONObject result = goToPetFormActivity_CreateNewPet(PetChoiceEdit);
        Log.w("Testing", "The returned JSON Object is: " + result);

    }

    public JSONObject fakePetObject( JSONObject petHolder ) throws JSONException {
        JSONObject fakePetObject = new JSONObject();
        try {
            fakePetObject.put("petName", "Pumpkin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            fakePetObject.put("petSpecies", "Dog");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        petHolder.put("FakePetObjectJSON", fakePetObject); //adds petObjectJson to the input Json
        return petHolder;
    }

    public JSONObject goToPetFormActivity_CreateNewPet(JSONObject PetChoice) {
        Log.w("Testing","This is what will be sent to petForm Acitivty: " + PetChoice.toString());

        // Create an Intent to go to DataActivity
        //  Intent intent = new Intent(this, PetFormActivity.class);
        //  intent.putExtra("Json_NewOrEdit", PetChoice.toString());
        //start a DataActivity
        //  startActivity(intent);


        return PetChoice;
    }


}