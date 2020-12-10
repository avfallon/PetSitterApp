package com.example.petsitterapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class User {
    HashMap<Integer, Model.Pet> petMap;
    int userID;

    /**
     * This constructor is for an existing Owner, accessed by login page
     * @return true if the login and password are correct, false if they are not
     */
    public User(int id)  {
        this.userID = id;
        this.petMap = Controller.model.buildPetMap(userID);
    }

    /**
     * Returns an ArrayList of the Pet objects belonging to the current user
     * @return an Arraylist of the user's petMap values
     */
    public String[] getPets() throws JSONException {
        String[] petNames = new String[petMap.size()];
        int i=0;
        for(Model.Pet pet:petMap.values()) {
            petNames[i] = pet.toString();
            i++;
        }
        return petNames;
    }
}

