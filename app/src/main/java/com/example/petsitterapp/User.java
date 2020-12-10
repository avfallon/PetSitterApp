package com.example.petsitterapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    HashMap<Integer, Pet> petMap;
    ArrayList<Pet> petList;
    JSONObject accountInfo;
    int userID;

    /**
     * This constructor is for an existing Owner, accessed by login page
     * @param userID the ID of the user account
     * @param accountInfo the JSON of all account info, some fields may be missing if it's just an owner or sitter
     */
    public User(int userID, JSONObject accountInfo, ArrayList<Pet> pets)  {
        Log.w("MA", "Inside User");

        this.userID = userID;
        this.accountInfo = accountInfo;
        this.petList = pets;
    }

    /**
     * Returns a string array of the pets
     * @return an array of each String in pet info
     */
    public String[] getPets() {
        String[] pets = new String[petList.size()];
        int i=0;
        for(Pet pet:petList) {
            pets[i] = pet.toString();
            i++;
        }
        return pets;
    }
}

