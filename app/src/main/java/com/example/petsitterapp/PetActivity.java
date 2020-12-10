package com.example.petsitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class PetActivity extends AppCompatActivity {
    private JSONObject petInfo;

    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        petInfo = getTransferredPetData();
    }
    //Method slowly for taking the petData that was transferred through intent.putExtra,
    //Takes that data which is received as a String and turns it back into a JSONObject.
    public JSONObject getTransferredPetData() {
        Intent intent = this.getIntent();
        JSONObject transferData = null;
        try {
            transferData = new JSONObject(intent.getStringExtra("Json_NewOrEdit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.w("Testing", "The data collected is: " + transferData);

        return transferData;
    }
}
