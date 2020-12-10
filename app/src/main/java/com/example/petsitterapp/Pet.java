package com.example.petsitterapp;

import org.json.JSONException;
import org.json.JSONObject;


public class Pet {
    public int petID;
    public JSONObject petInfo;

    /**
     * This constructor is for a brand new Pet object, and adds the input information
     * to the Pet database as a new row
     * @param petInfo - JSONObject with the information entered by the user on the addPet view
     */
    public Pet(JSONObject petInfo) {
        this.petInfo = petInfo;
        try {
            this.petID = petInfo.getInt("petIDKey");
        }
        catch(JSONException je) {
            this.petID = -1;
        }
    }

    /**
     * This returns a string of the petInfo for use in the listview when viewing all of your pets at the same time
     * @return
     */
    public String toString() {
        return petInfo.toString();
    }

    /**
     * Purpose: edit the information of an existing pet in the DB
     * Input: a list of the new values to be put in the DB
     * Return: true if the pet row is successfully found and edited
     */
    public void editPet(JSONObject newInfo) {
        this.petInfo = newInfo;
    }
}
