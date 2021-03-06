/**
 * This class creates an object represenation of an individual user in the database, which
 * may contain sitter capabilities, owner capabilities, or both capabilities
 * @author: Andrew Fallon, Jeff Umanzor, Derek Morales, Nick Pierce-Ptak
 * Date updated: 12/17/20
 */

package com.example.petsitterapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    ArrayList<Pet> petList;

    ArrayList<SittingJob> openJobsSitter;
    ArrayList<SittingJob> openJobsOwner;

    JSONObject accountInfo;
    int userID;

    /**
     * This constructor is for an existing Owner, accessed by login page
     * @param userID the ID of the user account
     * @param accountInfo the JSON of all account info, some fields may be missing if it's just an owner or sitter
     */
    public User(int userID, JSONObject accountInfo, ArrayList<Pet> pets)  {
        //Log.w("MA", "Inside User");

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

    /**
     * Update the list of pets of the user
     * @param newList The new list of pets for the user
     */
    public void updatePetList(ArrayList<Pet> newList) {
        petList = (ArrayList<Pet>) newList.clone();
    }


    /**
     * Update the ownerJobs of the user
     * @param ownerJobs The list of jobs they have
     */
    public void updateOwnerJobs(ArrayList<SittingJob> ownerJobs) {
        this.openJobsOwner = (ArrayList<SittingJob>) ownerJobs.clone();
    }

    /**
     * Update the list of open sitter jobs
     * @param sitterJobs The new list of sitter jobs available
     */
    public void updateSitterJobs(ArrayList<SittingJob> sitterJobs) {
        this.openJobsOwner = (ArrayList<SittingJob>) sitterJobs.clone();

    }
}

