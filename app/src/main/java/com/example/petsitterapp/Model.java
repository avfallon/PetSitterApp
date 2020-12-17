package com.example.petsitterapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class Model {
    private String petQuery = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/query.php";
    private String ownerQuery = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/getOwners.php";
    private String jobsQuery = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/getJobs.php";

    public JSONArray allPets;
    public JSONArray allOwners;
    public static JSONArray allJobs;

    public static ArrayList<Pet> usersPets = new ArrayList<>();
    public static ArrayList<SittingJob> ownersJobs = new ArrayList<>();
    public static ArrayList<SittingJob> sittersJobs = new ArrayList<>();
    public static ArrayList<SittingJob> openJobs = new ArrayList<>();

    public static Context context;

    public Model() throws JSONException, IOException {

        getJSON();
        getOwnerJSON();
        getJobs();

        //buildPetList(currentUser.userID);

        String examplePet = "{\"ownerIDKey\":\"4\",\"petIDKey\":\"6\",\"name\":\"Andrew\",\"species\":\"Cowboy\",\"size\":\"Small\",\"temperament\":\"Lazy\",\"breed\":\"german shep\",\"age\":\"21\",\"diet\":\"Peanut Butter\",\"healthIssues\":\"Needs Glasses\",\"extraInfo\":\"Extras\"}";
        JSONObject examplePetJSON = new JSONObject(examplePet);
        //deletePet(examplePetJSON);
    }

    /**
     * This method checks the owner database against login credentials from the user
     * If the credentials are correct, it returns a user object for that account
     * @param username - username entered by the user
     * @param password - password entered by the user
     * @return the user associated with the username and password, or null if not found
     */
    public User authenticateUser(String username, String password) throws JSONException {
        Log.w("MA", "username: "+username+" password: "+password);
        boolean isAuthenticated = false;
        int ownerID = 0;
        for (int i = 0; i < allOwners.length(); i++) {
            JSONObject obj = allOwners.getJSONObject(i);
            String usernameAuth = obj.getString("email");
            String passwordAuth = obj.getString("password");
            // String petsOwners = obj.getString("OwnerIDKey");
            if (username.equals(usernameAuth) && password.equals(passwordAuth)) {
                int userID = obj.getInt("ownerIDKey");
                User newUser = null;
                try {
                    buildPetList(userID);
                    newUser = new User(userID, obj, usersPets);
                }
                catch(Exception e) {
                    Log.w("MA", "Exception in AuthenticateUser: " + e);
                }
                Log.w("MA", "Login Successful");
                return newUser;
            }
        }


        return null;
    }

    /**
     * This method creates a new pet object, which adds it to the database, and then adds that
     * pet to the owner's pet list to keep it updated
     * @param petInfo - JSONObject of all info entered in the view for a new pet
     */
    public void addPet(JSONObject petInfo) throws JSONException, IOException {
        // Creating a pet object with a JSON object adds that pet to the database automatically
        String addPetURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/addPet.php?json="+petInfo;
        AddPet getJSON = new AddPet(addPetURL);
        getJSON.execute();
        Pet newPet = new Pet(petInfo);
        usersPets.add(newPet);
        System.out.println(usersPets.toString());
        for(int i = 0; i < allOwners.length(); i++){
            JSONObject obj = allOwners.getJSONObject(i);
            if(Integer.parseInt(obj.getString("typeOfAccount")) == Controller.SITTER_ACCOUNT){
                LatLng myAddy = getLocationFromAddress(context, obj.getString("address"));
                if(myAddy == null){
                    break;
                }
                else{
                    System.out.println(myAddy.toString());
                }

                float[] distance = new float[1];
                Location.distanceBetween(Controller.wayLatitude, Controller.wayLongitude,
                        myAddy.latitude, myAddy.longitude, distance);

                double radiusInMeters = 48.2803*1000.0; //1 KM = 1000 Meter

                if( distance[0]/1000 > radiusInMeters ){
                    System.out.println("Outside, distance from center: " + distance[0]/1000 + " radius: " + radiusInMeters);
                } else {
                    System.out.println("Inside, distance from center: " + distance[0]/1000 + " radius: " + radiusInMeters);
                    String emailURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/sendEmail.php?email="+obj.getString("email");
                    AddPet sendEmail = new AddPet(emailURL);
                    sendEmail.execute();
                }
            }

        }


    }

    /**
     * Clears the global variables carrying the specific user information: called when logging outjj
     */
    public void logout() {
        usersPets = new ArrayList<>();
        ownersJobs = new ArrayList<>();
        sittersJobs = new ArrayList<>();
        openJobs = new ArrayList<>();

    }

    private class AddPet extends AsyncTask<Void, Void, String> {
        String urlPHP = "";

        protected AddPet(String url){
            this.urlPHP = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w("MA", "DB Call execute");
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            System.out.println("Added Pet");
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(urlPHP);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                //System.out.println(sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }
    }

    /**
     * Purpose: edit the information of an existing pet in the DB
     * Input: a list of the new values to be put in the DB
     * Return: true if the pet row is successfully found and edited
     */
    public void editPet(JSONObject newInfo) throws JSONException {
        String editPetURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/editPet.php?json="+newInfo;
        EditPet getJSON = new EditPet(editPetURL);
        getJSON.execute();
        for (int i = 0; i < usersPets.size(); i++) {
            JSONObject obj = usersPets.get(i).petInfo;
            int petsOwners = obj.getInt("petIDKey");
            System.out.println(petsOwners);
            if (petsOwners == newInfo.getInt("petIDKey")) {
                Pet newPet = new Pet(newInfo);
                usersPets.set(i, newPet);
            }
        }
    }

    private class EditPet extends AsyncTask<Void, Void, String> {
        String urlPHP = "";

        protected EditPet(String url){
            this.urlPHP = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w("MA", "DB Call execute");
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            System.out.println("Edited Pet");
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(urlPHP);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                //System.out.println(sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }
    }

    /**
     * This method gets the Pet obj. of the petID, deletes it from the database, and removes it from petMap
     * @param pet - the ID of the pet to be deleted
     */
    public void deletePet(JSONObject pet) throws JSONException {
        String editPetURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/deletePet.php?json="+pet;
        DeletePet getJSON = new DeletePet(editPetURL);
        getJSON.execute();
        for (int i = 0; i < usersPets.size(); i++) {
            JSONObject obj = usersPets.get(i).petInfo;
            int petsOwners = obj.getInt("petIDKey");
            System.out.println(petsOwners);
            if (petsOwners == pet.getInt("petIDKey")) {
                usersPets.remove(usersPets.get(i));
            }
        }
    }

    private class DeletePet extends AsyncTask<Void, Void, String> {
        String urlPHP = "";

        protected DeletePet(String url){
            this.urlPHP = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w("MA", "DB Call execute");
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            System.out.println("Deleted Pet");
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(urlPHP);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                //System.out.println(sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }
    }

    public User createAccount(JSONObject user) throws JSONException {
        String editPetURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/createAccount.php?json="+user;
        CreateAccount getJSON = new CreateAccount(editPetURL);
        getJSON.execute();
        User newUser = new User(user.getInt("ownerIDKey"), user, usersPets);
        return newUser;
    }

    public User editAccount(User newUser) throws JSONException {
        String editPetURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/editAccount.php?json="+newUser.accountInfo;
        CreateAccount getJSON = new CreateAccount(editPetURL);
        getJSON.execute();
        User editedUser = new User(newUser.accountInfo.getInt("ownerIDKey"), newUser.accountInfo, usersPets);
        return editedUser;
    }

    public void deleteAccount(User currUser){
        String editPetURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/deleteAccount.php?json="+currUser.accountInfo;
        CreateAccount getJSON = new CreateAccount(editPetURL);
        getJSON.execute();
        Controller.currentUser = null;
    }

    public void createJob(SittingJob newJob){
        String createPetURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/createJob.php?json="+newJob.jobInfo;
        CreateAccount getJSON = new CreateAccount(createPetURL);
        getJSON.execute();
        ownersJobs.add(newJob);
        Controller.currentUser.updateOwnerJobs(ownersJobs);
    }

    public void deleteJob(SittingJob job) throws JSONException {
        String deletePetURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/deleteJob.php?json="+job.jobInfo;
        CreateAccount getJSON = new CreateAccount(deletePetURL);
        getJSON.execute();
        for (int i = 0; i < ownersJobs.size(); i++) {
            JSONObject obj = ownersJobs.get(i).jobInfo;
            if (obj.getInt("jobID") == job.jobInfo.getInt("jobID")) {
                ownersJobs.remove(ownersJobs.get(i));
            }
        }
        Controller.currentUser.updateOwnerJobs(ownersJobs);
    }

    private class CreateAccount extends AsyncTask<Void, Void, String> {
        String urlPHP = "";

        protected CreateAccount(String url){
            this.urlPHP = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.w("MA", "DB Call execute");
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            System.out.println("Deleted Pet");
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(urlPHP);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                //System.out.println(sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }
    }

    /**
     *
     * @param userID ID of the user whose pets we are finding
     */
    public void buildPetList(int userID) throws JSONException {
        for (int i = 0; i < allPets.length(); i++) {
            JSONObject obj = allPets.getJSONObject(i);
            int petsOwners = Integer.parseInt(obj.getString("ownerIDKey"));
            System.out.println(petsOwners);
            if (petsOwners == userID) {
                Pet newPet = new Pet(obj);
                usersPets.add(newPet);
            }
        }
    }

    public static void assignJobs() throws JSONException {
        for (int j = 0; j < allJobs.length(); j++) {
            JSONObject jobObj = allJobs.getJSONObject(j);
            int typeAccount = 0;
            try {
                typeAccount = Integer.parseInt(Controller.currentUser.accountInfo.getString("typeOfAccount"));
            } catch (JSONException je) {
                Log.w("MA", "Error in GoToDashboard");
            }

            if (typeAccount == Controller.OWNER_ACCOUNT) {
                if (jobObj.getInt("ownerIDKey") == Controller.currentUser.accountInfo.getInt("ownerIDKey")){
                    SittingJob ownerJob = new SittingJob(jobObj);
                    ownersJobs.add(ownerJob);
                }
            } else if (typeAccount == Controller.SITTER_ACCOUNT) {
                if (jobObj.getInt("sitterIDKey") == Controller.currentUser.accountInfo.getInt("ownerIDKey")){
                    SittingJob ownerJob = new SittingJob(jobObj);
                    sittersJobs.add(ownerJob);
                }
            } else {
                if (jobObj.getInt("ownerIDKey") == Controller.currentUser.accountInfo.getInt("ownerIDKey")){
                    SittingJob ownerJob = new SittingJob(jobObj);
                    ownersJobs.add(ownerJob);
                }
                if(jobObj.getInt("sitterIDKey") == Controller.currentUser.accountInfo.getInt("ownerIDKey")){
                    SittingJob ownerJob = new SittingJob(jobObj);
                    sittersJobs.add(ownerJob);
                }
            }
        }
        Controller.currentUser.updateOwnerJobs(ownersJobs);
        Controller.currentUser.updateSitterJobs(sittersJobs);

        System.out.println(ownersJobs.toString());
        System.out.println(sittersJobs.toString());
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
                //System.out.println(sb.toString().trim());
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
                //System.out.println(sb.toString().trim());
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
            pets[i] = obj.getString("firstName");
            System.out.println("name");
        }
    }

    private class GetJobsJson extends AsyncTask<Void, Void, String> {

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
                loadUsersJobs(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(jobsQuery);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }
                //System.out.println(sb.toString().trim());
                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }
    }

    /**
     * Method to call the getJSON object
     */
    private void getJobs() {
        GetJobsJson getJSON = new GetJobsJson();
        getJSON.execute();
    }

    private void loadUsersJobs(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        System.out.println(jsonArray.toString());
        allJobs = jsonArray;
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            if(obj.getInt("sitterIDKey") == 0){
                SittingJob openJob = new SittingJob(obj);
                openJobs.add(openJob);
            }
        }
        System.out.println("Open Jobs: "+ openJobs.toString());
    }


    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


}

