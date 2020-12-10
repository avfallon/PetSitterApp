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
     * Purpose: update the petInfo of this pet after it has been edited in the database
     */
    public void editPet(JSONObject newInfo) {
        this.petInfo = newInfo;
    }
}
