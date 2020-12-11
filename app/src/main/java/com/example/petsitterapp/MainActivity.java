package com.example.petsitterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static JSONObject petInfoReturned;
    private FusedLocationProviderClient fusedLocationClient;
    public static Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petform);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            userLocation = location;
                            System.out.println(userLocation);
                        }
                    }
                });

        String[] tester = {"one", "two"};
//        Pet testPet = new Pet(tester);

        Log.w("Testing","---This Is The Start Of The Test!!!---");
        //TestPetApp();


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

    public void TestPetApp( View v ) {
        Log.w("Testing", "Inside TestPetApp");
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
        goToPetFormActivity_CreateNewPet(PetChoiceEdit);
       //Log.w("Testing", "The returned JSON Object is: " + result);

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

    //public JSONObject goToPetFormActivity_CreateNewPet(JSONObject PetChoice) {
    public void goToPetFormActivity_CreateNewPet(JSONObject PetChoice) {
        Log.w("Testing","This is what will be sent to petForm Acitivty: " + PetChoice.toString());

        // Create an Intent to go to DataActivity
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", PetChoice.toString());
        //start a DataActivity
        startActivity(intent);


        //return PetChoice;
    }

    protected void onResume( ) {
        super.onResume( );
        Log.w( "MA", "Inside MainActivity::onResume" );
        //get the new/updated petInfo
        Log.w("Testing", "The data returned is: " + petInfoReturned);
    }


}