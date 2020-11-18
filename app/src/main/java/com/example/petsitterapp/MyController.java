package com.example.petsitterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MyController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.w("Testing","Inside PetFormActivity onCreate.");
    }

    public void addPetView(View v) {
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", "New");

        startActivity(intent);
    }

    public void editPetView(View v) {
        Intent intent = new Intent(this, PetFormActivity.class);
        intent.putExtra("Json_NewOrEdit", "Edit");

        startActivity(intent);
    }

    public void addPet(JSONObject petInfo) {
        Pet newPet = new Pet(petInfo);
        // attach pet to owner account

        setContentView(R.layout.activity_main);
    }

    public void editPet(JSONObject newPetInfo) {
//        int petID = newPetInfo.getInt(petID);
        int petID = 2;
        Pet pet = new Pet(petID);
        pet.editPet(newPetInfo);
    }

    public void deletePet(int petID) {
        Pet pet = new Pet(petID);
        pet.deletePet();
    }
}