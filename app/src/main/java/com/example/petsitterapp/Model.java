package com.example.petsitterapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Model {
    private String petQuery = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/query.php";
    private String ownerQuery = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/getOwners.php";
    private String jobsQuery = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/getJobs.php";

    private JSONArray allPets;
    private JSONArray allOwners;
    public static JSONArray allJobs;

    public static ArrayList<Pet> usersPets = new ArrayList<>();;
    public static ArrayList<SittingJob> ownersJobs = new ArrayList<>();
    public static ArrayList<SittingJob> sittersJobs = new ArrayList<>();
    public static ArrayList<SittingJob> openJobs = new ArrayList<>();

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
                buildPetList(userID);
                User newUser = new User(userID, obj, usersPets);
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
                String emailURL = "http://damorales.cs.loyola.edu/PetSitterApp/app/src/main/php/sendEmail.php?email="+obj.getString("email");
                AddPet sendEmail = new AddPet(emailURL);
                sendEmail.execute();
            }

        }


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



//    public class Owner {
//        HashMap<Integer, OldPet> petMap;
//        int ownerID;
//
//        /**
//         * This constructor is for an existing Owner, accessed by login page
//         * @return true if the login and password are correct, false if they are not
//         */
//        public Owner(int ownerID)  {
//            try {
//                this.ownerID = ownerID;
//                petMap = new HashMap<Integer, OldPet>();
//                // DEREK search the Pet database for any matches with the ownerID
//                for (int i = 0; i < allPets.length(); i++) {
//                    JSONObject obj = allPets.getJSONObject(i);
//                    String petsOwners = obj.getString("OwnerIDKey");
//                    if (Integer.parseInt(petsOwners) == ownerID) {
//                        OldPet newPet = new OldPet(obj, Integer.parseInt(petsOwners));
//                        int petIdKey = Integer.parseInt(obj.getString("PetIDKey"));
//                        petMap.put(petIdKey, newPet);
//                    }
//                }
//            }
//            catch(JSONException je){ }
//            System.out.println(petMap.toString());
//            // DEREK create a Pet object from each match of the ownerID, store them in petMap with their petIDs as keys
//        }


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

}

