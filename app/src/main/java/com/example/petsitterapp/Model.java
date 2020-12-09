package com.example.petsitterapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {
    private String petQuery = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/query.php";
    private String ownerQuery = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/getOwners.php";

    private JSONArray allPets;
    private JSONArray allOwners;

    public Owner user;

    public Model() {

        getJSON();
        getOwnerJSON();
    }

    /**
     * This method checks the owner database against login credentials from the user
     * If the credentials are correct, it creates a user object for that account
     * @param username - username entered by the user
     * @param password - password entered by the user
     * @return true if the username/password are correct and present in the Owner database
     *         false if the username or password are incorrect
     */
    public boolean authenticateUser(String username, String password) {
        Log.w("MA", "username: "+username+" password"+password);
        boolean isAuthenticated = true;
        int ownerID = 0;
        // DEREK search the database for username+password,
        // DEREK if they're both present, set isAuthenticated to true and create a Owner object with the ownerID of that row
        // DEREK if the username/password are wrong, set isAuthenticated to false

        if(isAuthenticated) {
            user = new Owner(ownerID);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method creates a new pet object, which adds it to the database, and then adds that
     * pet to the owner's pet list to keep it updated
     * @param petInfo - JSONObject of all info entered in the view for a new pet
     */
    public void addPet(JSONObject petInfo) throws JSONException{
        // Creating a pet object with a JSON object adds that pet to the database automatically
        Pet newPet = new Pet(petInfo, user.ownerID);
        user.petMap.put(newPet.petID, newPet);
    }

    /**
     * This method takes new Pet information, finds the existing pet object associated with that petID
     * and changes the database to reflect any of the newly input information for that pet
     * @param newPetInfo - JSONObject of an ALREADY EXISTING PET and the newly updated info for that pet
     * @throws JSONException
     */
    public void editPet(JSONObject newPetInfo) throws JSONException{
        int petID = newPetInfo.getInt("petID");
        Pet currentPet = user.petMap.get(petID);
        currentPet.editPet(newPetInfo);
    }

    /**
     * This method gets the Pet obj. of the petID, deletes it from the database, and removes it from petMap
     * @param petID - the ID of the pet to be deleted
     */
    public void deletePet(int petID) {
        Pet pet = user.petMap.get(petID);
        pet.deletePet();
        user.petMap.remove(petID);
    }



    public class Owner {
        HashMap<Integer, Pet> petMap;
        int ownerID;

        /**
         * This constructor is for an existing Owner, accessed by login page
         * @return true if the login and password are correct, false if they are not
         */
        public Owner(int ownerID) {
            this.ownerID = ownerID;
            petMap= new HashMap<Integer, Pet>();
            // DEREK search the Pet database for any matches with the ownerID
            // DEREK create a Pet object from each match of the ownerID, store them in petMap with their petIDs as keys
        }

        /**
         * Returns an ArrayList of the Pet objects belonging to the current user
         * @return an Arraylist of the user's petMap values
         */
        public String[] getPets() {
            String[] petNames = new String[petMap.size()];
            int i=0;
            for(Pet pet:petMap.values()) {
                petNames[i] = pet.toString();
                i++;
            }
            return petNames;
        }
    }



    public class Pet {
        public int petID;
        public JSONObject petInfo;

        /**
         * This constructor is for a brand new Pet object, and adds the input information
         * to the Pet database as a new row
         * @param petInfo - JSONObject with the information entered by the user on the addPet view
         * @param ownerID - foreign key for the Pet database, attaches this pet to its owner in the Owner Database
         */
        public Pet(JSONObject petInfo, int ownerID) {
            this.petInfo = petInfo;
            this.petID = addPet(petInfo, ownerID);
        }

        /**
         * This constructor makes a pet object out of an existing row in the Pet database
         * @param petID - the primary key for the row in the Pet database that you are getting
         */
        public Pet(int petID) {
            this.petID = petID;
            // DEREK search the pet database for this existing petID, store the info in petInfo
        }

        /**
         * This returns a string of the petInfo for use in the listview when viewing all of your pets at the same time
         * @return
         */
        public String toString() {
            try{
                return (String) petInfo.get("name");
            }
            catch(JSONException je){return "";}
        }

        /**
         * Purpose: add a new row to the Pet table with the input information
         * Input: a list of values for every field in the Pet table of the DB
         * Return: the integer pet ID, primary key for the Pet table
         */
        public int addPet(JSONObject petInfo, int ownerID) {
            int petID = 0;
            // DEREK create a new row in the Pet DB with the petInfo and the owner ID
            // DEREK search for and return the petID that is generated when you make the row
            return petID;
        }

        /**
         * Purpose: edit the information of an existing pet in the DB
         * Input: a list of the new values to be put in the DB
         * Return: true if the pet row is successfully found and edited
         */
        public boolean editPet(JSONObject newInfo) {
            this.petInfo = newInfo;
            // DEREK search the database for this pet's petID, then edit that row so that
            // newInfo replaces whatever petInfo was already entered for this pet

            return true;
        }

        /**
         * Purpose: remove an existing pet from a pet owner's account
         */
        public void deletePet() {
            // DEREK find the Pet row associated with this petID and delete it from the DB
        }


    }



    // JSON stuff
    //________________________________________________________________________________________________


    // Class made by Derek that executes call to the database
    private class GetPetJSON extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w("MA", "DB Call execute");
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            try {
                loadIntoListViewPets(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(petQuery);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                System.out.println(sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }
    }

    private class GetOwnerJSON extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w("MA", "DB Call execute");
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            try {
                loadIntoListViewOwners(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(ownerQuery);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                System.out.println(sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }
    }

    /**
     * Method to call the getJSON object
     */
    private void getJSON() {
        GetPetJSON getJSON = new GetPetJSON();
        getJSON.execute();
    }

    private void getOwnerJSON() {
        GetOwnerJSON ownerJSON = new GetOwnerJSON();
        ownerJSON.execute();
    }

    /**
     * This method is called inside the getJSON class, it parses a returned JSONArray
     * @param json - the raw string of the json to be parsed
     * @throws JSONException
     */
    private void loadIntoListViewPets(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        System.out.println(jsonArray.toString());
        allPets = jsonArray;
        String[] pets = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            pets[i] = obj.getString("name");
            System.out.println("name");
        }
    }

    private void loadIntoListViewOwners(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        System.out.println(jsonArray.toString());
        allOwners = jsonArray;
        String[] pets = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            pets[i] = obj.getString("name");
            System.out.println("name");
        }
    }

    // This function returns a column from the owner database
    private String getOwnerColumn(String columnName) {
        return "";
    }

    private JSONObject[] getPets(int ownerID) {
        // DEREK return an array of all the json objects of database rows that match the owner ID
        return null;
    }

    // This function returns a column from the pet database
    private String getPetColumn(String columnName) {
        // DEREK Get the JSONObject from the database, store it in petInfo
//        JSONObject petInfo;
//        petInfo.getString(columnName);
        return null;
    }

}
