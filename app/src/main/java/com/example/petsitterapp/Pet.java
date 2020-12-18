/**
 * This class creates an object represenation of an individual pet's data, it is included in both
 * User objects and SittingJob objects
 * Authors: Andrew Fallon, Jeff Umanzor, Derek Morales, Nick Pierce-Ptak
 * Date updated: 12/17/20
 */
package com.example.petsitterapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class Pet {
    public String petID;
    public JSONObject petInfo;

    /**
     * This constructor is for a brand new Pet object, and adds the input information
     * to the Pet database as a new row
     * @param petInfo - JSONObject with the information entered by the user on the addPet view
     */
    public Pet(JSONObject petInfo) {
        this.petInfo = petInfo;
        try {
            this.petID = petInfo.getString("petIDKey");
        }
        catch(JSONException je) {
            this.petID = "-1";
        }
    }

    /**
     * This returns a string of the petInfo for use in the listview when viewing all of your pets at the same time
     * @return
     */
    public String toString() {
        String returnString = "";
        try {
            returnString += petInfo.getString("name")+"   -   ";
            returnString += petInfo.getString("species");
            returnString+= "  ("+petInfo.getString("breed")+")";
        }
        catch(JSONException je) {
            Log.w("MA", "JSONException Pet.toString()");
        }
        return returnString;
    }

    /**
     * Purpose: update the petInfo of this pet after it has been edited in the database
     */
    public void editPet(JSONObject newInfo) {
        this.petInfo = newInfo;
    }
}
