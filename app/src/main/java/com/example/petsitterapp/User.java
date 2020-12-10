package com.example.petsitterapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class User {
    HashMap<Integer, Pet> petMap;
    JSONObject accountInfo;
    int userID;

    /**
     * This constructor is for an existing Owner, accessed by login page
     * @param userID the ID of the user account
     * @param accountInfo the JSON of all account info, some fields may be missing if it's just an owner or sitter
     */
    public User(int userID, JSONObject accountInfo)  {
        this.userID = userID;
        this.accountInfo = accountInfo;
        this.petMap = Controller.model.buildPetMap(userID);
    }

    /**
     * Returns a string array of the pets
     * @return an array of each String in pet info
     */
    public String[] getPets() {
        String[] pets = new String[petMap.size()];
        int i=0;
        for(Pet pet:petMap.values()) {
            pets[i] = pet.toString();
            i++;
        }
        return pets;
    }
}

